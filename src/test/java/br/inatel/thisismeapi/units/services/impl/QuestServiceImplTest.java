package br.inatel.thisismeapi.units.services.impl;

import br.inatel.thisismeapi.entities.Character;
import br.inatel.thisismeapi.entities.Quest;
import br.inatel.thisismeapi.entities.Skill;
import br.inatel.thisismeapi.entities.SubQuest;
import br.inatel.thisismeapi.enums.DayOfWeekCustom;
import br.inatel.thisismeapi.exceptions.NotFoundException;
import br.inatel.thisismeapi.exceptions.OnCreateSubQuestException;
import br.inatel.thisismeapi.exceptions.QuestValidationsException;
import br.inatel.thisismeapi.models.Day;
import br.inatel.thisismeapi.repositories.QuestRepository;
import br.inatel.thisismeapi.repositories.SkillRepository;
import br.inatel.thisismeapi.services.impl.CharacterServiceImpl;
import br.inatel.thisismeapi.services.impl.QuestServiceImpl;
import br.inatel.thisismeapi.services.impl.SubQuestsServiceImpl;
import br.inatel.thisismeapi.units.classesToTest.EmailConstToTest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@SpringBootTest(classes = {QuestServiceImpl.class})
class QuestServiceImplTest {

    @Autowired
    private QuestServiceImpl questService;

    @MockBean
    private QuestRepository questRepository;

    @MockBean
    private CharacterServiceImpl characterService;

    @MockBean
    private SubQuestsServiceImpl subQuestsService;

    @MockBean
    private SkillRepository skillRepository;

    @Test
    void testCreateNewQuestSuccess() throws OnCreateSubQuestException {

        // given
        String email = EmailConstToTest.EMAIL_DEFAULT;
        Quest quest = this.getInstanceOfQuest();
        quest.setStartDate(LocalDate.now());
        quest.setEndDate(LocalDate.now().plusDays(1));
        List<SubQuest> subQuestList = new ArrayList<>();
        subQuestList.add(getInstanceOfSubQuest());
        Character character = new Character();
        character.setEmail(email);

        // when
        when(characterService.findCharacterByEmail(email)).thenReturn(character);
        when(questRepository.save(quest)).thenReturn(quest);
        when(subQuestsService.createSubQuestByQuest(quest, email)).thenReturn(subQuestList);
        when(questRepository.save(quest)).thenReturn(quest);
        when(characterService.updateCharacter(character)).thenReturn(character);

        Quest result = questService.createNewQuest(quest, email);

        // then
        assertEquals(email, result.getEmail());
        assertEquals("quest", result.getName());
    }

    @Test
    void testCreateNewQuestWithNewSkillSuccess() throws OnCreateSubQuestException{

        String email = EmailConstToTest.EMAIL_DEFAULT;
        Quest quest = this.getInstanceOfQuest();
        quest.setSkill(new Skill());
        quest.setStartDate(LocalDate.now());
        quest.setEndDate(LocalDate.now().plusDays(1));
        List<SubQuest> subQuestList = new ArrayList<>();
        subQuestList.add(getInstanceOfSubQuest());
        Character character = new Character();
        character.setEmail(email);

        when(characterService.findCharacterByEmail(email)).thenReturn(character);
        when(questRepository.save(quest)).thenReturn(quest);
        when(subQuestsService.createSubQuestByQuest(quest, email)).thenReturn(subQuestList);
        when(questRepository.save(quest)).thenReturn(quest);
        when(characterService.updateCharacter(character)).thenReturn(character);
        when(skillRepository.save(any(Skill.class))).thenReturn(new Skill());

        Quest result = questService.createNewQuest(quest, email);

        assertEquals(email, result.getEmail());
        assertEquals("quest", result.getName());
    }

    @Test
    void testCreateNewQuestWithSkillAlreadyExistsSuccess() throws OnCreateSubQuestException{

        String email = EmailConstToTest.EMAIL_DEFAULT;
        Quest quest = this.getInstanceOfQuest();
        quest.setSkill(new Skill());
        quest.setStartDate(LocalDate.now());
        quest.setEndDate(LocalDate.now().plusDays(1));
        List<SubQuest> subQuestList = new ArrayList<>();
        subQuestList.add(getInstanceOfSubQuest());
        Character character = new Character();
        character.setEmail(email);

        when(characterService.findCharacterByEmail(email)).thenReturn(character);
        when(questRepository.save(quest)).thenReturn(quest);
        when(subQuestsService.createSubQuestByQuest(quest, email)).thenReturn(subQuestList);
        when(questRepository.save(quest)).thenReturn(quest);
        when(characterService.updateCharacter(character)).thenReturn(character);
        when(skillRepository.findByNameAndEmail(anyString(), anyString())).thenReturn(Optional.of(new Skill()));

        Quest result = questService.createNewQuest(quest, email);

        assertEquals(email, result.getEmail());
        assertEquals("quest", result.getName());
    }

    @Test
    void testCreateNewQuestThrowExceptionWhenStartDateLessThanEndDate() {

        Quest quest = new Quest();
        quest.setName("quest");
        quest.setHexColor("color");
        quest.setStartDate(LocalDate.now());
        quest.setEndDate(LocalDate.now().minusDays(1));


        QuestValidationsException exception = assertThrows(QuestValidationsException.class, () -> {
            questService.createNewQuest(quest, EmailConstToTest.EMAIL_DEFAULT);
        });

        assertEquals("Data de inicio precisa ser maior que a data final!", exception.getMessage());
    }

    @Test
    void testCreateNewQuestThrowExceptionWhenStartDateLessThanCurrentDate() {

        Quest quest = this.getInstanceOfQuest();
        quest.setStartDate(LocalDate.now().minusDays(100));

        QuestValidationsException exception = assertThrows(QuestValidationsException.class, () -> {
            questService.createNewQuest(quest, EmailConstToTest.EMAIL_DEFAULT);
        });

        assertEquals("Data de inicio precisa ser maior ou igual a data do dia atual!", exception.getMessage());
    }

    @Test
    void testCreateNewQuestThrowExceptionWhenNameIsBlank() {

        Quest quest = new Quest();
        quest.setName("");

        QuestValidationsException exception = assertThrows(QuestValidationsException.class, () -> {
            questService.createNewQuest(quest, EmailConstToTest.EMAIL_DEFAULT);
        });

        assertEquals("Quest deve ter um nome!", exception.getMessage());
    }

    @Test
    void testCreateNewQuestThrowExceptionWhenStartDateIsNull() {

        Quest quest = new Quest();
        quest.setName("quest");
        quest.setEndDate(LocalDate.now().plusDays(1));
        quest.setHexColor("color");

        QuestValidationsException exception = assertThrows(QuestValidationsException.class, () -> {
            questService.createNewQuest(quest, EmailConstToTest.EMAIL_DEFAULT);
        });

        assertEquals("Período não pode ser nulo!", exception.getMessage());
    }

    @Test
    void testCreateNewQuestThrowExceptionWhenEndDateIsNull() {

        Quest quest = new Quest();
        quest.setName("quest");
        quest.setStartDate(LocalDate.now());
        quest.setHexColor("color");

        QuestValidationsException exception = assertThrows(QuestValidationsException.class, () -> {
            questService.createNewQuest(quest, EmailConstToTest.EMAIL_DEFAULT);
        });

        assertEquals("Período não pode ser nulo!", exception.getMessage());
    }

    @Test
    void testCreateNewQuestThrowExceptionWhenHexColorIsBlank() {

        Quest quest = this.getInstanceOfQuest();
        quest.setHexColor("");

        QuestValidationsException exception = assertThrows(QuestValidationsException.class, () -> {
            questService.createNewQuest(quest, EmailConstToTest.EMAIL_DEFAULT);
        });

        assertEquals("Cor não pode ser nula!", exception.getMessage());
    }

    @Test
    void testCreateNewQuestThrowExceptionWhenWeekIsEmpty() {

        Quest quest = this.getInstanceOfQuest();
        quest.setWeek(new ArrayList<>());

        QuestValidationsException exception = assertThrows(QuestValidationsException.class, () -> {
            questService.createNewQuest(quest, EmailConstToTest.EMAIL_DEFAULT);
        });

        assertEquals("Não pode criar uma tarefa com a semana vazia, por favor, selecione pelo menos um dia da semana!", exception.getMessage());
    }

    @Test
    void testCreateNewQuestThrowExceptionWhenCatchExceptionOnCreateSubQuest() throws OnCreateSubQuestException {

        Quest quest = this.getInstanceOfQuest();
        List<Day> week = new ArrayList<>();
        week.add(new Day());
        quest.setWeek(week);

        when(characterService.findCharacterByEmail(EmailConstToTest.EMAIL_DEFAULT)).thenReturn(new Character());
        when(questRepository.save(quest)).thenReturn(quest);
        when(subQuestsService.createSubQuestByQuest(quest, EmailConstToTest.EMAIL_DEFAULT)).thenThrow(new OnCreateSubQuestException("Erro ao criar subquest"));


        QuestValidationsException exception = assertThrows(QuestValidationsException.class, () -> {
            questService.createNewQuest(quest, EmailConstToTest.EMAIL_DEFAULT);
        });

        assertEquals("Erro ao criar subquest", exception.getMessage());
    }

    @Test
    void testGetQuestTodaySuccess() {

        List<Quest> questListExpected = new ArrayList<>();
        questListExpected.add(new Quest());
        questListExpected.add(new Quest());

        when(questRepository.findAllQuestsByDate(any(), any())).thenReturn(questListExpected);

        List<Quest> questListActual = questService.getQuestToday(EmailConstToTest.EMAIL_DEFAULT);

        assertEquals(questListExpected, questListActual);
    }

    @Test
    void testDeleteAllQuestsSuccess() {

        doNothing().when(questRepository).deleteAllQuestsByEmail(EmailConstToTest.EMAIL_DEFAULT);

        questService.deleteAllQuestsByEmail(EmailConstToTest.EMAIL_DEFAULT);

        verify(questRepository).deleteAllQuestsByEmail(EmailConstToTest.EMAIL_DEFAULT);
    }

    @Test
    void testGetQuestByIdSuccess() {

        Quest quest = this.getInstanceOfQuest();
        when(questRepository.findQuestByIdAndEmail(quest.getQuestId(), EmailConstToTest.EMAIL_DEFAULT)).thenReturn(Optional.of(quest));

        Quest questActual = questService.getQuestById(quest.getQuestId(), EmailConstToTest.EMAIL_DEFAULT);

        assertEquals(quest, questActual);
    }

    @Test
    void testGetQuestByIdThrowExceptionWhenQuestNotFound() {


        when(questRepository.findQuestByIdAndEmail("123456", EmailConstToTest.EMAIL_DEFAULT)).thenReturn(Optional.empty());

        NotFoundException exception = assertThrows(NotFoundException.class, () -> {
            questService.getQuestById("123456", EmailConstToTest.EMAIL_DEFAULT);
        });

        assertEquals("Quest não encontrada!", exception.getMessage());
    }

    @Test
    void testUpdateQuestSuccess() {

        Quest quest = this.getInstanceOfQuest();
        List<Day> week = new ArrayList<>();
        week.add(new Day());
        quest.setWeek(week);
        quest.setQuestId("123456");

        when(questRepository.save(quest)).thenReturn(quest);

        questService.updateQuest(quest, EmailConstToTest.EMAIL_DEFAULT);

        verify(questRepository).save(quest);
    }

    @Test
    void testUpdateQuestThrowExceptionWhenQuestIdIsNull() {

        Quest quest = this.getInstanceOfQuest();
        List<Day> week = new ArrayList<>();
        week.add(new Day());
        quest.setWeek(week);

        QuestValidationsException exception = assertThrows(QuestValidationsException.class, () -> {
            questService.updateQuest(quest, EmailConstToTest.EMAIL_DEFAULT);
        });

        assertEquals("Quest não pode ser atualizada sem id", exception.getMessage());
    }


    private Quest getInstanceOfQuest(){

        Quest quest = new Quest();

        quest.setName("quest");
        quest.setStartDate(LocalDate.now());
        quest.setEndDate(LocalDate.now().plusDays(1));
        quest.setHexColor("color");
        List<Day> week = new ArrayList<>();
        week.add(new Day());
        quest.setWeek(week);

        return quest;
    }

    private SubQuest getInstanceOfSubQuest(){

        SubQuest subQuest = new SubQuest();

        subQuest.setDurationInMin(10L);
        subQuest.setXp(10L);
        subQuest.setDayOfWeekEnum(DayOfWeekCustom.FRIDAY);

        return subQuest;
    }
}
