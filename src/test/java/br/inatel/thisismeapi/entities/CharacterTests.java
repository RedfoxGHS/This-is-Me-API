package br.inatel.thisismeapi.entities;

import org.junit.jupiter.api.Test;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.boot.autoconfigure.web.servlet.ServletWebServerFactoryAutoConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

@SpringBootTest(classes = {ServletWebServerFactoryAutoConfiguration.class},
        webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@EnableAutoConfiguration(exclude = {SecurityAutoConfiguration.class})
@ActiveProfiles("dev")
class CharacterTests {

    @Test
    void testCreateANewInstance() {
        Character character = new Character();

        assertNull(character.getId());
        assertNull(character.getCharacterName());
        assertEquals(0L, character.getXp());
        assertEquals(0L, character.getLevel());
    }

    @Test
    void testUpLevel() {
        Character character = new Character();
        Character character2 = new Character();

        character.upLevel();
        character2.upLevel();
        character2.upLevel();

        assertEquals(1L, character.getLevel());
        assertEquals(2L, character2.getLevel());
    }

    @Test
    void testAddXp() {
        Character character = new Character();
        Character character2 = new Character();

        character.addXp(100L);
        character2.addXp(100L);
        character2.addXp(150L);

        assertEquals(100L, character.getXp());
        assertEquals(250L, character2.getXp());
    }
}
