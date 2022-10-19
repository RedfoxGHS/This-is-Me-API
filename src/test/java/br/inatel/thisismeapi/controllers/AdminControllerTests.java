package br.inatel.thisismeapi.controllers;


import br.inatel.thisismeapi.classestotest.EmailConst;
import br.inatel.thisismeapi.classestotest.PasswordConst;
import br.inatel.thisismeapi.controllers.exceptions.ConstraintViolationException;
import br.inatel.thisismeapi.controllers.wrapper.CreateUserContext;
import br.inatel.thisismeapi.entities.User;
import br.inatel.thisismeapi.entities.dtos.UserDtoInput;
import br.inatel.thisismeapi.services.impl.AdminServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.web.context.WebApplicationContext;

import java.util.Objects;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.when;

@ExtendWith(SpringExtension.class)
@WebMvcTest(value = AdminController.class, excludeAutoConfiguration = SecurityAutoConfiguration.class)
@ActiveProfiles("dev")
class AdminControllerTests {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private WebApplicationContext wac;

    @MockBean
    private AdminServiceImpl adminService;

    private final static String ENDPOINT_REGISTER = "/admin/register";


    @Test
    void testCreateNewAccountSuccess() throws Exception {
        UserDtoInput userDtoInput = new UserDtoInput(EmailConst.EMAIL_MAX_LENGHT_255, "12345");
        String verifyPassword = "12345";
        CreateUserContext createUserContext = new CreateUserContext();
        createUserContext.setUserDtoInput(userDtoInput);
        createUserContext.setVerifyPassword(verifyPassword);
        createUserContext.setCharacterName("Character Name");
        User user = new User(userDtoInput.getEmail(), userDtoInput.getPassword());

        when(adminService.createNewAccount(any(User.class), any(String.class))).thenReturn(user);

        mockMvc.perform(MockMvcRequestBuilders.post(ENDPOINT_REGISTER)
                        .accept("application/json")
                        .contentType("application/json")
                        .content(createUserContext.toStringJson())
                )
                .andExpect(MockMvcResultMatchers.status().isCreated());
    }


    @Test
    void testCreateNewAccountWithEmailInvalid() throws Exception {
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
    void testCreateNewAccountWithEmailNull() throws Exception {
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
    void testCreateNewAccountWithEmailWithSpacesOnly() throws Exception {
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
    void testCreateNewAccountWithEmailWithMoreMaxCharacter() throws Exception {
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
    void testCreateNewAccountWithVerifyPasswordDifferent() throws Exception {
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
    void testCreateNewAccountWithPasswordWithLessThanMinNumberOfCharacters() throws Exception {
        UserDtoInput userDtoInput = new UserDtoInput("test@email.com", PasswordConst.PASSWORD_WITH_LESS_MIN_LENGHT_4);
        String verifyPassword = PasswordConst.PASSWORD_WITH_LESS_MIN_LENGHT_4;
        CreateUserContext createUserContext = new CreateUserContext();
        createUserContext.setUserDtoInput(userDtoInput);
        createUserContext.setVerifyPassword(verifyPassword);
        createUserContext.setCharacterName("Character Name");

        when(adminService.createNewAccount(any(User.class), any(String.class)))
                .thenThrow(ConstraintViolationException.class);

        mockMvc.perform(MockMvcRequestBuilders.post(ENDPOINT_REGISTER)
                        .accept("application/json")
                        .contentType("application/json")
                        .content(createUserContext.toStringJson())
                )
                .andExpect(MockMvcResultMatchers.status().isBadRequest());
    }

    @Test
    void testCreateNewAccountWithPasswordWithMoreThanMaxNumberOfCharacters() throws Exception {
        UserDtoInput userDtoInput = new UserDtoInput("test@email.com", PasswordConst.PASSWORD_WITH_MORE_MAX_LENGHT_31);
        String verifyPassword = PasswordConst.PASSWORD_WITH_MORE_MAX_LENGHT_31;
        CreateUserContext createUserContext = new CreateUserContext();
        createUserContext.setUserDtoInput(userDtoInput);
        createUserContext.setVerifyPassword(verifyPassword);
        createUserContext.setCharacterName("Character Name");

        when(adminService.createNewAccount(any(User.class), any(String.class)))
                .thenThrow(ConstraintViolationException.class);

        mockMvc.perform(MockMvcRequestBuilders.post(ENDPOINT_REGISTER)
                        .accept("application/json")
                        .contentType("application/json")
                        .content(createUserContext.toStringJson())
                )
                .andExpect(MockMvcResultMatchers.status().isBadRequest());
    }

    @Test
    void testCreateNewAccountWithPasswordNull() throws Exception {
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
    void testCreateNewAccountWithPasswordWithSpacesOnly() throws Exception {
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
    void testHelloUser() throws Exception {

        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.get("/admin/helloAdmin")
                        .accept("application/json")
                        .contentType("application/json")
                )
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andReturn();

        assertEquals("Hello Admin", result.getResponse().getContentAsString());
    }
}
