package br.inatel.thisismeapi.controllers.dtos.requests;

import java.io.Serializable;

public class UserResetPasswordRequestDTO implements Serializable {

    private String password;

    private String passwordVerify;

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getPasswordVerify() {
        return passwordVerify;
    }

    public void setPasswordVerify(String passwordVerify) {
        this.passwordVerify = passwordVerify;
    }
}
