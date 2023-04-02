package br.inatel.thisismeapi.models;

import br.inatel.thisismeapi.enums.RoleName;
import org.springframework.security.core.GrantedAuthority;

public class Roles implements GrantedAuthority {

    private RoleName roleName;

    public Roles() {
    }

    public Roles(RoleName roleName) {
        this.roleName = roleName;
    }


    @Override
    public String getAuthority() {
        return roleName.toString();
    }

    public RoleName getRoleName() {
        return roleName;
    }

    public void setRoleName(RoleName roleName) {
        this.roleName = roleName;
    }
}
