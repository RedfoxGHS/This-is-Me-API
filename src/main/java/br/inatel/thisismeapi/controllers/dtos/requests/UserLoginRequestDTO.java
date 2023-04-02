package br.inatel.thisismeapi.controllers.dtos.requests;


import java.io.Serializable;

public class UserLoginRequestDTO implements Serializable {

    private String email;

    private String password;

    public UserLoginRequestDTO() {
    }

    public UserLoginRequestDTO(String email, String password) {
        this.email = email;
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
