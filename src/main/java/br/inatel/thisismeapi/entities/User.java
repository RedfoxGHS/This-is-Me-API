package br.inatel.thisismeapi.entities;

import br.inatel.thisismeapi.models.Roles;
import com.fasterxml.jackson.annotation.JsonIgnore;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.List;

@Document
public class User {

    @Id
    private String id;
    @Indexed(name = "unique_email", unique = true)
    @NotNull()
    @NotBlank()
    @Email(regexp = ".+[@].+[\\\\.].+")
    @Size(max = 120)
    private String email;

    @NotNull()
    @NotBlank()
    private String password;

    @DBRef
    @JsonIgnore
    private Character character;

    private List<Roles> roles;

    private String tokenResetPassword;

    public User() {
    }

    public User(String email, String password) {
        this.email = email;
        this.password = password;
    }

    public String getId() {
        return id;
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

    public Character getCharacter() {
        return character;
    }

    public void setCharacter(Character character) {
        this.character = character;
    }

    public List<Roles> getRoles() {
        return roles;
    }

    public void setRoles(List<Roles> roles) {
        this.roles = roles;
    }

    public String getTokenResetPassword() {
        return tokenResetPassword;
    }

    public void setTokenResetPassword(String tokenResetPassword) {
        this.tokenResetPassword = tokenResetPassword;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        User user = (User) o;

        return email.equals(user.email);
    }
}
