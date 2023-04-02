package br.inatel.thisismeapi.services;

import br.inatel.thisismeapi.entities.User;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

public interface UserService {

    String saveNewAccount(String email, String password, String verifyPassword, String characterName);

    User updateUser(User user);

    User findUserByEmail(String email);

    void sendEmailToResetPassword(String email);

    String getResetTokenWithEmailAndNumber(String email, Integer number);

    public void resetPassword(String password, String passwordVerify, String resetToken);

    BCryptPasswordEncoder passwordEncoder();

}
