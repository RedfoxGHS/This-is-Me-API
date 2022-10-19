package br.inatel.thisismeapi.entities;

import br.inatel.thisismeapi.enums.RoleName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.boot.autoconfigure.web.servlet.ServletWebServerFactoryAutoConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(classes = {ServletWebServerFactoryAutoConfiguration.class},
        webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@EnableAutoConfiguration(exclude = {SecurityAutoConfiguration.class})
@ActiveProfiles("dev")
class UserTests {

    @Test
    void testIfTwoUsersWithTheSameEmailIsTheSameUser() {
        String email = "test@email.com";
        User user1 = new User();
        user1.setEmail(email);

        User user2 = new User();
        user2.setEmail(email);
        boolean result = user1.equals(user2);
        assertTrue(result);
    }


    @Test
    void testIfTwoUsersWithDifferentEmailIsNotTheSameUser() {
        String email = "test@email.com";
        User user1 = new User();
        user1.setEmail(email);

        User user2 = new User();
        user2.setEmail("test2@email.com");
        boolean result = user1.equals(user2);
        assertFalse(result);
    }

    @Test
    void testIfIstheSameObjectUser() {
        String email = "test@email.com";
        User user1 = new User();
        user1.setEmail(email);

        boolean result = user1.equals(user1);

        assertTrue(result);
    }

    @Test
    void testUserIsNotEqualNull() {
        String email = "test@email.com";
        User user1 = new User();
        user1.setEmail(email);

        boolean result = user1.equals(null);

        assertFalse(result);
    }

    @Test
    void testUserIsNotEqualADifferentObject() {
        String email = "test@email.com";
        User user1 = new User();
        user1.setEmail(email);

        String anotherClass = new String("Test");


        boolean result = user1.equals(anotherClass);

        assertFalse(result);
    }

    @Test
    void testUserGetId() {
        User user1 = new User();

        assertNull(user1.getId());
    }

    @Test
    void testUserGetRoles() {
        User user = new User();
        List<Roles> roles = new ArrayList<>();
        roles.add(new Roles(RoleName.ROLE_USER));
        roles.add(new Roles(RoleName.ROLE_ADMIN));
        user.setRoles(roles);

        List<Roles> actual = user.getRoles();
        assertEquals(roles.get(0).getRoleName(), actual.get(0).getRoleName());
        assertEquals(roles.get(1).getRoleName(), actual.get(1).getRoleName());
        assertEquals(roles, actual);
    }

    @Test
    void testGetTokenResetPassword() {
        User user = new User();
        user.setTokenResetPassword("123456");

        assertEquals("123456", user.getTokenResetPassword());
    }
}
