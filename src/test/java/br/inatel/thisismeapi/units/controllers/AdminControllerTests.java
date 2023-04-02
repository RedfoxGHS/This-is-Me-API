package br.inatel.thisismeapi.units.controllers;


import br.inatel.thisismeapi.controllers.AdminController;
import br.inatel.thisismeapi.controllers.dtos.requests.UserCreatingAccountRequestDTO;
import br.inatel.thisismeapi.entities.Character;
import br.inatel.thisismeapi.entities.User;
import br.inatel.thisismeapi.services.impl.AdminServiceImpl;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;


@SpringBootTest(classes = {AdminController.class})
class AdminControllerTests {

    private String email = "test@test.com";
    private String password = "123456";
    private String characterName = "Test Name";
    private Integer verifyNumber = 123456;
    @Autowired
    private AdminController adminController;

    @MockBean
    private AdminServiceImpl adminService;

    @Test
    void testCreateNewAccountAdminSuccess() {

        UserCreatingAccountRequestDTO requestDTO = this.getUserCreatingAccountRequestDTO();
        User user = this.getUser();
        String token = "token";

        when(adminService.saveNewAccount(
                requestDTO.getEmail(),
                requestDTO.getPassword(),
                requestDTO.getVerifyPassword(),
                requestDTO.getCharacterName()
        )).thenReturn(token);

        this.adminController.createNewAccountAdmin(requestDTO);

        verify(adminService).saveNewAccount(
                requestDTO.getEmail(),
                requestDTO.getPassword(),
                requestDTO.getVerifyPassword(),
                requestDTO.getCharacterName()
        );
    }

    @Test
    void testHelloAdminSuccess() {

        String expected = "Hello Admin";

        String actual = this.adminController.helloAdmin();

        assertEquals(expected, actual);
    }

    private UserCreatingAccountRequestDTO getUserCreatingAccountRequestDTO() {

        UserCreatingAccountRequestDTO userCreatingAccountRequestDTO = new UserCreatingAccountRequestDTO();
        userCreatingAccountRequestDTO.setEmail(this.email);
        userCreatingAccountRequestDTO.setPassword(this.password);
        userCreatingAccountRequestDTO.setVerifyPassword(this.password);
        userCreatingAccountRequestDTO.setCharacterName(this.characterName);

        return userCreatingAccountRequestDTO;
    }

    private User getUser() {

        User user = new User();
        user.setEmail(this.email);
        user.setPassword(this.password);
        user.setCharacter(new Character(this.email, this.characterName));
        return user;
    }
}
