package br.inatel.thisismeapi.units.controllers;


import br.inatel.thisismeapi.controllers.SubQuestController;
import br.inatel.thisismeapi.controllers.dtos.responses.CardResponseDTO;
import br.inatel.thisismeapi.entities.Quest;
import br.inatel.thisismeapi.entities.SubQuest;
import br.inatel.thisismeapi.enums.DayOfWeekCustom;
import br.inatel.thisismeapi.services.SubQuestsService;
import br.inatel.thisismeapi.units.classesToTest.EmailConstToTest;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.core.Authentication;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@SpringBootTest(classes = {SubQuestController.class})
class SubQuestControllerTests {

    @Autowired
    private SubQuestController subQuestController;

    @MockBean
    private SubQuestsService subQuestsService;

    @Mock
    private Authentication authentication;

    @Test
    void testGetAllSubQuestCurrentDayAsCardsSuccess() {

        SubQuest subQuest = getInstanceOfSubQuest();
        CardResponseDTO card = new CardResponseDTO(subQuest);

        List<CardResponseDTO> expectedList = new ArrayList<>();
        List<SubQuest> subQuestList = new ArrayList<>();
        expectedList.add(card);
        expectedList.add(card);
        subQuestList.add(subQuest);
        subQuestList.add(subQuest);


        when(authentication.getName()).thenReturn(EmailConstToTest.EMAIL_DEFAULT);
        when(subQuestsService.findAllSubQuestsToday(anyString())).thenReturn(subQuestList);

        List<CardResponseDTO> actualList = subQuestController.getAllSubQuestCurrentDayAsCards(authentication);

        assertEquals(expectedList.get(0).getName(), actualList.get(0).getName());
        assertEquals(expectedList.size(), actualList.size());
    }

    @Test
    void testGetAllSubQuestCurrentWeekAsCardsSuccess() {

        SubQuest subQuest = getInstanceOfSubQuest();
        CardResponseDTO card = new CardResponseDTO(subQuest);

        List<CardResponseDTO> expectedList = new ArrayList<>();
        List<SubQuest> subQuestList = new ArrayList<>();
        expectedList.add(card);
        expectedList.add(card);
        subQuestList.add(subQuest);
        subQuestList.add(subQuest);


        when(authentication.getName()).thenReturn(EmailConstToTest.EMAIL_DEFAULT);
        when(subQuestsService.findAllSubQuestsWeekly(anyString())).thenReturn(subQuestList);

        List<CardResponseDTO> actualList = subQuestController.getAllSubQuestCurrentWeekAsCards(authentication);

        assertEquals(expectedList.get(0).getName(), actualList.get(0).getName());
        assertEquals(expectedList.size(), actualList.size());
    }

    @Test
    void testGetAllSubQuestNextWeekAsCardsSuccess() {

        SubQuest subQuest = getInstanceOfSubQuest();
        CardResponseDTO card = new CardResponseDTO(subQuest);

        List<CardResponseDTO> expectedList = new ArrayList<>();
        List<SubQuest> subQuestList = new ArrayList<>();
        expectedList.add(card);
        expectedList.add(card);
        subQuestList.add(subQuest);
        subQuestList.add(subQuest);


        when(authentication.getName()).thenReturn(EmailConstToTest.EMAIL_DEFAULT);
        when(subQuestsService.findAllSubQuestsFromNextWeek(anyString())).thenReturn(subQuestList);

        List<CardResponseDTO> actualList = subQuestController.getAllSubQuestNextWeekAsCards(authentication);

        assertEquals(expectedList.get(0).getName(), actualList.get(0).getName());
        assertEquals(expectedList.size(), actualList.size());
    }

    @Test
    void testGetAllSubQuestLateAsCardsSuccess() {

        SubQuest subQuest = getInstanceOfSubQuest();
        CardResponseDTO card = new CardResponseDTO(subQuest);

        List<CardResponseDTO> expectedList = new ArrayList<>();
        List<SubQuest> subQuestList = new ArrayList<>();
        expectedList.add(card);
        expectedList.add(card);
        subQuestList.add(subQuest);
        subQuestList.add(subQuest);


        when(authentication.getName()).thenReturn(EmailConstToTest.EMAIL_DEFAULT);
        when(subQuestsService.findAllSubQuestsLate(anyString())).thenReturn(subQuestList);

        List<CardResponseDTO> actualList = subQuestController.getAllSubQuestLateAsCards(authentication);

        assertEquals(expectedList.get(0).getName(), actualList.get(0).getName());
        assertEquals(expectedList.size(), actualList.size());
    }

    @Test
    void testDeleteSubQuestByIdSuccess() {

        when(authentication.getName()).thenReturn(EmailConstToTest.EMAIL_DEFAULT);
        doNothing().when(subQuestsService).deleteSubQuestById(anyString());

        subQuestController.deleteSubQuestById("123456", authentication);

        verify(subQuestsService).deleteSubQuestById(anyString());
    }

    @Test
    void testCheckAndUncheckSubQuestSuccess() {

        when(authentication.getName()).thenReturn(EmailConstToTest.EMAIL_DEFAULT);
        when(subQuestsService.checkAndUncheckSubQuest(anyString(), anyString())).thenReturn(new SubQuest());

        subQuestController.checkAndUncheckSubQuest("123456", authentication);

        verify(subQuestsService).checkAndUncheckSubQuest("123456", EmailConstToTest.EMAIL_DEFAULT);
    }

    private SubQuest getInstanceOfSubQuest() {

        SubQuest subQuest = new SubQuest();
        subQuest.setEmail(EmailConstToTest.EMAIL_DEFAULT);
        Quest quest = new Quest();
        quest.setName("Quest");
        subQuest.setQuest(new Quest());
        subQuest.setCheck(false);
        subQuest.setXp(10L);
        subQuest.setDayOfWeekEnum(DayOfWeekCustom.FRIDAY);
        subQuest.setDurationInMin(10L);
        subQuest.setStart(LocalDateTime.now());
        subQuest.setEnd(LocalDateTime.now());

        return subQuest;
    }
}
