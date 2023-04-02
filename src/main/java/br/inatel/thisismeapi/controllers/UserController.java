package br.inatel.thisismeapi.controllers;

import br.inatel.thisismeapi.controllers.dtos.requests.UserCreatingAccountRequestDTO;
import br.inatel.thisismeapi.controllers.dtos.requests.UserResetPasswordRequestDTO;
import br.inatel.thisismeapi.controllers.dtos.requests.UserVerifyResetPasswordDTO;
import br.inatel.thisismeapi.exceptions.TokenInvalidException;
import br.inatel.thisismeapi.services.impl.UserServiceImpl;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.WebUtils;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@RestController
@RequestMapping("/user")
public class UserController {

    private static final Logger LOGGER = LoggerFactory.getLogger(UserController.class);

    @Autowired
    private UserServiceImpl userService;

    @PostMapping("/register")
    @ResponseStatus(code = HttpStatus.CREATED)
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Usuário criado com sucesso"),
            @ApiResponse(responseCode = "400", description = "Dados inválidos para criação de usuário"),
            @ApiResponse(responseCode = "409", description = "Usuário já existe"),
            @ApiResponse(responseCode = "500", description = "Erro interno do servidor ao criar usuário")
    })
    @Operation(summary = "Cria um novo usuário")
    public void createNewAccount(@RequestBody UserCreatingAccountRequestDTO userCreatingAccountRequestDTO, HttpServletResponse response) {

        LOGGER.info("m=createNewAccount, email={}", userCreatingAccountRequestDTO.getEmail());

        String jwt = this.userService.saveNewAccount(
                userCreatingAccountRequestDTO.getEmail(),
                userCreatingAccountRequestDTO.getPassword(),
                userCreatingAccountRequestDTO.getVerifyPassword(),
                userCreatingAccountRequestDTO.getCharacterName()
        );

        Cookie cookie = new Cookie("token", jwt);
        cookie.setPath("/");
        cookie.setHttpOnly(true);
        cookie.setMaxAge(60 * 60 * 24 * 30);
        response.addCookie(cookie);
        LOGGER.info("m=createNewAccount, status=CREATED");
    }

    @PostMapping("/reset/forgot-password")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Email enviado com sucesso"),
            @ApiResponse(responseCode = "400", description = "Dados inválidos para envio de email", content = @Content(schema = @Schema(hidden = true))),
            @ApiResponse(responseCode = "404", description = "Usuário não encontrado", content = @Content(schema = @Schema(hidden = true))),
            @ApiResponse(responseCode = "500", description = "Erro interno do servidor ao enviar email", content = @Content(schema = @Schema(hidden = true)))
    })
    @Operation(summary = "Envia um email para o usuário com um código para resetar a senha")
    public ResponseEntity<String> forgotPasswordSendEmail(@RequestParam("email") String email) {

        LOGGER.info("m=forgotPasswordSendEmail, email={}", email);

        this.userService.sendEmailToResetPassword(email);

        LOGGER.info("m=forgotPasswordSendEmail, email={}, status={}", email, "Código enviado");
        return ResponseEntity.ok().body("Código enviado para seu email: " + email);
    }

    @PostMapping("/reset/verify-code-reset")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Código verificado com sucesso"),
            @ApiResponse(responseCode = "400", description = "Dados inválidos para verificação de código"),
            @ApiResponse(responseCode = "404", description = "Usuário não encontrado"),
            @ApiResponse(responseCode = "500", description = "Erro interno do servidor ao verificar código")
    })
    @Operation(summary = "Verifica se o código enviado para o email do usuário é válido")
    public void verifyCodeResetPassword(@RequestBody UserVerifyResetPasswordDTO userVerifyResetPasswordDTO, HttpServletResponse response) {

        LOGGER.info("m=verifyCodeResetPassword, email={}", userVerifyResetPasswordDTO.getEmail());
        String jwt = this.userService.getResetTokenWithEmailAndNumber(
                userVerifyResetPasswordDTO.getEmail(), userVerifyResetPasswordDTO.getNumber());

        Cookie cookie = new Cookie("token_reset", jwt);
        cookie.setPath("/");
        cookie.setHttpOnly(true);
        response.addCookie(cookie);
    }

    @PostMapping("/reset/reset-password")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Senha resetada com sucesso"),
            @ApiResponse(responseCode = "400", description = "Dados inválidos para resetar senha"),
            @ApiResponse(responseCode = "404", description = "Usuário não encontrado"),
            @ApiResponse(responseCode = "500", description = "Erro interno do servidor ao resetar senha")
    })
    @Operation(summary = "Reseta a senha do usuário")
    public void resetPassword(@RequestBody UserResetPasswordRequestDTO userResetPasswordRequestDTO, HttpServletRequest request) {

        LOGGER.info("m=resetPassword");

        String tokenReset;
        try {
            tokenReset = WebUtils.getCookie(request, "token_reset").getValue();
        } catch (NullPointerException e) {
            throw new TokenInvalidException("Token não encontrado, solicite um novo código para troca de senha!");
        }

        userService.resetPassword(
                userResetPasswordRequestDTO.getPassword(),
                userResetPasswordRequestDTO.getPasswordVerify(),
                tokenReset);
    }

    @GetMapping("/helloUser")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Usuário logado com sucesso"),
            @ApiResponse(responseCode = "401", description = "Usuário não logado")
    })
    @Operation(summary = "Verifica se o usuário está logado")
    public String helloUser() {
        LOGGER.info("m=helloUser");
        return "Hello User";
    }
}
