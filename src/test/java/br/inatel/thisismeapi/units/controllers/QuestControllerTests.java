package br.inatel.thisismeapi.units.controllers;


import br.inatel.thisismeapi.controllers.QuestController;
import br.inatel.thisismeapi.controllers.dtos.responses.QuestResponseDTO;
import br.inatel.thisismeapi.entities.Quest;
import br.inatel.thisismeapi.enums.DayOfWeekCustom;
import br.inatel.thisismeapi.enums.QuestStatus;
import br.inatel.thisismeapi.models.Day;
import br.inatel.thisismeapi.services.impl.QuestServiceImpl;
import br.inatel.thisismeapi.units.classesToTest.EmailConstToTest;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.core.Authentication;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@SpringBootTest(classes = {QuestController.class})
class QuestControllerTests {

    @Autowired
    private QuestController questController;

    @MockBean
    private QuestServiceImpl questService;

    @Mock
    private Authentication authentication;

    @Test
    void testCreateNewQuestSuccess() {

        Quest expectedQuest = new Quest();
        expectedQuest.setEmail(EmailConstToTest.EMAIL_DEFAULT);
        expectedQuest.setName("QuestTest");

        when(authentication.getName()).thenReturn(EmailConstToTest.EMAIL_DEFAULT);
        Mockito.when(questService.createNewQuest(any(), anyString())).thenReturn(expectedQuest);

        questController.createNewQuest(expectedQuest, authentication);

        verify(questService).createNewQuest(any(), anyString());
    }

    @Test
    void testGetDayCardsSuccessWithTwoQuest() {

        Quest expectedQuest = new Quest();
        expectedQuest.setEmail(EmailConstToTest.EMAIL_DEFAULT);
        expectedQuest.setName("QuestTest");

        List<Quest> list = new ArrayList<>();
        list.add(expectedQuest);
        list.add(expectedQuest);

        when(authentication.getName()).thenReturn(EmailConstToTest.EMAIL_DEFAULT);
        Mockito.when(questService.getQuestToday(anyString())).thenReturn(list);

        List<Quest> questListActual = questController.getDayCards(authentication);

        assertEquals(list, questListActual);
    }

    @Test
    void testGetQuestByIdSuccess() {

        Quest expectedQuest = getQuest();

        QuestResponseDTO expectedQuestResponseDTO = new QuestResponseDTO(expectedQuest);

        when(authentication.getName()).thenReturn(EmailConstToTest.EMAIL_DEFAULT);
        Mockito.when(questService.getQuestById(anyString(), anyString())).thenReturn(expectedQuest);

        QuestResponseDTO actual = questController.getQuestById("1", authentication);

        assertEquals(expectedQuestResponseDTO, actual);
    }

    private Quest getQuest() {
        Quest quest = new Quest();
        quest.setName("QuestTest");
        quest.setEmail(EmailConstToTest.EMAIL_DEFAULT);
        quest.setStartDate(LocalDate.now());
        quest.setEndDate(LocalDate.now().plusDays(1));
        quest.setStatus(QuestStatus.IN_PROGRESS);
        quest.setHexColor("#000000");
        quest.setTotalXp(100L);
        List<Day> days = new ArrayList<>();
        Day day = new Day();
        day.setStartTime("10:00");
        day.setEndTime("11:00");
        day.setDayOfWeekCustom(DayOfWeekCustom.FRIDAY);
        days.add(day);
        quest.setWeek(days);
        return quest;
    }
}
