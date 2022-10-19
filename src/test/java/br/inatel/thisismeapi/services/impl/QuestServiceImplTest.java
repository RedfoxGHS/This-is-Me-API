package br.inatel.thisismeapi.services.impl;

import br.inatel.thisismeapi.classestotest.PasswordConst;
import br.inatel.thisismeapi.entities.Character;
import br.inatel.thisismeapi.entities.Quest;
import br.inatel.thisismeapi.entities.User;
import br.inatel.thisismeapi.repositories.CharacterRepository;
import br.inatel.thisismeapi.repositories.QuestRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.mongo.MongoAutoConfiguration;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@SpringBootTest(classes = {QuestServiceImplTest.class},
        webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@EnableAutoConfiguration(exclude = {MongoAutoConfiguration.class, SecurityAutoConfiguration.class})
@ContextConfiguration(classes = {QuestServiceImpl.class})
@ActiveProfiles("dev")
@ExtendWith(MockitoExtension.class)
class QuestServiceImplTest {

    @Autowired
    private QuestServiceImpl questService;

    @MockBean
    private UserServiceImpl userService;

    @MockBean
    private QuestRepository questRepository;

    @MockBean
    private CharacterRepository characterRepository;

    @Test
    void testCreateNewAccountSuccess() {

        // given
        String email = "test@email.com";
        String cName = "Character Name";
        Character character = new Character();
        Quest quest = new Quest();
        quest.setEmail(email);
        List<Quest> expected = new ArrayList<>();
        expected.add(quest);
        character.setCharacterName(cName);

        // when
        when(userService.findCharacterByEmail(email)).thenReturn(character);
        when(questRepository.save(quest)).thenReturn(quest);
        when(characterRepository.save(character)).thenReturn(any());
        Quest actual = questService.createNewQuest(quest, email);

        // then
        assertEquals(quest.getEmail(), actual.getEmail());
    }

    @Test
    void testGetQuestTodaySuccess() {

        // given
        String email = "test@email.com";
        List<Quest> expected = new ArrayList<>();
        Quest quest1 = new Quest();
        quest1.setEmail(email);
        Quest quest2 = new Quest();
        quest2.setEmail(email);
        expected.add(quest1);
        expected.add(quest2);

        // when

        when(questRepository.findAllQuestsOfTheDay(any(), any(), any())).thenReturn(expected);

        List<Quest> actual = questService.getQuestToday(email);

        // then
        assertEquals(2, actual.size());
        assertEquals(email, actual.get(0).getEmail());
    }

    @Test
    void testGetQuestWeekSuccess() {

        // given
        String email = "test@email.com";
        List<Quest> expected = new ArrayList<>();
        Quest quest1 = new Quest();
        quest1.setEmail(email);
        Quest quest2 = new Quest();
        quest2.setEmail(email);
        expected.add(quest1);
        expected.add(quest2);

        // when
        when(questRepository.findAllQuestsWeek(any(), any())).thenReturn(expected);

        List<Quest> actual = questService.getQuestWeek(email);

        // then
        assertEquals(2, actual.size());
        assertEquals(email, actual.get(0).getEmail());
    }
}
