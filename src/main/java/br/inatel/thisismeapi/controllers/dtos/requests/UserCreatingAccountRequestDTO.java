package br.inatel.thisismeapi.controllers.dtos.requests;

import com.google.gson.Gson;

import java.io.Serializable;

public class UserCreatingAccountRequestDTO implements Serializable {

    private String email;

    private String password;

    private String verifyPassword;

    private String characterName;

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

    public String getVerifyPassword() {
        return verifyPassword;
    }

    public void setVerifyPassword(String verifyPassword) {
        this.verifyPassword = verifyPassword;
    }

    public String getCharacterName() {
        return characterName;
    }

    public void setCharacterName(String characterName) {
        this.characterName = characterName;
    }

    public String toStringJson() {
        return new Gson().toJson(this);
    }
}
