package br.inatel.thisismeapi.units.services.impl;

import br.inatel.thisismeapi.entities.Character;
import br.inatel.thisismeapi.entities.Quest;
import br.inatel.thisismeapi.entities.Skill;
import br.inatel.thisismeapi.entities.SubQuest;
import br.inatel.thisismeapi.enums.DayOfWeekCustom;
import br.inatel.thisismeapi.enums.QuestStatus;
import br.inatel.thisismeapi.exceptions.NotFoundException;
import br.inatel.thisismeapi.exceptions.OnCreateDataException;
import br.inatel.thisismeapi.exceptions.OnCreateSubQuestException;
import br.inatel.thisismeapi.models.Day;
import br.inatel.thisismeapi.repositories.QuestRepository;
import br.inatel.thisismeapi.repositories.SkillRepository;
import br.inatel.thisismeapi.repositories.SubQuestRepository;
import br.inatel.thisismeapi.services.CharacterService;
import br.inatel.thisismeapi.services.impl.SubQuestsServiceImpl;
import br.inatel.thisismeapi.units.classesToTest.EmailConstToTest;
import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.doNothing;

@SpringBootTest(classes = {SubQuestsServiceImpl.class})
public class SubQuestsServiceTest {

    @Autowired
    private SubQuestsServiceImpl subQuestsService;


    @MockBean
    private SubQuestRepository subQuestRepository;

    @MockBean
    private CharacterService characterService;

    @MockBean
    private QuestRepository questRepository;

    @MockBean
    private SkillRepository skillRepository;

    @Test
    void testCreateSubQuestByQuestSuccess() {

        LocalDateTime today = LocalDateTime.of(LocalDate.of(2022, 11, 6), LocalTime.of(12, 0));

        Quest quest = this.getInstanceOfQuestWithoutWeek();
        quest.setStartDate(LocalDate.of(2022, 11, 6));
        quest.setEndDate(LocalDate.of(2022, 11, 12));
        quest.setWeek(this.getOneOfEachDayOfTheWeekStartAtTenAndFinishingAtElevenOfMorning());

        try (MockedStatic<LocalDateTime> localDateMockedStatic = mockStatic(LocalDateTime.class)) {
            localDateMockedStatic.when(LocalDateTime::now).thenReturn(today);
            localDateMockedStatic.when(() -> LocalDateTime.of(any(LocalDate.class), any(LocalTime.class))).thenCallRealMethod();
            List<SubQuest> subQuestsList = new ArrayList<>();
            subQuestsList.add(new SubQuest());
            subQuestsList.add(new SubQuest());
            when(subQuestRepository.saveAll(any())).thenReturn(subQuestsList);
            List<SubQuest> result = this.subQuestsService.createSubQuestByQuest(quest, EmailConstToTest.EMAIL_DEFAULT);

            assertEquals(2, result.size());
        } catch (OnCreateSubQuestException e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    void testCreateSubQuestByQuestThrowExceptionWhenWeekFromQuestIsEmpty() {

        LocalDateTime today = LocalDateTime.of(LocalDate.of(2022, 11, 6), LocalTime.of(12, 0));

        Quest quest = this.getInstanceOfQuestWithoutWeek();
        quest.setStartDate(LocalDate.of(2022, 11, 6));
        quest.setEndDate(LocalDate.of(2022, 11, 12));

        OnCreateSubQuestException exception = assertThrows(OnCreateSubQuestException.class, () -> {
            this.subQuestsService.createSubQuestByQuest(quest, EmailConstToTest.EMAIL_DEFAULT);
        });

        assertEquals("Não pode criar uma tarefa com a semana vazia, por favor, selecione pelo menos um dia da semana!", exception.getMessage());
    }

    @Test
    void testCreateSubQuestByQuestThrowExceptionWhenNoSubQuestCreated() {

        LocalDateTime today = LocalDateTime.of(LocalDate.of(2023, 11, 6), LocalTime.of(12, 0));

        Quest quest = this.getInstanceOfQuestWithoutWeek();
        quest.setStartDate(LocalDate.of(2022, 11, 6));
        quest.setEndDate(LocalDate.of(2022, 11, 12));
        quest.setWeek(this.getOneOfEachDayOfTheWeekStartAtTenAndFinishingAtElevenOfMorning());

        try (MockedStatic<LocalDateTime> localDateMockedStatic = mockStatic(LocalDateTime.class)) {
            localDateMockedStatic.when(LocalDateTime::now).thenReturn(today);
            localDateMockedStatic.when(() -> LocalDateTime.of(any(LocalDate.class), any(LocalTime.class))).thenCallRealMethod();
            List<SubQuest> subQuestsList = new ArrayList<>();
            subQuestsList.add(new SubQuest());
            subQuestsList.add(new SubQuest());
            when(subQuestRepository.saveAll(any())).thenReturn(subQuestsList);
            OnCreateSubQuestException exception = assertThrows(OnCreateSubQuestException.class, () -> {
                this.subQuestsService.createSubQuestByQuest(quest, EmailConstToTest.EMAIL_DEFAULT);
            });

            assertEquals("Nenhuma tarefa criada com o periodo e os dias da semana selecionados!", exception.getMessage());
        }
    }

    @Test
    void testFindAllSubQuestsTodaySuccess() {

        LocalDate today = LocalDate.of(2022, 11, 6);


        try (MockedStatic<LocalDate> localDateMockedStatic = mockStatic(LocalDate.class)) {

            localDateMockedStatic.when(LocalDate::now).thenReturn(today);
            List<SubQuest> subQuestsList = new ArrayList<>();
            subQuestsList.add(new SubQuest());
            subQuestsList.add(new SubQuest());
            when(subQuestRepository.findAllSubQuestsByRangeDate(any(), any(), any())).thenReturn(subQuestsList);
            List<SubQuest> result = this.subQuestsService.findAllSubQuestsToday(EmailConstToTest.EMAIL_DEFAULT);

            assertEquals(2, result.size());
        }
    }

    private Quest getInstanceOfQuestWithoutWeek() {

        Quest quest = new Quest();
        quest.setName("Quest");
        quest.setStartDate(LocalDate.now());
        quest.setStartDate(LocalDate.now().plusDays(6));
        quest.setEmail(EmailConstToTest.EMAIL_DEFAULT);
        quest.setHexColor("#000000");
        quest.setDesc("desc");
        quest.setStatus(QuestStatus.IN_PROGRESS);

        return quest;
    }

    private List<Day> getOneOfEachDayOfTheWeekStartAtTenAndFinishingAtElevenOfMorning() {

        List<Day> week = new ArrayList<>();

        for (int i = 1; i <= 7; i++) {
            Day day = new Day();
            day.setDayOfWeekCustom(DayOfWeekCustom.of(i));
            day.setStartTime("10:00");
            day.setEndTime("11:00");
            week.add(day);
        }

        Collections.shuffle(week);
        return week;
    }

    @Test
    void testFindAllSubQuestsWeeklySuccess() {

        LocalDate today = LocalDate.of(2022, 11, 6);

        try (MockedStatic<LocalDate> localDateMockedStatic = mockStatic(LocalDate.class)) {

            localDateMockedStatic.when(LocalDate::now).thenReturn(today);
            List<SubQuest> subQuestsList = new ArrayList<>();
            subQuestsList.add(new SubQuest());
            subQuestsList.add(new SubQuest());
            when(subQuestRepository.findAllSubQuestsByRangeDate(any(), any(), any())).thenReturn(subQuestsList);
            List<SubQuest> result = this.subQuestsService.findAllSubQuestsWeekly(EmailConstToTest.EMAIL_DEFAULT);

            assertEquals(2, result.size());
        }
    }

    @Test
    void testFindAllSubQuestsFromNextSuccess() {

        LocalDate today = LocalDate.of(2022, 11, 6);

        try (MockedStatic<LocalDate> localDateMockedStatic = mockStatic(LocalDate.class)) {

            localDateMockedStatic.when(LocalDate::now).thenReturn(today);
            List<SubQuest> subQuestsList = new ArrayList<>();
            subQuestsList.add(new SubQuest());
            subQuestsList.add(new SubQuest());
            when(subQuestRepository.findAllSubQuestsByRangeDate(any(), any(), any())).thenReturn(subQuestsList);
            List<SubQuest> result = this.subQuestsService.findAllSubQuestsFromNextWeek(EmailConstToTest.EMAIL_DEFAULT);

            assertEquals(2, result.size());
        }
    }

    @Test
    void testFindAllSubQuestsLateSuccess() {

        LocalDate today = LocalDate.of(2022, 11, 6);

        try (MockedStatic<LocalDate> localDateMockedStatic = mockStatic(LocalDate.class)) {

            localDateMockedStatic.when(LocalDate::now).thenReturn(today);
            List<SubQuest> subQuestsList = new ArrayList<>();
            subQuestsList.add(new SubQuest());
            subQuestsList.add(new SubQuest());
            when(subQuestRepository.findAllSubQuestNotCheckUntilDate(any(), any())).thenReturn(subQuestsList);
            List<SubQuest> result = this.subQuestsService.findAllSubQuestsLate(EmailConstToTest.EMAIL_DEFAULT);

            assertEquals(2, result.size());
        }
    }

    @Test
    void testDeleteAllSubQuestByEmailSuccess() {

        doNothing().when(subQuestRepository).deleteAllByEmail(any());
        this.subQuestsService.deleteAllSubQuestByEmail(EmailConstToTest.EMAIL_DEFAULT);
        verify(subQuestRepository, times(1)).deleteAllByEmail(any());
    }

    @Test
    void testDeleteSubQuestByIdSuccess() {

        doNothing().when(subQuestRepository).deleteById(anyString());
        this.subQuestsService.deleteSubQuestById("123456");
        verify(subQuestRepository).deleteById(any());
    }

    @Test
    void testCheckAndUncheckSubQuestThrowExceptionWhenDontFindSubQuest() {

        when(subQuestRepository.findByIdAndEmail(any(), any())).thenReturn(Optional.empty());
        NotFoundException exception = assertThrows(NotFoundException.class, () -> {
            this.subQuestsService.checkAndUncheckSubQuest("123456", EmailConstToTest.EMAIL_DEFAULT);
        });

        assertEquals("Sub Quest não encontrada!", exception.getMessage());
    }

    @Test
    void testCheckAndUncheckSubQuestWithSubQuestChecked() {

        Quest quest = new Quest();
        SubQuest subQuest = new SubQuest();
        subQuest.setQuest(quest);
        subQuest.setCheck(true);
        subQuest.setXp(10L);

        when(subQuestRepository.findByIdAndEmail(any(), any())).thenReturn(Optional.of(subQuest));
        when(characterService.findCharacterByEmail(any())).thenReturn(new Character());
        SubQuest actual = this.subQuestsService.checkAndUncheckSubQuest("123456", EmailConstToTest.EMAIL_DEFAULT);

        assertFalse(subQuest.isCheck());
        verify(subQuestRepository).save(any());
    }

    @Test
    void testCheckAndUncheckSubQuestWithSubQuestUnchecked() {

        Quest quest = new Quest();
        SubQuest subQuest = new SubQuest();
        subQuest.setQuest(quest);
        subQuest.setCheck(false);
        subQuest.setXp(10L);

        when(subQuestRepository.findByIdAndEmail(any(), any())).thenReturn(Optional.of(subQuest));
        when(characterService.findCharacterByEmail(any())).thenReturn(new Character());
        this.subQuestsService.checkAndUncheckSubQuest("123456", EmailConstToTest.EMAIL_DEFAULT);

        assertTrue(subQuest.isCheck());
        verify(subQuestRepository).save(any());
    }

    @Test
    void testCheckAndUncheckedSubQuestWhenSubQuestIsCheckedAndSkillIsNotNull() {

        Quest quest = new Quest();
        SubQuest subQuest = new SubQuest();
        subQuest.setQuest(quest);
        subQuest.setCheck(true);
        subQuest.setXp(10L);
        subQuest.getQuest().setSkill(new Skill());

        when(subQuestRepository.findByIdAndEmail(any(), any())).thenReturn(Optional.of(subQuest));
        when(characterService.findCharacterByEmail(any())).thenReturn(new Character());
        this.subQuestsService.checkAndUncheckSubQuest("123456", EmailConstToTest.EMAIL_DEFAULT);

        assertFalse(subQuest.isCheck());
        verify(subQuestRepository).save(any());
    }

    @Test
    void testCheckAndUncheckedSubQuestWhenSubQuestIsUncheckedAndSkillIsNotNull() {

        Quest quest = new Quest();
        SubQuest subQuest = new SubQuest();
        subQuest.setQuest(quest);
        subQuest.setCheck(false);
        subQuest.setXp(10L);
        subQuest.getQuest().setSkill(new Skill());

        when(subQuestRepository.findByIdAndEmail(any(), any())).thenReturn(Optional.of(subQuest));
        when(characterService.findCharacterByEmail(any())).thenReturn(new Character());
        this.subQuestsService.checkAndUncheckSubQuest("123456", EmailConstToTest.EMAIL_DEFAULT);

        assertTrue(subQuest.isCheck());
        verify(subQuestRepository).save(any());
    }

//    @Test
//    void testCheckAndUncheckSubQuestSuccessWhenSubQuestIsNotCheckedAndHasSkill() {
//
//        SubQuest subQuest = new SubQuest();
//        Quest quest = new Quest();
//        quest.setSkill(new Skill());
//        subQuest.setCheck(false);
//
//        when(subQuestRepository.findByIdAndEmail(any(), any())).thenReturn(Optional.of(subQuest));
//        when(characterService.findCharacterByEmail(any())).thenReturn(new Character());
//        when(skillRepository.save(any())).thenReturn(new Skill());
//        when(questRepository.save(any())).thenReturn(quest);
//        when(characterService.updateCharacter(any())).thenReturn(new Character());
//        when(subQuestRepository.save(any())).thenReturn(subQuest);
//
//
//        verify(subQuestRepository).save(any());
//    }

}
