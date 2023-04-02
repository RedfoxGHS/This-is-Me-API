package br.inatel.thisismeapi.units.utils;

import br.inatel.thisismeapi.exceptions.ConstraintViolationException;
import br.inatel.thisismeapi.units.classesToTest.EmailConstToTest;
import br.inatel.thisismeapi.units.classesToTest.PasswordConstToTest;
import br.inatel.thisismeapi.utils.UserUtils;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;


@SpringBootTest(classes = UserUtils.class)
class UserUtilsTests {


    @Test
    void testVerifyPasswordSuccess() {

        String password = "123456";
        String passwordVerify = "123456";

        Boolean result = UserUtils.verifyPassword(password, passwordVerify);

        assertTrue(result);
    }

    @Test
    void testVerifyPasswordThrowExceptionWhenPasswordIsNull() {

        String password = null;
        String passwordVerify = "123456";

        ConstraintViolationException exception = assertThrows(ConstraintViolationException.class, () -> {
            UserUtils.verifyPassword(password, passwordVerify);
        });

        assertEquals("Senha não pode ser nula!", exception.getMessage());
    }

    @Test
    void testVerifyPasswordThrowExceptionWhenVerifyPasswordIsNull() {

        String password = "123456";
        String passwordVerify = null;

        ConstraintViolationException exception = assertThrows(ConstraintViolationException.class, () -> {
            UserUtils.verifyPassword(password, passwordVerify);
        });

        assertEquals("Senha não pode ser nula!", exception.getMessage());
    }

    @Test
    void testVerifyPasswordThrowExceptionWhenPasswordIsBlank() {

        String password = "";
        String passwordVerify = "123456";

        ConstraintViolationException exception = assertThrows(ConstraintViolationException.class, () -> {
            UserUtils.verifyPassword(password, passwordVerify);
        });

        assertEquals("Senha não pode ser deixada em branco!", exception.getMessage());
    }

    @Test
    void testVerifyPasswordThrowExceptionWhenVerifyPasswordIsBlank() {

        String password = "123456";
        String passwordVerify = "";

        ConstraintViolationException exception = assertThrows(ConstraintViolationException.class, () -> {
            UserUtils.verifyPassword(password, passwordVerify);
        });

        assertEquals("Senha não pode ser deixada em branco!", exception.getMessage());
    }

    @Test
    void testVerifyPasswordThrowExceptionWhenPasswordsIsNotEquals() {

        String password = "123456";
        String passwordVerify = "654321";

        ConstraintViolationException exception = assertThrows(ConstraintViolationException.class, () -> {
            UserUtils.verifyPassword(password, passwordVerify);
        });

        assertEquals("As senhas não coincidem!", exception.getMessage());
    }

    @Test
    void testVerifyDecryptedPasswordLengthSuccess() {

        String password = "123456";

        Boolean result = UserUtils.verifyDecryptedPasswordLength(password);

        assertTrue(result);
    }

    @Test
    void testVerifyDecryptedPasswordLengthThrowExceptionWhenPasswordIsLenghtIsLessThanFive() {

        String password = PasswordConstToTest.PASSWORD_WITH_LESS_MIN_LENGHT_4;

        ConstraintViolationException exception = assertThrows(ConstraintViolationException.class, () -> {
            UserUtils.verifyDecryptedPasswordLength(password);
        });

        assertEquals("Senha deve conter no mínimo 5 e no máximo 30 dígitos!", exception.getMessage());
    }

    @Test
    void testVerifyDecryptedPasswordLengthThrowExceptionWhenPasswordIsGreaterThanThirty() {

        String password = PasswordConstToTest.PASSWORD_WITH_MORE_MAX_LENGHT_31;

        ConstraintViolationException exception = assertThrows(ConstraintViolationException.class, () -> {
            UserUtils.verifyDecryptedPasswordLength(password);
        });

        assertEquals("Senha deve conter no mínimo 5 e no máximo 30 dígitos!", exception.getMessage());
    }

    @Test
    void testVerifyEmailSucess() {

        String email = EmailConstToTest.EMAIL_DEFAULT;

        Boolean result = UserUtils.verifyEmail(email);

        assertTrue(result);
    }

    @Test
    void testVerifyEmailThrowExceptionWhenEmailIsNull() {

        String email = null;

        ConstraintViolationException exception = assertThrows(ConstraintViolationException.class, () -> {
            UserUtils.verifyEmail(email);
        });

        assertEquals("Email não pode ser nulo!", exception.getMessage());
    }

    @Test
    void testVerifyEmailThrowExceptionWhenEmailIsBlank() {

        String email = "";

        ConstraintViolationException exception = assertThrows(ConstraintViolationException.class, () -> {
            UserUtils.verifyEmail(email);
        });

        assertEquals("Email não pode ser deixado em branco!", exception.getMessage());
    }

    @Test
    void testVerifyEmailThrowExceptionWhenEmailLengthIsGreaterThanOneHundredAndTwenty() {

        String email = EmailConstToTest.EMAIL_WITH_MORE_MAX_LENGHT;

        ConstraintViolationException exception = assertThrows(ConstraintViolationException.class, () -> {
            UserUtils.verifyEmail(email);
        });

        assertEquals("Email não pode conter mais de 120 dígitos!", exception.getMessage());
    }

    @Test
    void testVerifyEmailThrowExceptionWhenEmailFormatInvalid() {

        String email = "email";

        ConstraintViolationException exception = assertThrows(ConstraintViolationException.class, () -> {
            UserUtils.verifyEmail(email);
        });

        assertEquals("Email inválido!", exception.getMessage());
    }
}
