package br.inatel.thisismeapi.units.services.impl;


import br.inatel.thisismeapi.exceptions.SendEmailException;
import br.inatel.thisismeapi.services.impl.MailServiceImpl;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@SpringBootTest(classes = {MailServiceImpl.class, SimpleMailMessage.class})
class MailServiceTests {


    @Autowired
    private MailServiceImpl mailService;

    @MockBean
    private JavaMailSender javaMailSender;

    @MockBean
    private SimpleMailMessage simpleMailMessage;

    @Test
    void testSendEmailWithMessageSuccess() {

        doNothing().when(simpleMailMessage).setTo(anyString());
        doNothing().when(simpleMailMessage).setSubject(anyString());
        doNothing().when(simpleMailMessage).setText(anyString());
        doNothing().when(javaMailSender).send((SimpleMailMessage) any());

        mailService.sendEmailWithMessage("email", "subject", "message");

        verify(javaMailSender).send((SimpleMailMessage) any());
    }

    @Test
    void testSendEmailWithMessageThrowException() {

        doNothing().when(simpleMailMessage).setTo(anyString());
        doNothing().when(simpleMailMessage).setSubject(anyString());
        doNothing().when(simpleMailMessage).setText(anyString());
        doThrow(new RuntimeException("error message")).when(javaMailSender).send((SimpleMailMessage) any());

        SendEmailException exception = assertThrows(SendEmailException.class, () -> {
                mailService.sendEmailWithMessage("email", "subject", "message");
        });

        assertEquals("error message",exception.getMessage());
        verify(javaMailSender).send((SimpleMailMessage) any());

    }
}
