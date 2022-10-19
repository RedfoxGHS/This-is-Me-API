package br.inatel.thisismeapi.services.impl;


import br.inatel.thisismeapi.classestotest.JwtUtilToTest;
import br.inatel.thisismeapi.entities.User;
import br.inatel.thisismeapi.repositories.UserRepository;
import br.inatel.thisismeapi.services.exceptions.TokenExpiredException;
import br.inatel.thisismeapi.services.exceptions.TokenInvalidException;
import br.inatel.thisismeapi.services.exceptions.UnregisteredUserException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.mongo.MongoAutoConfiguration;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@SpringBootTest(classes = {MailServiceTests.class},
        webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@EnableAutoConfiguration(exclude = {MongoAutoConfiguration.class, SecurityAutoConfiguration.class})
@ContextConfiguration(classes = {MailServiceImpl.class})
@ActiveProfiles("dev")
@ExtendWith(MockitoExtension.class)
class MailServiceTests {

    @Autowired
    private MailServiceImpl mailService;

    @MockBean
    private JavaMailSender javaMailSender;

    @MockBean
    private UserRepository userRepository;

    @Autowired
    @Value("${private.key.mail}")
    private String PRIVATE_KEY_MAIL;

    @Test
    void testSendEmailForgotPasswordSuccess(){

        String email = "test123@test.com";

        User user = new User(email, "12345");
        when(userRepository.findByEmail(email)).thenReturn(Optional.of(user));
        when(userRepository.save(user)).thenReturn(user);
        doNothing().when(javaMailSender).send(any(SimpleMailMessage.class));
        mailService.sendEmailForgotPassword(email);

        verify(userRepository).findByEmail(email);
        verify(userRepository).save(user);
        verify(javaMailSender).send(any(SimpleMailMessage.class));
    }

    @Test
    void testSendEmailForgotPasswordWithNonExistentUser(){

        String email = "test@test.com";

        when(userRepository.findByEmail(any(String.class))).thenReturn(Optional.empty());

        UnregisteredUserException thrown = assertThrows(UnregisteredUserException.class, () -> {
            mailService.sendEmailForgotPassword(email);
        });

        assertEquals("Usúario com email " + email + " não encontrado!", thrown.getMessage());
    }

    @Test
    void testVerifyNumberPasswordWithNonExistentUser(){

        String email = "test@test.com";

        when(userRepository.findByEmail(any(String.class))).thenReturn(Optional.empty());

        UnregisteredUserException thrown = assertThrows(UnregisteredUserException.class, () -> {
            mailService.verifyNumberPassword(email, "123456");
        });

        assertEquals("Usúario com email " + email + " não encontrado!", thrown.getMessage());
    }

    @Test
    void testVerifyNumberPasswordWithInvalidCode(){

        String email = "test123@test.com";
        User user = new User(email, "12345");
        String code = "123123";
        String jwt = JwtUtilToTest.generateTokenResetPassword(email, code, PRIVATE_KEY_MAIL, 240);
        user.setTokenResetPassword(jwt);

        when(userRepository.findByEmail(email)).thenReturn(Optional.of(user));

        TokenInvalidException thrown = assertThrows(TokenInvalidException.class, () -> {
            mailService.verifyNumberPassword(email, "121212");
        });

        assertEquals("Código de verificação incorreto!", thrown.getMessage());
        verify(userRepository).findByEmail(email);
    }

    @Test
    void testVerifyNumberPasswordWithExpiredToken(){

        String email = "test123@test.com";
        User user = new User(email, "12345");
        String code = "123123";
        String jwt = JwtUtilToTest.generateTokenResetPassword(email, code, PRIVATE_KEY_MAIL, -240);
        user.setTokenResetPassword(jwt);

        when(userRepository.findByEmail(email)).thenReturn(Optional.of(user));

        TokenInvalidException thrown = assertThrows(TokenInvalidException.class, () -> {
            mailService.verifyNumberPassword(email, code);
        });

        assertTrue(thrown.getMessage().contains("The Token has expired on"));
        verify(userRepository).findByEmail(email);
    }

    @Test
    void testVerifyNumberPasswordSuccess(){

        String email = "test123@test.com";
        User user = new User(email, "12345");
        String code = "123123";
        String jwt = JwtUtilToTest.generateTokenResetPassword(email, code, PRIVATE_KEY_MAIL, 240);
        user.setTokenResetPassword(jwt);

        when(userRepository.findByEmail(email)).thenReturn(Optional.of(user));
        String jwtResponse = mailService.verifyNumberPassword(email, code);

        assertEquals(jwt, jwtResponse);
        verify(userRepository).findByEmail(email);
    }
}
