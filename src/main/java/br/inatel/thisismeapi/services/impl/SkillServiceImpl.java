package br.inatel.thisismeapi.services.impl;

import br.inatel.thisismeapi.entities.Character;
import br.inatel.thisismeapi.entities.Skill;
import br.inatel.thisismeapi.exceptions.SkillAlreadyExistsException;
import br.inatel.thisismeapi.repositories.SkillRepository;
import br.inatel.thisismeapi.services.CharacterService;
import br.inatel.thisismeapi.services.SkillService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SkillServiceImpl implements SkillService {

    @Autowired
    private SkillRepository skillRepository;

    @Autowired
    private CharacterService characterService;

    @Override
    public Skill createSkill(Skill skill, String email) {

        if (skill.getName().isBlank())
            throw new IllegalArgumentException("Nome da skill não pode ser vazio");

        if (skill.getName().length() > 20)
            throw new IllegalArgumentException("Nome da skill não pode ter mais de 20 caracteres");

        if (skillRepository.findByNameAndEmail(skill.getName(), email).isPresent())
            throw new SkillAlreadyExistsException("Skill já existe");

        skill.setEmail(email);
        try{
            skill = skillRepository.save(skill);
        } catch (Exception e) {
            throw new IllegalArgumentException("Erro ao salvar skill");
        }
        Character character = characterService.findCharacterByEmail(email);
        List<Skill> skills = character.getSkills();
        skills.add(skill);
        character.setSkills(skills);
        characterService.updateCharacter(character);
        return skill;
    }

    @Override
    public List<Skill> getAllSkills(String email) {

        return skillRepository.findAllByEmail(email);
    }
}
