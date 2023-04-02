package br.inatel.thisismeapi.services;

import br.inatel.thisismeapi.entities.Quest;

import java.util.List;

public interface QuestService {

    Quest createNewQuest(Quest quest, String email);

    List<Quest> getQuestToday(String email);

    Quest getQuestById(String id, String email);

    void updateQuest(Quest quest, String email);
}
