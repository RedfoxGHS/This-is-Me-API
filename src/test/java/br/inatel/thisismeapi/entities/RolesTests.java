package br.inatel.thisismeapi.entities;

import br.inatel.thisismeapi.enums.RoleName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.boot.autoconfigure.web.servlet.ServletWebServerFactoryAutoConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

@SpringBootTest(classes = {ServletWebServerFactoryAutoConfiguration.class},
        webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@EnableAutoConfiguration(exclude = {SecurityAutoConfiguration.class})
@ActiveProfiles("dev")
class RolesTests {

    @Test
    void testCreateANNewInstanceWithNullConstructor() {
        Roles roles = new Roles();

        assertEquals(Roles.class, roles.getClass());
        assertNull(roles.getRoleName());
    }

    @Test
    void testSetRoleName() {
        Roles roles = new Roles();
        RoleName roleName = RoleName.ROLE_USER;
        roles.setRoleName(roleName);
        assertEquals(RoleName.ROLE_USER, roles.getRoleName());
        assertEquals(roleName.toString(), roles.getAuthority());
    }

}
