package br.inatel.thisismeapi.controllers.dtos.responses;

import br.inatel.thisismeapi.entities.User;
import br.inatel.thisismeapi.models.Roles;

import java.util.List;

public class UserResponseDTO {

    private String id;
    private String email;
    private List<Roles> roles;

    public UserResponseDTO() {
    }

    public UserResponseDTO(User user) {
        this.id = user.getId();
        this.email = user.getEmail();
        this.roles = user.getRoles();
    }

    public String getId() {
        return id;
    }

    public String getEmail() {
        return email;
    }

    public List<Roles> getRoles() {
        return roles;
    }
}
