package br.inatel.thisismeapi.services;

public interface MailService {

    void sendEmailWithMessage(String email, String subject, String message);
}
