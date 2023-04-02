package br.inatel.thisismeapi.services;

import br.inatel.thisismeapi.entities.Quest;
import br.inatel.thisismeapi.entities.SubQuest;
import br.inatel.thisismeapi.exceptions.OnCreateSubQuestException;

import java.util.List;

public interface SubQuestsService {

    List<SubQuest> createSubQuestByQuest(Quest quest, String email) throws OnCreateSubQuestException;

    List<SubQuest> findAllSubQuestsToday(String email);

    List<SubQuest> findAllSubQuestsWeekly(String email);

    List<SubQuest> findAllSubQuestsFromNextWeek(String email);

    List<SubQuest> findAllSubQuestsLate(String email);

    void deleteSubQuestById(String subQuestId);

    SubQuest checkAndUncheckSubQuest(String id, String email);
}
