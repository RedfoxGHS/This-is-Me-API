package br.inatel.thisismeapi.controllers;

import br.inatel.thisismeapi.controllers.dtos.responses.CardResponseDTO;
import br.inatel.thisismeapi.entities.SubQuest;
import br.inatel.thisismeapi.services.SubQuestsService;
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
import java.util.stream.Collectors;

@RestController
@RequestMapping("/subQuest")
public class SubQuestController {

    private static final Logger LOGGER = LoggerFactory.getLogger(SubQuestController.class);
    @Autowired
    private SubQuestsService subQuestsService;

    @GetMapping("/today-cards")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Retorna as quests em formato de cards do dia"),
            @ApiResponse(responseCode = "401", description = "Não esta logado", content = @Content(schema = @Schema(hidden = true))),
            @ApiResponse(responseCode = "404", description = "Não há quests para o dia de hoje", content = @Content(schema = @Schema(hidden = true))),
            @ApiResponse(responseCode = "500", description = "Erro interno do servidor", content = @Content(schema = @Schema(hidden = true)))
    })
    @Operation(summary = "Retorna as quests em formato de cards do dia")
    public List<CardResponseDTO> getAllSubQuestCurrentDayAsCards(Authentication authentication) {

        LOGGER.info("m=getAllSubQuestCurrentDayAsCards, email={}", authentication.getName());
        List<SubQuest> subQuestList = subQuestsService.findAllSubQuestsToday(authentication.getName());

        return subQuestList.stream().map(CardResponseDTO::new).collect(Collectors.toList());
    }

    @GetMapping("/weekly-cards")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Retorna as quests em formato de cards da semana"),
            @ApiResponse(responseCode = "401", description = "Não esta logado", content = @Content(schema = @Schema(hidden = true))),
            @ApiResponse(responseCode = "404", description = "Não há quests para a semana", content = @Content(schema = @Schema(hidden = true))),
            @ApiResponse(responseCode = "500", description = "Erro interno do servidor", content = @Content(schema = @Schema(hidden = true)))
    })
    @Operation(summary = "Retorna as quests em formato de cards da semana")
    public List<CardResponseDTO> getAllSubQuestCurrentWeekAsCards(Authentication authentication) {

        LOGGER.info("m=getAllSubQuestCurrentWeekAsCards, email={}", authentication.getName());
        List<SubQuest> subQuestList = subQuestsService.findAllSubQuestsWeekly(authentication.getName());

        return subQuestList.stream().map(CardResponseDTO::new).toList();
    }

    @GetMapping("/next-week-cards")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Retorna as quests em formato de cards da semana"),
            @ApiResponse(responseCode = "401", description = "Não esta logado", content = @Content(schema = @Schema(hidden = true))),
            @ApiResponse(responseCode = "404", description = "Não há quests para a próxima semana", content = @Content(schema = @Schema(hidden = true))),
            @ApiResponse(responseCode = "500", description = "Erro interno do servidor", content = @Content(schema = @Schema(hidden = true)))
    })
    @Operation(summary = "Retorna as quests em formato de cards da próxima semana")
    public List<CardResponseDTO> getAllSubQuestNextWeekAsCards(Authentication authentication) {

        LOGGER.info("m=getAllSubQuestNextWeekAsCards, email={}", authentication.getName());
        List<SubQuest> subQuestList = subQuestsService.findAllSubQuestsFromNextWeek(authentication.getName());

        return subQuestList.stream().map(CardResponseDTO::new).toList();
    }

    @GetMapping("/late-cards")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Retorna as quests em formato de cards atrasadas"),
            @ApiResponse(responseCode = "401", description = "Não esta logado", content = @Content(schema = @Schema(hidden = true))),
            @ApiResponse(responseCode = "404", description = "Não há quests atrasadas", content = @Content(schema = @Schema(hidden = true))),
            @ApiResponse(responseCode = "500", description = "Erro interno do servidor", content = @Content(schema = @Schema(hidden = true)))
    })
    @Operation(summary = "Retorna as quests em formato de cards atrasadas")
    public List<CardResponseDTO> getAllSubQuestLateAsCards(Authentication authentication) {

        LOGGER.info("m=getAllSubQuestLateAsCards, email={}", authentication.getName());
        List<SubQuest> subQuestList = subQuestsService.findAllSubQuestsLate(authentication.getName());

        return subQuestList.stream().map(CardResponseDTO::new).toList();
    }

    @PatchMapping("/check-sub-quest/")
    @ResponseStatus(code = HttpStatus.OK)
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Quest concluída ou quest desconcluída com sucesso"),
            @ApiResponse(responseCode = "401", description = "Não esta logado", content = @Content(schema = @Schema(hidden = true))),
            @ApiResponse(responseCode = "404", description = "Quest não encontrada", content = @Content(schema = @Schema(hidden = true))),
            @ApiResponse(responseCode = "500", description = "Erro interno do servidor", content = @Content(schema = @Schema(hidden = true)))
    })
    @Operation(summary = "Conclui ou desconclui uma sub quest")
    public void checkAndUncheckSubQuest(String id, Authentication authentication) {
        LOGGER.info("m=doneSubQuest, id={}, email={}", id, authentication.getName());
        subQuestsService.checkAndUncheckSubQuest(id, authentication.getName());
    }

    @DeleteMapping("/delete")
    @ResponseStatus(code = HttpStatus.NO_CONTENT)
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Quest deletada com sucesso"),
            @ApiResponse(responseCode = "401", description = "Não esta logado"),
            @ApiResponse(responseCode = "404", description = "Quest não encontrada"),
            @ApiResponse(responseCode = "500", description = "Erro interno do servidor")
    })
    @Operation(summary = "Deleta uma sub quest")
    public void deleteSubQuestById(@RequestParam String subQuestId, Authentication authentication) {

        LOGGER.info("m=deleteSubQuestById, email={}", authentication.getName());
        subQuestsService.deleteSubQuestById(subQuestId);
    }
}
