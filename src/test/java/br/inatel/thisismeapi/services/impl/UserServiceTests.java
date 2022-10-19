package br.inatel.thisismeapi.services.impl;

import br.inatel.thisismeapi.classestotest.JwtUtilToTest;
import br.inatel.thisismeapi.classestotest.PasswordConst;
import br.inatel.thisismeapi.controllers.exceptions.ConstraintViolationException;
import br.inatel.thisismeapi.entities.Character;
import br.inatel.thisismeapi.entities.User;
import br.inatel.thisismeapi.repositories.CharacterRepository;
import br.inatel.thisismeapi.repositories.UserRepository;
import br.inatel.thisismeapi.services.exceptions.TokenInvalidException;
import br.inatel.thisismeapi.services.exceptions.UnregisteredUserException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.mongo.MongoAutoConfiguration;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@SpringBootTest(classes = {UserServiceTests.class},
        webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@EnableAutoConfiguration(exclude = {MongoAutoConfiguration.class, SecurityAutoConfiguration.class})
@ContextConfiguration(classes = {UserServiceImpl.class})
@ActiveProfiles("dev")
@ExtendWith(MockitoExtension.class)
class UserServiceTests {

    @Autowired
    private UserServiceImpl userService;

    @MockBean
    private UserRepository userRepository;

    @MockBean
    private CharacterRepository characterRepository;

    @Autowired
    @Value("${private.key.mail}")
    private String PRIVATE_KEY_MAIL;


    private BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

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
        when(userRepository.save(any(User.class))).thenReturn(user);
        when(characterRepository.save(any(Character.class))).thenReturn(character);
        User actual = userService.createNewAccount(user, character);

        // then
        verify(userRepository, times(1)).save(user);
        assertEquals(user.getEmail(), actual.getEmail());
        assertEquals(user.getPassword(), actual.getPassword());
        assertTrue(passwordEncoder().matches(PasswordConst.PASSWORD_MIN_LENGHT_5, actual.getPassword()));
        assertEquals(cName, actual.getCharacter().getCharacterName());
        assertEquals("indefinido", actual.getCharacter().getSex());
        assertEquals(actual, user);
    }

    @Test
    void testCreateNewAccountWithPasswordWithTheMaxNumberOfCharacters() {

        String email = "test@email.com";
        String password = PasswordConst.PASSWORD_WITH_MORE_MAX_LENGHT_31;
        User user = new User(email, "0123456789012345678901234567890");
        Character character = new Character("Character Name");

        ConstraintViolationException thrown = assertThrows(ConstraintViolationException.class, () -> {
            userService.createNewAccount(user, character);
        });

        assertEquals("Senha deve conter no minimo 5 e no maximo 30 digitos!", thrown.getMessage());
    }

    @Test
    void testCreateNewAccountWithPasswordWithLessThanMinNumberOfCharacters() {

        // given
        String email = "test@email.com";
        String password = PasswordConst.PASSWORD_WITH_LESS_MIN_LENGHT_4;
        User user = new User(email, password);
        Character character = new Character("Character Name");

        // when
        ConstraintViolationException thrown = assertThrows(ConstraintViolationException.class, () -> {
            userService.createNewAccount(user, character);
        });

        // then
        assertEquals("Senha deve conter no minimo 5 e no maximo 30 digitos!", thrown.getMessage());
    }

    @Test
    void testFindCharacterByEmailSuccess() {

        // given
        String email = "test@email.com";
        String password = PasswordConst.PASSWORD_MIN_LENGHT_5;
        String cName = "Character Name";
        User user = new User(email, password);
        Character character = new Character();
        character.setCharacterName(cName);
        user.setCharacter(character);

        // when
        when(userRepository.findByEmail(email)).thenReturn(Optional.of(user));
        Character actual = userService.findCharacterByEmail(email);

        // then

        assertEquals(cName, actual.getCharacterName());
        assertEquals(character, actual);
        verify(userRepository, times(1)).findByEmail(email);
    }

    @Test
    void testFindCharacterByEmailWhenNotFoundAccount() {

        String email = "test@test.com";


        when(userRepository.findByEmail(email)).thenReturn(Optional.empty());

        // when
        UnregisteredUserException thrown = assertThrows(UnregisteredUserException.class, () -> {
            userService.findCharacterByEmail(email);
        });

        // then
        assertEquals("Nenhuma conta cadastrada com esse email!", thrown.getMessage());
        verify(userRepository, times(1)).findByEmail(email);
    }

    @Test
    void testResetPasswordWithLessCharactersPassword() {

        String pass = "123";
        String jwt = "jwt";


        ConstraintViolationException thrown = assertThrows(ConstraintViolationException.class, () -> {
            userService.resetPassword(pass, jwt);
        });

        assertEquals("Senha deve conter no minimo 5 e no maximo 30 digitos!", thrown.getMessage());
    }

    @Test
    void testResetPasswordWithMoreCharactersPassword() {

        String pass = "0123456789012345678901234567890123456789";
        String jwt = "jwt";

        ConstraintViolationException thrown = assertThrows(ConstraintViolationException.class, () -> {
            userService.resetPassword(pass, jwt);
        });

        assertEquals("Senha deve conter no minimo 5 e no maximo 30 digitos!", thrown.getMessage());
    }

    @Test
    void testResetPasswordWithInvalidToken() {

        String newPass = "123456";
        String jwt = JwtUtilToTest.generateTokenResetPassword("test@test.com", "12345", "invalid key", 120);

        TokenInvalidException thrown = assertThrows(TokenInvalidException.class, () -> {
            userService.resetPassword(newPass, jwt);
        });

        assertEquals("The Token's Signature resulted invalid when verified using the Algorithm: HmacSHA256", thrown.getMessage());
    }

    @Test
    void testResetPasswordWithExpiredToken() {

        String newPass = "123456";
        String jwt = JwtUtilToTest.generateTokenResetPassword("test@test.com", "123456", PRIVATE_KEY_MAIL, -120);

        TokenInvalidException thrown = assertThrows(TokenInvalidException.class, () -> {
            userService.resetPassword(newPass, jwt);
        });

        assertTrue(thrown.getMessage().contains("The Token has expired on"));
    }

    @Test
    void testResetPasswordWithNonExistentUser() {

        String newPass = "123456";
        String email = "test@test.com";
        String jwt = JwtUtilToTest.generateTokenResetPassword(email, "123456", PRIVATE_KEY_MAIL, 120);

        when(userRepository.findByEmail(email)).thenReturn(Optional.empty());

        UnregisteredUserException thrown = assertThrows(UnregisteredUserException.class, () -> {
            userService.resetPassword(newPass, jwt);
        });

        assertEquals("Usuario não encontrado!", thrown.getMessage());
    }

    @Test
    void testResetPasswordWithTokenAlreadyUsed() {

        String newPass = "123456";
        String email = "test@test.com";
        User user = new User(email, "12345");
        user.setTokenResetPassword(null);

        String jwt = JwtUtilToTest.generateTokenResetPassword(email, "123456", PRIVATE_KEY_MAIL, 120);

        when(userRepository.findByEmail(email)).thenReturn(Optional.of(user));

        TokenInvalidException thrown = assertThrows(TokenInvalidException.class, () -> {
            userService.resetPassword(newPass, jwt);
        });

        assertEquals("Token já utilizado, por favor gere um novo codigo para a alteração de senha.", thrown.getMessage());
    }

    @Test
    void testResetPasswordSuccess() {

        String newPass = "123456";
        String oldPass = "654321";
        String email = "test@test.com";
        User user = new User(email, oldPass);
        String jwt = JwtUtilToTest.generateTokenResetPassword(email, "123123", PRIVATE_KEY_MAIL, 120);
        user.setTokenResetPassword(jwt);

        when(userRepository.findByEmail(email)).thenReturn(Optional.of(user));
        when(userRepository.save(user)).thenReturn(user);

        userService.resetPassword(newPass, jwt);

        assertNull(user.getTokenResetPassword());
        assertTrue(passwordEncoder().matches(newPass, user.getPassword()));
    }
}
