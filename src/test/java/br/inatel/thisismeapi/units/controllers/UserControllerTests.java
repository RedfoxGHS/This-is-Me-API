package br.inatel.thisismeapi.units.controllers;


import br.inatel.thisismeapi.controllers.UserController;
import br.inatel.thisismeapi.controllers.dtos.requests.UserCreatingAccountRequestDTO;
import br.inatel.thisismeapi.controllers.dtos.requests.UserResetPasswordRequestDTO;
import br.inatel.thisismeapi.controllers.dtos.requests.UserVerifyResetPasswordDTO;
import br.inatel.thisismeapi.entities.Character;
import br.inatel.thisismeapi.entities.User;
import br.inatel.thisismeapi.exceptions.TokenInvalidException;
import br.inatel.thisismeapi.services.impl.UserServiceImpl;
import org.junit.Ignore;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@SpringBootTest(classes = {UserController.class})
class UserControllerTests {

    private String email = "test@test.com";
    private String password = "123456";
    private String characterName = "Test Name";
    private Integer verifyNumber = 123456;
    private String resetToken = "resetToken";

    @Autowired
    private UserController userController;

    @MockBean
    private UserServiceImpl userService;

    @Test
    void testCreateNewAccountUserSuccess() {

        UserCreatingAccountRequestDTO requestDTO = this.getUserCreatingAccountRequestDTO();
        User user = this.getUser();

        MockHttpServletResponse httpServletResponse = new MockHttpServletResponse();

        String jwt = "token";

        when(userService.saveNewAccount(
                requestDTO.getEmail(),
                requestDTO.getPassword(),
                requestDTO.getVerifyPassword(),
                requestDTO.getCharacterName()
        )).thenReturn(jwt);
        this.userController.createNewAccount(requestDTO, httpServletResponse);

        verify(userService).saveNewAccount(
                requestDTO.getEmail(),
                requestDTO.getPassword(),
                requestDTO.getVerifyPassword(),
                requestDTO.getCharacterName()
        );
    }

    @Test
    void testForgotPasswordSendEmailSuccess() {

        doNothing().when(userService).sendEmailToResetPassword(email);
        ResponseEntity<String> response = userController.forgotPasswordSendEmail(email);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("Código enviado para seu email: " + email, response.getBody());
        verify(userService).sendEmailToResetPassword(email);
    }

    @Test
    void testVerifyCodeResetPasswordSuccess() {

        UserVerifyResetPasswordDTO userVerifyResetPasswordDTO = this.getUserVerifyResetPasswordDTO();
        MockHttpServletResponse httpServletResponse = new MockHttpServletResponse();

        when(userService.getResetTokenWithEmailAndNumber(
                userVerifyResetPasswordDTO.getEmail(), userVerifyResetPasswordDTO.getNumber()))
                .thenReturn(resetToken);

        userController.verifyCodeResetPassword(userVerifyResetPasswordDTO, httpServletResponse);

        verify(userService).getResetTokenWithEmailAndNumber(
                userVerifyResetPasswordDTO.getEmail(), userVerifyResetPasswordDTO.getNumber());
    }

    @Test
    void testResetPasswordSuccess() {

        UserResetPasswordRequestDTO requestDTO = this.getUserResetPasswordRequestDTO();
        MockHttpServletRequest httpServletRequest = new MockHttpServletRequest();
        Cookie cookie = new Cookie("token_reset", resetToken);
        httpServletRequest.setCookies(cookie);


        doNothing().when(userService).resetPassword(requestDTO.getPassword(), requestDTO.getPasswordVerify(), resetToken);

        userController.resetPassword(requestDTO, httpServletRequest);

        verify(userService).resetPassword(anyString(), anyString(), anyString());
    }

    @Test
    void testResetPasswordSuccessWithResetTokenNull() {

        UserResetPasswordRequestDTO requestDTO = this.getUserResetPasswordRequestDTO();
        MockHttpServletRequest httpServletRequest = new MockHttpServletRequest();

        TokenInvalidException exception = assertThrows(TokenInvalidException.class, () -> {
            userController.resetPassword(requestDTO, httpServletRequest);
        });

        assertEquals("Token não encontrado, solicite um novo código para troca de senha!", exception.getMessage());
    }

    @Test
    void testHelloUserSuccess() {

        String expected = "Hello User";

        String actual = this.userController.helloUser();

        assertEquals(expected, actual);
    }

    private UserResetPasswordRequestDTO getUserResetPasswordRequestDTO() {

        UserResetPasswordRequestDTO userResetPasswordRequestDTO = new UserResetPasswordRequestDTO();
        userResetPasswordRequestDTO.setPassword(this.password);
        userResetPasswordRequestDTO.setPasswordVerify(this.password);
        return userResetPasswordRequestDTO;
    }

    private UserVerifyResetPasswordDTO getUserVerifyResetPasswordDTO() {

        UserVerifyResetPasswordDTO userVerifyResetPasswordDTO = new UserVerifyResetPasswordDTO();
        userVerifyResetPasswordDTO.setEmail(this.email);
        userVerifyResetPasswordDTO.setNumber(this.verifyNumber);
        return userVerifyResetPasswordDTO;
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
