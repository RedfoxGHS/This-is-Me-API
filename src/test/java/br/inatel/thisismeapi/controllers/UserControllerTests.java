package br.inatel.thisismeapi.controllers;


import br.inatel.thisismeapi.classestotest.EmailConst;
import br.inatel.thisismeapi.classestotest.PasswordConst;
import br.inatel.thisismeapi.config.exceptions.StandardError;
import br.inatel.thisismeapi.controllers.exceptions.ConstraintViolationException;
import br.inatel.thisismeapi.controllers.wrapper.CreateUserContext;
import br.inatel.thisismeapi.controllers.wrapper.ResetPasswordContext;
import br.inatel.thisismeapi.controllers.wrapper.VerifyResetContext;
import br.inatel.thisismeapi.entities.Character;
import br.inatel.thisismeapi.entities.Roles;
import br.inatel.thisismeapi.entities.User;
import br.inatel.thisismeapi.entities.dtos.UserDtoInput;
import br.inatel.thisismeapi.enums.RoleName;
import br.inatel.thisismeapi.services.exceptions.TokenInvalidException;
import br.inatel.thisismeapi.services.exceptions.UnregisteredUserException;
import br.inatel.thisismeapi.services.impl.MailServiceImpl;
import br.inatel.thisismeapi.services.impl.UserServiceImpl;
import com.google.gson.Gson;
import io.swagger.v3.core.util.Json;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.context.HttpSessionSecurityContextRepository;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.web.context.WebApplicationContext;

import javax.servlet.http.Cookie;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.webAppContextSetup;

@ExtendWith(SpringExtension.class)
@WebMvcTest(value = UserController.class, excludeAutoConfiguration = SecurityAutoConfiguration.class)
@ActiveProfiles("dev")
public class UserControllerTests {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private WebApplicationContext wac;

    @MockBean
    private MailServiceImpl mailService;

    @MockBean
    private UserServiceImpl userService;

    private final static String ENDPOINT_REGISTER = "/user/register";
    private final static String ENDPOINT_USER_RESET = "/user/reset/";

    @BeforeEach
    public void setUp() {
        mockMvc = webAppContextSetup(wac).addFilter(((request, response, chain) -> {
            response.setCharacterEncoding("UTF-8");
            chain.doFilter(request, response);
        })).build();
    }

    @Test
    public void testCreateNewAccountSuccess() throws Exception {
        UserDtoInput userDtoInput = new UserDtoInput(EmailConst.EMAIL_MAX_LENGHT_255, "12345");
        String verifyPassword = "12345";
        CreateUserContext createUserContext = new CreateUserContext();
        createUserContext.setUserDtoInput(userDtoInput);
        createUserContext.setVerifyPassword(verifyPassword);
        createUserContext.setCharacterName("Character Name");

        User user = new User(userDtoInput.getEmail(), userDtoInput.getPassword());

        when(userService.createNewAccount(any(User.class), any(Character.class))).thenReturn(user);

        mockMvc.perform(MockMvcRequestBuilders.post(ENDPOINT_REGISTER)
                        .accept("application/json")
                        .contentType("application/json")
                        .content(createUserContext.toStringJson())
                )
                .andExpect(MockMvcResultMatchers.status().isCreated());
    }


    @Test
    public void testCreateNewAccountWithEmailInvalid() throws Exception {
        UserDtoInput userDtoInput = new UserDtoInput("test@", "12345");
        String verifyPassword = "12345";
        CreateUserContext createUserContext = new CreateUserContext();
        createUserContext.setUserDtoInput(userDtoInput);
        createUserContext.setVerifyPassword(verifyPassword);
        createUserContext.setCharacterName("Character Name");

        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.post(ENDPOINT_REGISTER)
                        .accept("application/json")
                        .contentType("application/json")
                        .content(createUserContext.toStringJson())
                )
                .andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andReturn();

        assertEquals("Email inválido!", Objects.requireNonNull(result.getResolvedException()).getMessage());
    }

    @Test
    public void testCreateNewAccountWithEmailNull() throws Exception {
        UserDtoInput userDtoInput = new UserDtoInput(null, "12345");
        String verifyPassword = "12345";
        CreateUserContext createUserContext = new CreateUserContext();
        createUserContext.setUserDtoInput(userDtoInput);
        createUserContext.setVerifyPassword(verifyPassword);
        createUserContext.setCharacterName("Character Name");

        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.post(ENDPOINT_REGISTER)
                        .accept("application/json")
                        .contentType("application/json")
                        .content(createUserContext.toStringJson())
                )
                .andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andReturn();

        assertEquals("Email não pode ser nulo!", Objects.requireNonNull(result.getResolvedException()).getMessage());
    }

    @Test
    public void testCreateNewAccountWithEmailWithSpacesOnly() throws Exception {
        UserDtoInput userDtoInput = new UserDtoInput("    ", "12345");
        String verifyPassword = "12345";
        CreateUserContext createUserContext = new CreateUserContext();
        createUserContext.setUserDtoInput(userDtoInput);
        createUserContext.setVerifyPassword(verifyPassword);
        createUserContext.setCharacterName("Character Name");

        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.post(ENDPOINT_REGISTER)
                        .accept("application/json")
                        .contentType("application/json")
                        .content(createUserContext.toStringJson())
                )
                .andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andReturn();

        assertEquals("Email não pode ser deixado em branco!", Objects.requireNonNull(result.getResolvedException()).getMessage());
    }

    @Test
    public void testCreateNewAccountWithEmailWithMoreMaxCharacter() throws Exception {
        UserDtoInput userDtoInput = new UserDtoInput(EmailConst.EMAIL_WITH_MORE_MAX_LENGHT_256, "12345");
        String verifyPassword = "12345";
        CreateUserContext createUserContext = new CreateUserContext();
        createUserContext.setUserDtoInput(userDtoInput);
        createUserContext.setVerifyPassword(verifyPassword);
        createUserContext.setCharacterName("Character Name");

        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.post(ENDPOINT_REGISTER)
                        .accept("application/json")
                        .contentType("application/json")
                        .content(createUserContext.toStringJson())
                )
                .andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andReturn();

        assertEquals("Email não pode ser ter mais de 255 digitos!", Objects.requireNonNull(result.getResolvedException()).getMessage());
    }

    @Test
    public void testCreateNewAccountWithVerifyPasswordDifferent() throws Exception {
        UserDtoInput userDtoInput = new UserDtoInput("test@email.com", "12345");
        String verifyPassword = "123";
        CreateUserContext createUserContext = new CreateUserContext();
        createUserContext.setUserDtoInput(userDtoInput);
        createUserContext.setVerifyPassword(verifyPassword);
        createUserContext.setCharacterName("Character Name");

        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.post(ENDPOINT_REGISTER)
                        .accept("application/json")
                        .contentType("application/json")
                        .content(createUserContext.toStringJson())
                )
                .andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andReturn();

        assertEquals("As Senhas não coincidem!", Objects.requireNonNull(result.getResolvedException()).getMessage());
    }

    @Test
    public void testCreateNewAccountWithPasswordWithLessThanMinNumberOfCharacters() throws Exception {
        UserDtoInput userDtoInput = new UserDtoInput("test@email.com", PasswordConst.PASSWORD_WITH_LESS_MIN_LENGHT_4);
        String verifyPassword = PasswordConst.PASSWORD_WITH_LESS_MIN_LENGHT_4;
        CreateUserContext createUserContext = new CreateUserContext();
        createUserContext.setUserDtoInput(userDtoInput);
        createUserContext.setVerifyPassword(verifyPassword);
        createUserContext.setCharacterName("Character Name");

        when(userService.createNewAccount(any(User.class), any(Character.class)))
                .thenThrow(ConstraintViolationException.class);

        mockMvc.perform(MockMvcRequestBuilders.post(ENDPOINT_REGISTER)
                        .accept("application/json")
                        .contentType("application/json")
                        .content(createUserContext.toStringJson())
                )
                .andExpect(MockMvcResultMatchers.status().isBadRequest());
    }

    @Test
    public void testCreateNewAccountWithPasswordWithMoreThanMaxNumberOfCharacters() throws Exception {
        UserDtoInput userDtoInput = new UserDtoInput("test@email.com", PasswordConst.PASSWORD_WITH_MORE_MAX_LENGHT_31);
        String verifyPassword = PasswordConst.PASSWORD_WITH_MORE_MAX_LENGHT_31;
        CreateUserContext createUserContext = new CreateUserContext();
        createUserContext.setUserDtoInput(userDtoInput);
        createUserContext.setVerifyPassword(verifyPassword);
        createUserContext.setCharacterName("Character Name");
        createUserContext.setSex("masculino");

        when(userService.createNewAccount(any(User.class), any(Character.class)))
                .thenThrow(ConstraintViolationException.class);

        mockMvc.perform(MockMvcRequestBuilders.post(ENDPOINT_REGISTER)
                        .accept("application/json")
                        .contentType("application/json")
                        .content(createUserContext.toStringJson())
                )
                .andExpect(MockMvcResultMatchers.status().isBadRequest());
    }

    @Test
    public void testCreateNewAccountWithPasswordNull() throws Exception {
        UserDtoInput userDtoInput = new UserDtoInput("test@email.com", null);
        String verifyPassword = "12345";
        CreateUserContext createUserContext = new CreateUserContext();
        createUserContext.setUserDtoInput(userDtoInput);
        createUserContext.setVerifyPassword(verifyPassword);
        createUserContext.setCharacterName("Character Name");

        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.post(ENDPOINT_REGISTER)
                        .accept("application/json")
                        .contentType("application/json")
                        .content(createUserContext.toStringJson())
                )
                .andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andReturn();

        assertEquals("Senha não pode ser nula!", Objects.requireNonNull(result.getResolvedException()).getMessage());
    }

    @Test
    public void testCreateNewAccountWithPasswordWithSpacesOnly() throws Exception {
        UserDtoInput userDtoInput = new UserDtoInput("test@email.com", "    ");
        String verifyPassword = "12345";
        CreateUserContext createUserContext = new CreateUserContext();
        createUserContext.setUserDtoInput(userDtoInput);
        createUserContext.setVerifyPassword(verifyPassword);
        createUserContext.setCharacterName("Character Name");

        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.post(ENDPOINT_REGISTER)
                        .accept("application/json")
                        .contentType("application/json")
                        .content(createUserContext.toStringJson())
                )
                .andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andReturn();

        assertEquals("Senha não pode ser deixada em branco!", Objects.requireNonNull(result.getResolvedException()).getMessage());
    }

    @Test
    public void testHelloUser() throws Exception {

        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.get("/user/helloUser")
                        .accept("application/json")
                        .contentType("application/json")
                )
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andReturn();

        assertEquals("Hello User", result.getResponse().getContentAsString());
    }

    @Test
    public void testGetCharacterSuccess() throws Exception {

        Character character = new Character();
        String email = "test@test.com";
        character.setCharacterName("Character Name");

        List<Roles> rolesList = new ArrayList<>();
        rolesList.add(new Roles(RoleName.ROLE_ADMIN));
        rolesList.add(new Roles(RoleName.ROLE_USER));
        Authentication auth =
                new UsernamePasswordAuthenticationToken("admin@admin.com", null, rolesList);

        when(userService.findCharacterByEmail(any(String.class))).thenReturn(character);


        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.get("/user/get-character")
                        .principal(auth)
                        .accept("application/json")
                        .sessionAttr(HttpSessionSecurityContextRepository.SPRING_SECURITY_CONTEXT_KEY, auth)
                        .contentType("application/json")
                        .content("")
                )
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andReturn();

        assertTrue(result.getResponse().getContentAsString().contains(character.getCharacterName()));
    }

    @Test
    public void testForgotPasswordSendEmailSuccess() throws Exception {
        String email = "test@test.com";
        String responseExpected = "Código enviado para seu email: " + email;

        doNothing().when(mailService).sendEmailForgotPassword(email);

        mockMvc.perform(MockMvcRequestBuilders.post(ENDPOINT_USER_RESET + "forgot-password")
                        .accept("application/json")
                        .param("email", email)
                        .contentType("application/json")
                )
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().string(responseExpected));
    }

    @Test
    public void testForgotPasswordSendEmailFail() throws Exception {
        String email = "testcom";

        doThrow(UnregisteredUserException.class).when(mailService).sendEmailForgotPassword(email);

        mockMvc.perform(MockMvcRequestBuilders.post(ENDPOINT_USER_RESET + "forgot-password")
                        .accept("application/json")
                        .param("email", email)
                        .contentType("application/json")
                )
                .andExpect(MockMvcResultMatchers.status().isUnauthorized());
    }


    @Test
    public void testVerifyCodeResetSuccess() throws Exception {
        VerifyResetContext verifyResetContext = new VerifyResetContext();
        verifyResetContext.setEmail("test@test.com");
        verifyResetContext.setNumber("123456");

        when(mailService.verifyNumberPassword(verifyResetContext.getEmail(), verifyResetContext.getNumber()))
                .thenReturn("jwt");

        mockMvc.perform(MockMvcRequestBuilders.post(ENDPOINT_USER_RESET + "verify-code-reset")
                        .accept("application/json")
                        .contentType("application/json")
                        .content(Json.pretty(verifyResetContext))
                )
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.cookie().value("token_reset", "jwt"));
    }

    @Test
    public void testVerifyCodeResetThrowTokenInvalidException() throws Exception {
        VerifyResetContext verifyResetContext = new VerifyResetContext();
        verifyResetContext.setEmail("test@test.com");
        verifyResetContext.setNumber("123456");

        doThrow(TokenInvalidException.class).when(mailService).verifyNumberPassword(
                verifyResetContext.getEmail(), verifyResetContext.getNumber());

        mockMvc.perform(MockMvcRequestBuilders.post(ENDPOINT_USER_RESET + "verify-code-reset")
                        .accept("application/json")
                        .contentType("application/json")
                        .content(Json.pretty(verifyResetContext))
                )
                .andExpect(MockMvcResultMatchers.status().isUnauthorized())
                .andExpect(MockMvcResultMatchers.cookie().doesNotExist("token_reset"));
    }

    @Test
    public void testResetPassword() throws Exception {
        ResetPasswordContext resetPasswordContext = new ResetPasswordContext();
        resetPasswordContext.setPassword("123456");
        resetPasswordContext.setPasswordVerify("123456");

        Cookie cookie = new Cookie("token_reset", "jwt");
        cookie.setPath("/");
        cookie.setHttpOnly(true);

        userService.resetPassword(resetPasswordContext.getPassword(), "jwt");

        mockMvc.perform(MockMvcRequestBuilders.post(ENDPOINT_USER_RESET + "reset-password")
                        .accept("application/json")
                        .cookie(cookie)
                        .contentType("application/json")
                        .content(Json.pretty(resetPasswordContext))
                )
                .andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    public void testResetPasswordWithoutTokenJWT() throws Exception {
        ResetPasswordContext resetPasswordContext = new ResetPasswordContext();
        resetPasswordContext.setPassword("123456");
        resetPasswordContext.setPasswordVerify("123456");
        String responseExpected = "Não encontrado Token valido";
        Gson gson = new Gson();
        userService.resetPassword(resetPasswordContext.getPassword(), "jwt");

        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.post(ENDPOINT_USER_RESET + "reset-password")
                        .accept("application/json")
                        .contentType("application/json")
                        .content(Json.pretty(resetPasswordContext))
                )
                .andExpect(MockMvcResultMatchers.status().isUnauthorized())
                .andReturn();

        StandardError response = gson.fromJson(result.getResponse().getContentAsString(), StandardError.class);
        assertEquals(responseExpected, response.getMessage());
    }
}
