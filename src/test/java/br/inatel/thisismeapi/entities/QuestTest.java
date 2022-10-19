package br.inatel.thisismeapi.entities;

import br.inatel.thisismeapi.enums.QuestStatus;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.boot.autoconfigure.web.servlet.ServletWebServerFactoryAutoConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;

@SpringBootTest(classes = {ServletWebServerFactoryAutoConfiguration.class},
        webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@EnableAutoConfiguration(exclude = {SecurityAutoConfiguration.class})
@ActiveProfiles("dev")
class QuestTest {

    @Test
    void testCreateNewInstance(){
        Quest actual = new Quest();
        Assertions.assertEquals(Quest.class, actual.getClass());
    }

    @Test
    void testQuestSetAttributes(){
        Quest actual = new Quest();
        actual.setName("quest");
        actual.setEmail("test@test.com");
        actual.setDesc("desc");
        actual.setStatus(QuestStatus.IN_PROGRESS);
        actual.setSkill("Skill");
        actual.setHexColor("#000000");
        Assertions.assertEquals("quest", actual.getName());
        Assertions.assertEquals("test@test.com", actual.getEmail());
        Assertions.assertEquals("desc", actual.getDesc());
        Assertions.assertEquals(QuestStatus.IN_PROGRESS, actual.getStatus());
        Assertions.assertEquals("Skill", actual.getSkill());
        Assertions.assertEquals("#000000", actual.getHexColor());
    }
}
