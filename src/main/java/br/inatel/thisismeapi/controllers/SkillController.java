package br.inatel.thisismeapi.controllers;

import br.inatel.thisismeapi.controllers.dtos.responses.SkillResponseDTO;
import br.inatel.thisismeapi.entities.Skill;
import br.inatel.thisismeapi.services.SkillService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(value = "/skill")

public class SkillController {

    private static final Logger LOGGER = LoggerFactory.getLogger(SkillController.class);

    @Autowired
    private SkillService skillService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Skill criada com sucesso"),
            @ApiResponse(responseCode = "400", description = "Erro na requisição"),
            @ApiResponse(responseCode = "401", description = "Não esta logado"),
            @ApiResponse(responseCode = "500", description = "Erro interno do servidor")
    })
    @Operation(summary = "Cria uma nova skill")
    public void createSkill(@RequestBody Skill skill, Authentication authentication) {

        LOGGER.info("m=createSkill, email={}", authentication.getName());
        skillService.createSkill(skill, authentication.getName());
    }

    @GetMapping("/all")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Retorna todas as skills do usuário"),
            @ApiResponse(responseCode = "401", description = "Não esta logado", content = @Content(schema = @Schema(hidden = true))),
            @ApiResponse(responseCode = "404", description = "Não há skills cadastradas", content = @Content(schema = @Schema(hidden = true))),
            @ApiResponse(responseCode = "500", description = "Erro interno do servidor", content = @Content(schema = @Schema(hidden = true)))
    })
    @Operation(
            summary = "Retorna todas as skills do usuário"
    )
    public List<SkillResponseDTO> getAllSkills(Authentication authentication) {

        LOGGER.info("m=getAllSkills, email={}", authentication.getName());
        List<Skill> skills = skillService.getAllSkills(authentication.getName());
        return skills.stream().map(SkillResponseDTO::new).toList();
    }
}
