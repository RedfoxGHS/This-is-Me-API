package br.inatel.thisismeapi.controllers;

import br.inatel.thisismeapi.controllers.dtos.requests.UserCreatingAccountRequestDTO;
import br.inatel.thisismeapi.controllers.dtos.responses.UserResponseDTO;
import br.inatel.thisismeapi.entities.User;
import br.inatel.thisismeapi.exceptions.StandardError;
import br.inatel.thisismeapi.services.impl.AdminServiceImpl;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/admin")
public class AdminController {

    private static final Logger LOGGER = LoggerFactory.getLogger(AdminController.class);

    @Autowired
    private AdminServiceImpl adminService;

    @PostMapping(value = "/register")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Usuário criado com sucesso"),
            @ApiResponse(responseCode = "400", description = "Dados inválidos para criação de usuário"),
            @ApiResponse(responseCode = "403", description = "Não tem permissão para criar uma conta admin"),
            @ApiResponse(responseCode = "409", description = "Usuário já existe"),
            @ApiResponse(responseCode = "500", description = "Erro interno do servidor ao criar usuário")
    })
    @Operation(summary = "Cria um novo usuário admin")
    public void createNewAccountAdmin(@RequestBody UserCreatingAccountRequestDTO userCreatingAccountRequestDTO) {

        LOGGER.info("m=createNewAccountAdmin, email={}", userCreatingAccountRequestDTO.getEmail());

        this.adminService.saveNewAccount(
                userCreatingAccountRequestDTO.getEmail(),
                userCreatingAccountRequestDTO.getPassword(),
                userCreatingAccountRequestDTO.getVerifyPassword(),
                userCreatingAccountRequestDTO.getCharacterName()
        );
        LOGGER.info("m=createNewAccountAdmin, status=CREATED");
    }

    @DeleteMapping("/delete-user-by-email")
    @ResponseStatus(code = HttpStatus.NO_CONTENT)
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Usuário deletado com sucesso"),
            @ApiResponse(responseCode = "400", description = "Erro na requisição"),
            @ApiResponse(responseCode = "401", description = "Não esta logado"),
            @ApiResponse(responseCode = "403", description = "Não tem permissão para deletar"),
            @ApiResponse(responseCode = "404", description = "Usuário não encontrado"),
            @ApiResponse(responseCode = "500", description = "Erro interno do servidor ao deletar usuário")
    })
    @Operation(summary = "Deleta um usuário pelo email (somente admin pode deletar)")
    public void deleteUserByEmail(@RequestParam String email) {
        LOGGER.info("m=deleteUserByEmail, email={}", email);
        this.adminService.deleteUserByEmail(email);
    }

    @GetMapping("/get-user-by-email")
    public UserResponseDTO getUserByEmail(String email) {
        return new UserResponseDTO(this.adminService.getUserByEmail(email));
    }

    @GetMapping("/helloAdmin")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Hello Admin"),
            @ApiResponse(responseCode = "401", description = "Não esta logado", content = @Content(schema = @Schema(allOf = StandardError.class))),
            @ApiResponse(responseCode = "403", description = "Não tem permissão para acessar", content = @Content(schema = @Schema(allOf = StandardError.class))),
    })
    @Operation(summary = "Verifica se o usuário é admin")
    public String helloAdmin() {
        LOGGER.info("m=helloAdmin");
        return "Hello Admin";
    }
}
