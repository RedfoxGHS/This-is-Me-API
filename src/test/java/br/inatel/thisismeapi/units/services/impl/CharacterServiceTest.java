package br.inatel.thisismeapi.units.services.impl;


import br.inatel.thisismeapi.entities.Character;
import br.inatel.thisismeapi.exceptions.UnregisteredUserException;
import br.inatel.thisismeapi.exceptions.mongo.UniqueViolationConstraintException;
import br.inatel.thisismeapi.repositories.CharacterRepository;
import br.inatel.thisismeapi.services.impl.CharacterServiceImpl;
import br.inatel.thisismeapi.units.classesToTest.EmailConstToTest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.test.context.ActiveProfiles;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@SpringBootTest(classes = {CharacterServiceImpl.class})
@ActiveProfiles("test")
class CharacterServiceTest {

    @Autowired
    private CharacterServiceImpl characterService;

    @MockBean
    private CharacterRepository characterRepository;


    @Test
    void testFindCharacterByEmailSuccess() {

        String email = EmailConstToTest.EMAIL_DEFAULT;
        Character character = new Character();
        character.setEmail(email);

        when(characterRepository.findCharacterByEmail(email)).thenReturn(Optional.of(character));
        Character actualChar = this.characterService.findCharacterByEmail(email);

        assertEquals(character, actualChar);
        assertEquals(character.getEmail(), actualChar.getEmail());
    }

    @Test
    void testFindCharacterByEmailThrowExceptionWhenNotFoundUser() {

        String email = EmailConstToTest.EMAIL_DEFAULT;
        String expectedMessage = "Personagem com email [" + email + "] não encontrado!";

        when(characterRepository.findCharacterByEmail(email)).thenReturn(Optional.empty());
        UnregisteredUserException exception = assertThrows(UnregisteredUserException.class, () -> {
            this.characterService.findCharacterByEmail(email);
        });

        assertEquals(expectedMessage, exception.getMessage());
    }

    @Test
    void testSaveNewCharacterSuccess() {

        String email = EmailConstToTest.EMAIL_DEFAULT;
        Character character = new Character();
        character.setEmail(email);

        when(characterRepository.save(character)).thenReturn(character);
        Character actualChar = this.characterService.saveNewCharacter(character);

        assertEquals(character, actualChar);
        assertEquals(character.getEmail(), actualChar.getEmail());
    }

    @Test
    void testSaveNewCharacterThrowExceptionWhenAlreadyExistCharacterInAccount() {

        String email = EmailConstToTest.EMAIL_DEFAULT;
        Character character = new Character();
        character.setEmail(email);
        String expectedMessage = "Já Existe uma conta cadastrada com esse e-mail!";

        when(characterRepository.save(character)).thenThrow(DuplicateKeyException.class);
        UniqueViolationConstraintException exception = assertThrows(UniqueViolationConstraintException.class, () -> {
            this.characterService.saveNewCharacter(character);
        });

        assertEquals(expectedMessage, exception.getMessage());
    }

    @Test
    void testUpdateCharacterSuccess() {

        String email = EmailConstToTest.EMAIL_DEFAULT;
        Character character = new Character();
        character.setEmail(email);
        character.setId("123456");
        when(characterRepository.save(character)).thenReturn(character);
        Character actualChar = this.characterService.updateCharacter(character);

        assertEquals(character, actualChar);
        assertEquals(character.getEmail(), actualChar.getEmail());
    }

    @Test
    void testSaveUpdateCharacterThrowExceptionWhenCharacterDoNotHaveIdToUpdate() {

        String email = EmailConstToTest.EMAIL_DEFAULT;
        Character character = new Character();
        character.setEmail(email);
        String expectedMessage = "Usuário não tem um personagem criado!";

        UnregisteredUserException exception = assertThrows(UnregisteredUserException.class, () -> {
            this.characterService.updateCharacter(character);
        });

        assertEquals(expectedMessage, exception.getMessage());
    }

    @Test
    void testSetClothesSuccess() {

        String email = EmailConstToTest.EMAIL_DEFAULT;
        Character character = new Character();
        character.setEmail(email);
        character.setNumberClothes(1L);

        when(characterRepository.findCharacterByEmail(email)).thenReturn(Optional.of(character));
        when(characterRepository.save(character)).thenReturn(character);
        Character actualChar = this.characterService.setClothes(email, character.getNumberClothes());

        assertEquals(character, actualChar);
        assertEquals(character.getEmail(), actualChar.getEmail());
        assertEquals(1L, actualChar.getNumberClothes());
    }

    @Test
    void testDeleteCharacterByIdSuccess() {

        doNothing().when(characterRepository).deleteById(any());

        characterService.deleteCharacterById("123456");

        verify(characterRepository).deleteById(any());
    }

    @Test
    void testDeleteCharacterByEmailSuccess() {

        doNothing().when(characterRepository).deleteCharacterByEmail(any());

        characterService.deleteCharacterByEmail(EmailConstToTest.EMAIL_DEFAULT);

        verify(characterRepository).deleteCharacterByEmail(any());
    }
}
