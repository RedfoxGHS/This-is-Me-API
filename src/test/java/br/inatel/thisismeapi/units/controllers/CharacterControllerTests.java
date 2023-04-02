package br.inatel.thisismeapi.units.controllers;


import br.inatel.thisismeapi.controllers.CharacterController;
import br.inatel.thisismeapi.controllers.dtos.responses.CharacterInfoResponseDTO;
import br.inatel.thisismeapi.entities.Character;
import br.inatel.thisismeapi.services.CharacterService;
import br.inatel.thisismeapi.units.classesToTest.EmailConstToTest;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@SpringBootTest(classes = {CharacterController.class})
class CharacterControllerTests {

    private String email = "test@test.com";
    private String charName = "Test Name";
    private Long clotheNumber = 0l;

    @Autowired
    private CharacterController characterController;

    @MockBean
    private CharacterService characterService;

    @Mock
    private Authentication authentication;

    @Test
    void testGetCharacterSuccess() {

        Character character = this.getCharacter();

        when(authentication.getName()).thenReturn(character.getEmail());
        when(characterService.findCharacterByEmail(character.getEmail())).thenReturn(character);
        ResponseEntity<CharacterInfoResponseDTO> response = characterController.getCharacter(authentication);
        CharacterInfoResponseDTO responseDTO = response.getBody();

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(character.getCharacterName(), responseDTO.getCharacterName());
        assertEquals(character.getLevel(), responseDTO.getLevel());
        assertEquals(character.getNumberClothes(), responseDTO.getClothes());
        assertEquals(character.getXp(), responseDTO.getXp());
        verify(characterService).findCharacterByEmail(character.getEmail());
    }

    @Test
    void testSetClothesSuccess() {

        Character character = this.getCharacter();
        Long newClothes = 66L;
        character.setNumberClothes(newClothes);

        when(authentication.getName()).thenReturn(character.getEmail());
        when(characterService.setClothes(character.getEmail(), newClothes)).thenReturn(character);
        ResponseEntity<CharacterInfoResponseDTO> response = characterController.setClothes(newClothes, authentication);
        CharacterInfoResponseDTO responseDTO = response.getBody();

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(newClothes, responseDTO.getClothes());
    }

    @Test
    void testGetCharacterAllInfosSuccess() {

        Character character = this.getCharacter();

        when(authentication.getName()).thenReturn(EmailConstToTest.EMAIL_DEFAULT);
        when(characterService.findCharacterByEmail(character.getEmail())).thenReturn(character);

        ResponseEntity<Character> charActual = this.characterController.getCharacterAllInfos(authentication);

        assertEquals(HttpStatus.OK, charActual.getStatusCode());
        assertEquals(character, charActual.getBody());
        verify(characterService).findCharacterByEmail(character.getEmail());
    }

    private Character getCharacter() {

        Character character = new Character();
        character.setEmail(this.email);
        character.setNumberClothes(this.clotheNumber);
        character.setCharacterName(this.charName);
        return character;
    }
}
