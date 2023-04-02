package br.inatel.thisismeapi.controllers.dtos.requests;

import java.io.Serializable;

public class UserVerifyResetPasswordDTO implements Serializable {

    private String email;

    private Integer number;

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Integer getNumber() {
        return number;
    }

    public void setNumber(Integer number) {
        this.number = number;
    }
}
