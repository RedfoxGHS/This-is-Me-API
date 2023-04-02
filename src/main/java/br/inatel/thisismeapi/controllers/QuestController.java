package br.inatel.thisismeapi.controllers;

import br.inatel.thisismeapi.controllers.dtos.responses.QuestResponseDTO;
import br.inatel.thisismeapi.entities.Quest;
import br.inatel.thisismeapi.services.impl.QuestServiceImpl;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;


@RestController
@RequestMapping("/quest")
public class QuestController {

    private static final Logger LOGGER = LoggerFactory.getLogger(QuestController.class);

    @Autowired
    private QuestServiceImpl questService;

    @PostMapping
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Quest criada com sucesso"),
            @ApiResponse(responseCode = "400", description = "Há dados inválidos na requisição"),
            @ApiResponse(responseCode = "401", description = "Não esta logado"),
            @ApiResponse(responseCode = "500", description = "Erro interno do servidor")
    })
    @Operation(summary = "Cria uma nova quest")
    public void createNewQuest(@RequestBody @Valid Quest quest, Authentication authentication) {

        LOGGER.info("m=createNewQuest, email={}", authentication.getName());
        questService.createNewQuest(quest, authentication.getName());
    }

    @GetMapping
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Retorna todas as quests do usuário"),
            @ApiResponse(responseCode = "401", description = "Não esta logado", content = @Content(schema = @Schema(hidden = true))),
            @ApiResponse(responseCode = "404", description = "Não há quests cadastradas", content = @Content(schema = @Schema(hidden = true))),
            @ApiResponse(responseCode = "500", description = "Erro interno do servidor", content = @Content(schema = @Schema(hidden = true)))
    })
    @Operation(summary = "Retorna a quest do Usuário pelo id")
    public QuestResponseDTO getQuestById(@RequestParam String id, Authentication authentication) {

        LOGGER.info("m=getQuestById, email={}", authentication.getName());
        return new QuestResponseDTO(questService.getQuestById(id, authentication.getName()));
    }

    @Deprecated
    @GetMapping("/today-cards")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Retorna todas as quests do usuário"),
            @ApiResponse(responseCode = "401", description = "Não esta logado", content = @Content(schema = @Schema(hidden = true))),
            @ApiResponse(responseCode = "404", description = "Não há quests cadastradas", content = @Content(schema = @Schema(hidden = true))),
            @ApiResponse(responseCode = "500", description = "Erro interno do servidor", content = @Content(schema = @Schema(hidden = true)))
    })
    public List<Quest> getDayCards(Authentication authentication) {

        LOGGER.info("m=getDayCards, email={}", authentication.getName());
        List<Quest> quests = questService.getQuestToday(authentication.getName());

        return quests;
    }
}
