package br.inatel.thisismeapi.services.impl;

import br.inatel.thisismeapi.entities.Character;
import br.inatel.thisismeapi.entities.Quest;
import br.inatel.thisismeapi.entities.Skill;
import br.inatel.thisismeapi.entities.SubQuest;
import br.inatel.thisismeapi.enums.QuestStatus;
import br.inatel.thisismeapi.exceptions.NotFoundException;
import br.inatel.thisismeapi.exceptions.OnCreateSubQuestException;
import br.inatel.thisismeapi.exceptions.QuestValidationsException;
import br.inatel.thisismeapi.repositories.QuestRepository;
import br.inatel.thisismeapi.repositories.SkillRepository;
import br.inatel.thisismeapi.services.CharacterService;
import br.inatel.thisismeapi.services.QuestService;
import br.inatel.thisismeapi.services.SubQuestsService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class QuestServiceImpl implements QuestService {

    private static final Logger LOGGER = LoggerFactory.getLogger(QuestServiceImpl.class);

    @Autowired
    private QuestRepository questRepository;

    @Autowired
    private CharacterService characterService;

    @Autowired
    private SubQuestsService subQuestsService;

    @Autowired
    private SkillRepository skillRepository;


    @Override
    @Transactional
    public Quest createNewQuest(Quest quest, String email) {

        LOGGER.info("m=createNewQuest, email={}, questName={}", email, quest.getName());
        this.validateQuest(quest);

        Character character = characterService.findCharacterByEmail(email);
        quest.setEmail(email);
        quest.setStatus(QuestStatus.IN_PROGRESS);

        Skill skill = this.createSkill(quest.getSkill(), character, email);

        quest.setSkill(skill);
        Quest savedQuest = questRepository.save(quest);
        List<SubQuest> subQuests = new ArrayList<>();
        try {
            subQuests = subQuestsService.createSubQuestByQuest(savedQuest, email);
        } catch (OnCreateSubQuestException e) {
            questRepository.delete(savedQuest);
            throw new QuestValidationsException(e.getMessage());
        }

        savedQuest.setTotal((long) subQuests.size());
        savedQuest.setTotalXp(this.calcutateTotalXp(subQuests));
        savedQuest = questRepository.save(savedQuest);
        character.getQuests().add(savedQuest);

        characterService.updateCharacter(character);
        return savedQuest;
    }

    @Override
    public List<Quest> getQuestToday(String email) {

        LOGGER.info("m=getQuestToday, email={}", email);
        return questRepository.findAllQuestsByDate(email, LocalDate.now());
    }

    @Override
    public Quest getQuestById(String id, String email) {

        LOGGER.info("m=getQuestById, email={}, id={}", email, id);

        return questRepository.findQuestByIdAndEmail(id, email)
                .orElseThrow(() -> new NotFoundException("Quest não encontrada!"));
    }

    @Override
    public void updateQuest(Quest quest, String email) {

        LOGGER.info("m=updateQuest, email={}, questName={}", email, quest.getName());
        this.validateQuest(quest);

        if (quest.getQuestId() == null)
            throw new QuestValidationsException("Quest não pode ser atualizada sem id");

        questRepository.save(quest);
    }

    public void deleteAllQuestsByEmail(String email) {

        LOGGER.info("m=deleteAllQuestByEmail, email={}", email);
        questRepository.deleteAllQuestsByEmail(email);
    }

    private Boolean validateQuest(Quest quest) {

        LOGGER.info("m=validateQuest");

        if (quest.getName() == null || quest.getName().isEmpty())
            throw new QuestValidationsException("Quest deve ter um nome!");

        if (quest.getStartDate() == null || quest.getEndDate() == null)
            throw new QuestValidationsException("Período não pode ser nulo!");

        if (quest.getHexColor() == null || quest.getHexColor().isEmpty())
            throw new QuestValidationsException("Cor não pode ser nula!");

        if (quest.getStartDate().isBefore(LocalDate.now()))
            throw new QuestValidationsException("Data de inicio precisa ser maior ou igual a data do dia atual!");

        if (quest.getStartDate().isAfter(quest.getEndDate()))
            throw new QuestValidationsException("Data de inicio precisa ser maior que a data final!");

        if (quest.getWeek().isEmpty())
            throw new QuestValidationsException("Não pode criar uma tarefa com a semana vazia, por favor, selecione pelo menos um dia da semana!");

        return true;
    }

    private Long calcutateTotalXp(List<SubQuest> subQuest) {

        LOGGER.info("m=calcutateTotalXp");
        Long totalXp = 0L;
        for (SubQuest sub : subQuest) {
            totalXp += sub.getXp();
        }
        return totalXp;
    }

    private Skill createSkill(Skill skill, Character character, String email) {
        if (skill != null && skill.getName() != null && !skill.getName().isEmpty()) {
            Optional<Skill> skillOptional = skillRepository.findByNameAndEmail(skill.getName(), email);
            if (skillOptional.isPresent()) {
                return skillOptional.get();
            } else {
                skill.setEmail(email);
                skill = skillRepository.save(skill);
                List<Skill> skills = character.getSkills();
                skills.add(skill);
                character.setSkills(skills);
                characterService.updateCharacter(character);
                return skill;
            }
        }

        return null;
    }
}
