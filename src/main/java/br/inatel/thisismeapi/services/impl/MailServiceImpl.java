package br.inatel.thisismeapi.services.impl;

import br.inatel.thisismeapi.exceptions.SendEmailException;
import br.inatel.thisismeapi.services.MailService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class MailServiceImpl implements MailService {

    private static final Logger LOGGER = LoggerFactory.getLogger(MailServiceImpl.class);

    @Autowired
    private JavaMailSender javaMailSender;

    private SimpleMailMessage simpleMailMessage;

    public MailServiceImpl() {
        this.simpleMailMessage = new SimpleMailMessage();
    }

    @Override
    public void sendEmailWithMessage(String email, String subject, String message) {

        LOGGER.info("m=sendEmailWithMessage, email={}, text={}", email, message);

        simpleMailMessage.setTo(email);
        simpleMailMessage.setSubject(subject);
        simpleMailMessage.setText(message);

        try {
            javaMailSender.send(simpleMailMessage);
        } catch (Exception e) {
            throw new SendEmailException(e.getMessage());
        }

    }
}
