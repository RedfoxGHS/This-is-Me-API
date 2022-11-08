package br.inatel.thisismeapi.services.impl;

import br.inatel.thisismeapi.entities.Quest;
import br.inatel.thisismeapi.entities.SubQuest;
import br.inatel.thisismeapi.enums.DayOfWeekCustom;
import br.inatel.thisismeapi.exceptions.OnCreateDataException;
import br.inatel.thisismeapi.models.Day;
import br.inatel.thisismeapi.repositories.SubQuestRepository;
import br.inatel.thisismeapi.services.SubQuestsService;
import br.inatel.thisismeapi.utils.WeekCalculatorUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Service
public class SubQuestsServiceImpl implements SubQuestsService {

    private static final Logger LOGGER = LoggerFactory.getLogger(SubQuestsServiceImpl.class);

    @Autowired
    private SubQuestRepository subQuestRepository;

    @Transactional
    public List<SubQuest> createSubQuestByQuest(Quest quest, String email) {

        LOGGER.info("m=createSubQuestByQuest, email={}, questName={}", email, quest.getName());
        List<SubQuest> subQuestList = new ArrayList<>();


        if (quest.getWeek().isEmpty())
            throw new OnCreateDataException("Não pode criar uma tarefa com a semana vazia, por favor, selecione pelo menos um dia da semana!");

        Collections.sort(quest.getWeek());

        for (Day day : quest.getWeek()) {

            LocalDate dayActualInLoop = quest.getStartDate();
            while (DayOfWeekCustom.from(dayActualInLoop) != day.getDayOfWeekCustom()) {
                dayActualInLoop = dayActualInLoop.plusDays(1);
            }

            while (dayActualInLoop.isBefore(quest.getEndDate().plusDays(1))) {
                LocalDateTime start = LocalDateTime.of(dayActualInLoop, LocalTime.parse(day.getStartTime()));
                LocalDateTime end = LocalDateTime.of(dayActualInLoop, LocalTime.parse(day.getEndTime()));

                if (start.isAfter(LocalDateTime.now())) {
                    SubQuest subQuest = new SubQuest();
                    subQuest.setQuest(quest);
                    subQuest.setEmail(email);
                    subQuest.setCheck(false);
                    subQuest.setStart(start);
                    subQuest.setEnd(end);
                    subQuest.setDurationInMin(day.getIntervalInMin());
                    subQuest.setXp(this.calculateXp(day));
                    subQuest.setDayOfWeekEnum(DayOfWeekCustom.from(dayActualInLoop));
                    subQuestList.add(subQuest);
                }

                dayActualInLoop = dayActualInLoop.plusDays(7);
            }

        }
        if (subQuestList.isEmpty())
            throw new OnCreateDataException("Nenhuma tarefa criada com o periodo e os dias da semana selecionados!");

        return subQuestRepository.saveAll(subQuestList);
    }

    @Override
    public List<SubQuest> findAllSubQuestsToday(String email) {

        LOGGER.info("m=findAllSubQuestsToday, email={}", email);
        LocalDateTime start = LocalDateTime.of(LocalDate.now(), LocalTime.MIN);
        LocalDateTime end = LocalDateTime.of(LocalDate.now(), LocalTime.MAX);

        return subQuestRepository.findAllSubQuestsByRangeDate(email, start, end);
    }

    @Override
    public List<SubQuest> findAllSubQuestsWeekly(String email) {

        LocalDate sunday = WeekCalculatorUtils.getSundayFromWeekDate(LocalDate.now());
        LocalDateTime start = LocalDateTime.of(sunday, LocalTime.MIN);
        LocalDateTime end = LocalDateTime.of(sunday.plusDays(6), LocalTime.MAX);

        return subQuestRepository.findAllSubQuestsByRangeDate(email, start, end);
    }

    @Override
    public List<SubQuest> findAllSubQuestsFromNextWeek(String email) {

        LocalDate sunday = WeekCalculatorUtils.getSundayFromWeekDate(LocalDate.now());
        LocalDateTime start = LocalDateTime.of(sunday.plusDays(7), LocalTime.MIN);
        LocalDateTime end = LocalDateTime.of(sunday.plusDays( 13), LocalTime.MAX);

        return subQuestRepository.findAllSubQuestsByRangeDate(email, start, end);
    }

    @Override
    public List<SubQuest> findAllSubQuestsLate(String email) {

        LocalDate yesterday = LocalDate.now().minusDays(1);
        LocalDateTime date = LocalDateTime.of(yesterday, LocalTime.MAX);

        return subQuestRepository.findAllSubQuestNotCheckUntilDate(email, date);
    }


    public void deleteAllSubQuestByEmail(String email) {

        LOGGER.info("m=deleteAllSubQuestByEmail, email={}", email);
        this.subQuestRepository.deleteAllByEmail(email);
    }

    private Long calculateXp(Day day) {

        long interval = day.getIntervalInMin();
        return Math.round(interval / 5.0);
    }
}
