package br.inatel.thisismeapi.cucumber.models;

public class CreateAccountContext {
    public String characterName;
    public String email;
    public String password;
    public String verifyPassword;

    public CreateAccountContext() {
    }

    public CreateAccountContext(String characterName, String email, String password, String verifyPassword) {
        this.characterName = characterName;
        this.email = email;
        this.password = password;
        this.verifyPassword = verifyPassword;
    }


    public String getCharacterName() {
        return characterName;
    }

    public void setCharacterName(String characterName) {
        this.characterName = characterName;
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

    public String getVerifyPassword() {
        return verifyPassword;
    }

    public void setVerifyPassword(String verifyPassword) {
        this.verifyPassword = verifyPassword;
    }
}
