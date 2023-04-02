package br.inatel.thisismeapi.units.services.impl;


import br.inatel.thisismeapi.entities.Character;
import br.inatel.thisismeapi.entities.User;
import br.inatel.thisismeapi.exceptions.OnCreateDataException;
import br.inatel.thisismeapi.exceptions.TokenInvalidException;
import br.inatel.thisismeapi.exceptions.UnregisteredUserException;
import br.inatel.thisismeapi.exceptions.mongo.UniqueViolationConstraintException;
import br.inatel.thisismeapi.repositories.UserRepository;
import br.inatel.thisismeapi.services.CharacterService;
import br.inatel.thisismeapi.services.MailService;
import br.inatel.thisismeapi.services.impl.UserServiceImpl;
import br.inatel.thisismeapi.units.classesToTest.EmailConstToTest;
import br.inatel.thisismeapi.units.classesToTest.PasswordConstToTest;
import br.inatel.thisismeapi.utils.JwtUtils;
import com.auth0.jwt.interfaces.DecodedJWT;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.test.context.ActiveProfiles;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@SpringBootTest(classes = {UserServiceImpl.class})
@ActiveProfiles("test")
class UserServiceTests {

    private static final long RESET_TOKEN_EXPIRATION_TIME_IN_SECONDS = 43200;

    @Value("${private.key.default}")
    private String PRIVATE_KEY_DEFAULT;

    @Autowired
    private UserServiceImpl userService;

    @MockBean
    private UserRepository userRepository;

    @MockBean
    private CharacterService characterService;

    @MockBean
    private MailService mailService;

    private BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    // TODO tentar novamente Mockar o UserUtils
    @Test
    void testSaveNewAccountSuccess() {

        // given
        String email = EmailConstToTest.EMAIL_DEFAULT;
        String password = PasswordConstToTest.PASSWORD_MIN_LENGHT_5;
        String verifyPassword = PasswordConstToTest.PASSWORD_MIN_LENGHT_5;
        String charName = "Test Name";

        User user = new User();
        user.setEmail(email);

        // when
        when(characterService.saveNewCharacter(any(Character.class))).thenReturn(new Character());
        when(userRepository.save(any(User.class))).thenReturn(user);
        String actual = userService.saveNewAccount(email, password, verifyPassword, charName);
        DecodedJWT decodedJWT = JwtUtils.decodedJWT(actual, this.PRIVATE_KEY_DEFAULT);
        String emailActual = decodedJWT.getClaim("email").asString();
        // then
        assertEquals(user.getEmail(), emailActual);
        verify(characterService).saveNewCharacter(any(Character.class));
        verify(userRepository).save(any(User.class));
    }

    @Test
    void testSaveNewAccountThrowExceptionWhenCharacterNameIsBlank() {

        String email = EmailConstToTest.EMAIL_DEFAULT;
        String password = PasswordConstToTest.PASSWORD_MIN_LENGHT_5;
        String verifyPassword = PasswordConstToTest.PASSWORD_MIN_LENGHT_5;
        String charName = "";

        User user = new User();
        user.setEmail(email);

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            userService.saveNewAccount(email, password, verifyPassword, charName);
        });

        assertEquals("Nome do personagem inválido!", exception.getMessage());
    }

    @Test
    void testSaveNewAccountThrowExceptionWhenCharacterNameIsGreaterThanFifteenCharacters() {

        String email = EmailConstToTest.EMAIL_DEFAULT;
        String password = PasswordConstToTest.PASSWORD_MIN_LENGHT_5;
        String verifyPassword = PasswordConstToTest.PASSWORD_MIN_LENGHT_5;
        String charName = "name with more 15 caracteres";

        User user = new User();
        user.setEmail(email);

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            userService.saveNewAccount(email, password, verifyPassword, charName);
        });

        assertEquals("Nome do personagem pode ter no máximo 15 caracteres!", exception.getMessage());
    }

    @Test
    void testSaveNewAccountThrowExceptionWhenTryCreateAnotherAccountWithAlreadyRegisteredEmail() {

        String email = EmailConstToTest.EMAIL_DEFAULT;
        String password = PasswordConstToTest.PASSWORD_MIN_LENGHT_5;
        String verifyPassword = PasswordConstToTest.PASSWORD_MIN_LENGHT_5;
        String charName = "Test Name";


        when(userRepository.save(any())).thenThrow(DuplicateKeyException.class);

        UniqueViolationConstraintException exception = assertThrows(UniqueViolationConstraintException.class, () -> {
            userService.saveNewAccount(email, password, verifyPassword, charName);
        });

        assertEquals("Já Existe uma conta cadastrada com esse e-mail!", exception.getLocalizedMessage());
    }

    @Test
    void testSaveNewAccountThrowExceptionWhenTryCreateUserNull() {

        String email = EmailConstToTest.EMAIL_DEFAULT;
        String password = PasswordConstToTest.PASSWORD_MIN_LENGHT_5;
        String verifyPassword = PasswordConstToTest.PASSWORD_MIN_LENGHT_5;
        String charName = "Test Name";


        when(userRepository.save(any())).thenThrow(new IllegalArgumentException("test"));

        OnCreateDataException exception = assertThrows(OnCreateDataException.class, () -> {
            userService.saveNewAccount(email, password, verifyPassword, charName);
        });

        assertEquals("test", exception.getMessage());
    }

    @Test
    void testUpdateUserSuccess() {

        String email = EmailConstToTest.EMAIL_DEFAULT;

        User user = Mockito.mock(User.class);

        when(user.getId()).thenReturn("id");
        when(userRepository.save(any(User.class))).thenReturn(user);
        User actual = userService.updateUser(user);

        assertEquals(user, actual);
        verify(userRepository).save(any(User.class));
    }

    @Test
    void testUpdateUserThrowExceptionWhenIdUserIsNull() {

        String email = EmailConstToTest.EMAIL_DEFAULT;

        User user = Mockito.mock(User.class);

        when(user.getId()).thenReturn(null);
        when(userRepository.save(any(User.class))).thenReturn(user);

        UnregisteredUserException exception = assertThrows(UnregisteredUserException.class, () -> {
            userService.updateUser(user);
        });

        assertEquals("Usuário não registrado!", exception.getMessage());;
    }

    @Test
    void testFindUserByEmailThrowExceptionWhenNotFoundUser() {

        String email = EmailConstToTest.EMAIL_DEFAULT;

        when(userRepository.findByEmail(email)).thenReturn(Optional.empty());

        UnregisteredUserException exception = assertThrows(UnregisteredUserException.class, () -> {
            userService.findUserByEmail(email);
        });

        assertEquals("Usuário com email [" + email + "] não encontrado!", exception.getMessage());
        verify(userRepository).findByEmail(email);
    }

    @Test
    void testFindUserByEmailSuccess() {

        String email = EmailConstToTest.EMAIL_DEFAULT;

        User user = new User();
        user.setEmail(email);

        when(userRepository.findByEmail(email)).thenReturn(Optional.of(user));
        User actual = userService.findUserByEmail(email);

        assertEquals(user, actual);
        verify(userRepository).findByEmail(email);
    }

    @Test
    void testFindUserByEmailThrowExeptionWhenDoNotFoundUser() {

        String email = EmailConstToTest.EMAIL_DEFAULT;
        String expectedMessage = "Usuário com email [" + email + "] não encontrado!";

        when(userRepository.findByEmail(email)).thenReturn(Optional.empty());

        UnregisteredUserException exception = assertThrows(UnregisteredUserException.class, () -> {
            userService.findUserByEmail(email);
        });


        assertEquals(expectedMessage, exception.getMessage());
        verify(userRepository).findByEmail(email);
    }

    // TODO: Descobrir como mockar uma chamada para um metodo interno
    @Test
    void testSendEmailToResetPasswordSuccess() {

        String email = EmailConstToTest.EMAIL_DEFAULT;
        User user = Mockito.spy(User.class);
        user.setEmail(email);

        when(userRepository.findByEmail(email)).thenReturn(Optional.of(user));
        doNothing().when(mailService).sendEmailWithMessage(anyString(), anyString(), anyString());
        when(userRepository.save(user)).thenReturn(user);
        when(user.getId()).thenReturn("id");

        userService.sendEmailToResetPassword(email);

        assertNotNull(user.getTokenResetPassword());
    }

    @Test
    void testGetResetTokenWithEmailAndNumberSuccess() {

        String email = EmailConstToTest.EMAIL_DEFAULT;

        User user = new User();
        user.setEmail(email);
        String tokenExpected = JwtUtils.createJwtResetTokenWith(email, 10, RESET_TOKEN_EXPIRATION_TIME_IN_SECONDS, this.PRIVATE_KEY_DEFAULT);
        user.setTokenResetPassword(tokenExpected);

        when(userRepository.findByEmail(email)).thenReturn(Optional.of(user));

        String tokenActual = userService.getResetTokenWithEmailAndNumber(email, 10);

        assertEquals(tokenExpected, tokenActual);
    }

    @Test
    void testGetResetTokenWithEmailAndNumberThrowExceptionWhenTokenResetIsNull() {

        String email = EmailConstToTest.EMAIL_DEFAULT;
        String expectedMessage = "Não foi gerado o código de verificação, por favor solicite outro código e tente novamente!";
        User user = new User();
        user.setEmail(email);
        user.setTokenResetPassword(null);
        when(userRepository.findByEmail(email)).thenReturn(Optional.of(user));

        TokenInvalidException exception = assertThrows(TokenInvalidException.class, () -> {
            userService.getResetTokenWithEmailAndNumber(email, 10);
        });

        assertEquals(expectedMessage, exception.getMessage());
    }


    @Test
    void testGetResetTokenWithEmailAndNumberThrowExceptionWhenNumberIsWrong() {

        String email = EmailConstToTest.EMAIL_DEFAULT;

        User user = new User();
        user.setEmail(email);
        String tokenExpected = JwtUtils.createJwtResetTokenWith(email, 10, RESET_TOKEN_EXPIRATION_TIME_IN_SECONDS, this.PRIVATE_KEY_DEFAULT);
        user.setTokenResetPassword(tokenExpected);

        when(userRepository.findByEmail(email)).thenReturn(Optional.of(user));

        TokenInvalidException exception = assertThrows(TokenInvalidException.class, () -> {
            userService.getResetTokenWithEmailAndNumber(email, 66);
        });

        assertEquals("Código de verificação incorreto!", exception.getMessage());
    }

    @Test
    void testGetResetTokenWithEmailAndNumberThrowExceptionWhenResetTokenJwtIsWrong() {

        String email = EmailConstToTest.EMAIL_DEFAULT;

        User user = new User();
        user.setEmail(email);
        String tokenExpected = JwtUtils.createJwtResetTokenWith(email, 10, RESET_TOKEN_EXPIRATION_TIME_IN_SECONDS, "wrongKey");
        user.setTokenResetPassword(tokenExpected);

        when(userRepository.findByEmail(email)).thenReturn(Optional.of(user));

        TokenInvalidException exception = assertThrows(TokenInvalidException.class, () -> {
            userService.getResetTokenWithEmailAndNumber(email, 10);
        });

        assertEquals("Não foi gerado o código de verificação, por favor solicite outro código e tente novamente!", exception.getMessage());
    }

    @Test
    void testResetPasswordSuccess() {

        String email = EmailConstToTest.EMAIL_DEFAULT;
        String oldPassword = PasswordConstToTest.PASSWORD_MIN_LENGHT_5;
        String newPassword = "NovaSenha";
        User user = Mockito.spy(User.class);
        user.setEmail(email);
        user.setPassword(this.passwordEncoder().encode(oldPassword));
        String token = JwtUtils.createJwtResetTokenWith(email, 10, RESET_TOKEN_EXPIRATION_TIME_IN_SECONDS, this.PRIVATE_KEY_DEFAULT);
        user.setTokenResetPassword(token);

        when(userRepository.findByEmail(email)).thenReturn(Optional.of(user));
        when(userRepository.save(user)).thenReturn(user);
        when(user.getId()).thenReturn("id");
        userService.resetPassword(newPassword, newPassword, token);

        assertTrue(passwordEncoder().matches(newPassword, user.getPassword()));
    }

    @Test
    void testResetPasswordThrowExceptionWhenJwtTokenIsInvalid() {

        String email = EmailConstToTest.EMAIL_DEFAULT;
        String resetToken = null;
        String newPassword = "NovaSenha";
        String token = JwtUtils.createJwtResetTokenWith(email, 10, RESET_TOKEN_EXPIRATION_TIME_IN_SECONDS, "wrong key");
        TokenInvalidException exception = assertThrows(TokenInvalidException.class, () -> {
            userService.resetPassword(newPassword, newPassword, token);
        });

        assertEquals("Não foi gerado o código de verificação, por favor solicite outro código e tente novamente!", exception.getMessage());
    }

    @Test
    void testResetPasswordThrowExceptionWhenJwtTokenInUserIsNull() {

        String email = EmailConstToTest.EMAIL_DEFAULT;
        String resetToken = null;
        String newPassword = "NovaSenha";
        User user = new User();
        user.setEmail(email);
        String token = JwtUtils.createJwtResetTokenWith(email, 10, RESET_TOKEN_EXPIRATION_TIME_IN_SECONDS, PRIVATE_KEY_DEFAULT);
        when(userRepository.findByEmail(email)).thenReturn(Optional.of(user));
        TokenInvalidException exception = assertThrows(TokenInvalidException.class, () -> {
            userService.resetPassword(newPassword, newPassword, token);
        });

        assertEquals("Token já utilizado ou não foi gerado, por favor crie um novo código para a alteração de senha!", exception.getMessage());
    }
}
