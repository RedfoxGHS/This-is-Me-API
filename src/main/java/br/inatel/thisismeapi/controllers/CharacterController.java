package br.inatel.thisismeapi.controllers;


import br.inatel.thisismeapi.controllers.dtos.responses.CharacterInfoResponseDTO;
import br.inatel.thisismeapi.entities.Character;
import br.inatel.thisismeapi.services.CharacterService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/character")
public class CharacterController {

    private static final Logger LOGGER = LoggerFactory.getLogger(CharacterController.class);

    @Autowired
    private CharacterService characterService;


    @GetMapping("/get-character")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Retorna o personagem do usuário"),
            @ApiResponse(responseCode = "401", description = "Não esta logado", content = @Content(schema = @Schema(hidden = true))),
            @ApiResponse(responseCode = "404", description = "Não há personagem cadastrado", content = @Content(schema = @Schema(hidden = true))),
            @ApiResponse(responseCode = "500", description = "Erro interno do servidor", content = @Content(schema = @Schema(hidden = true)))
    })
    @Operation(summary = "Retorna informações basica do personagem do usuário")
    public ResponseEntity<CharacterInfoResponseDTO> getCharacter(Authentication authentication) {

        String email = authentication.getName();
        LOGGER.info("m=getCharacter, email={}", email);

        Character character = characterService.findCharacterByEmail(email);

        return ResponseEntity.ok().body(new CharacterInfoResponseDTO(character));
    }

    @GetMapping("/get-character-all-infos")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Retorna todas as informações do personagem"),
            @ApiResponse(responseCode = "401", description = "Não esta logado", content = @Content(schema = @Schema(hidden = true))),
            @ApiResponse(responseCode = "404", description = "Não há personagem cadastrado", content = @Content(schema = @Schema(hidden = true))),
            @ApiResponse(responseCode = "500", description = "Erro interno do servidor", content = @Content(schema = @Schema(hidden = true)))
    })
    @Operation(summary = "Retorna todas as informações do personagem")
    public ResponseEntity<Character> getCharacterAllInfos(Authentication authentication) {

        String email = authentication.getName();
        LOGGER.info("m=getCharacter, email={}", email);

        Character character = characterService.findCharacterByEmail(email);

        return ResponseEntity.ok().body(character);
    }

    @PostMapping("/set-clothes")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Roupa alterada com sucesso"),
            @ApiResponse(responseCode = "400", description = "Há dados inválidos na requisição", content = @Content(schema = @Schema(hidden = true))),
            @ApiResponse(responseCode = "401", description = "Não esta logado", content = @Content(schema = @Schema(hidden = true))),
            @ApiResponse(responseCode = "500", description = "Erro interno do servidor", content = @Content(schema = @Schema(hidden = true)))
    })
    @Operation(summary = "Altera a roupa do personagem")
    public ResponseEntity<CharacterInfoResponseDTO> setClothes(Long number, Authentication authentication) {

        String email = authentication.getName();
        LOGGER.info("m=setClothes, email={}, clothesNumber={}", email, number);

        Character character = characterService.setClothes(email, number);

        return ResponseEntity.ok().body(new CharacterInfoResponseDTO(character));
    }
}
