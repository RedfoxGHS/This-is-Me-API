package br.inatel.thisismeapi.services.impl;

import br.inatel.thisismeapi.classestotest.PasswordConst;
import br.inatel.thisismeapi.controllers.exceptions.ConstraintViolationException;
import br.inatel.thisismeapi.entities.Character;
import br.inatel.thisismeapi.entities.User;
import br.inatel.thisismeapi.repositories.CharacterRepository;
import br.inatel.thisismeapi.repositories.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.mongo.MongoAutoConfiguration;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@SpringBootTest(classes = {AdminServiceTests.class},
        webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@EnableAutoConfiguration(exclude = {MongoAutoConfiguration.class, SecurityAutoConfiguration.class})
@ContextConfiguration(classes = {AdminServiceImpl.class})
@ActiveProfiles("dev")
@ExtendWith(MockitoExtension.class)
class AdminServiceTests {

    @Autowired
    private AdminServiceImpl adminService;

    @MockBean
    private UserRepository userRepository;


    @MockBean
    private PasswordEncoder encoder;

    @MockBean
    private CharacterRepository characterRepository;

    @Test
    void testCreateNewAccountSuccess() {

        // given
        String email = "test@email.com";
        String password = PasswordConst.PASSWORD_MIN_LENGHT_5;
        String cName = "Character Name";
        User user = new User(email, password);
        Character character = new Character();
        character.setCharacterName(cName);
        user.setCharacter(character);

        // when
        when(encoder.encode(password)).thenReturn(password);
        when(userRepository.save(any(User.class))).thenReturn(user);
        when(characterRepository.save(any(Character.class))).thenReturn(character);
        User actual = adminService.createNewAccount(user, cName);

        // then
        verify(userRepository, times(1)).save(user);
        assertEquals(user.getEmail(), actual.getEmail());
        assertEquals(user.getPassword(), actual.getPassword());
        assertEquals(cName, actual.getCharacter().getCharacterName());
        assertEquals(actual, user);
    }

    @Test
    void testCreateNewAccountWithPasswordWithTheMaxNumberOfCharacters() {

        String email = "test@email.com";
        String password = PasswordConst.PASSWORD_WITH_MORE_MAX_LENGHT_31;
        User user = new User(email, "0123456789012345678901234567890");

        ConstraintViolationException thrown = assertThrows(ConstraintViolationException.class, () -> {
            adminService.createNewAccount(user, "Character Name");
        });

        assertEquals("Senha deve conter no minimo 5 e no maximo 30 digitos!", thrown.getMessage());
    }

    @Test
    void testCreateNewAccountWithPasswordWithLessThanMinNumberOfCharacters() {

        // given
        String email = "test@email.com";
        String password = PasswordConst.PASSWORD_WITH_LESS_MIN_LENGHT_4;
        User user = new User(email, password);

        // when
        ConstraintViolationException thrown = assertThrows(ConstraintViolationException.class, () -> {
            adminService.createNewAccount(user, "Character Name");
        });

        // then
        assertEquals("Senha deve conter no minimo 5 e no maximo 30 digitos!", thrown.getMessage());
    }
}
