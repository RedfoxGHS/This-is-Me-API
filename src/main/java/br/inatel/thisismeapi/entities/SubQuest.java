package br.inatel.thisismeapi.entities;

import br.inatel.thisismeapi.enums.DayOfWeekCustom;
import io.swagger.v3.oas.annotations.media.Schema;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;

@Document
public class SubQuest {

    @Id
    @Schema(hidden = true)
    private String subQuestId;

    @DBRef
    private Quest quest;

    private String email;

    private Boolean check;

    DayOfWeekCustom dayOfWeekCustom;

    private LocalDateTime start;

    private LocalDateTime end;

    private Long durationInMin;

    private Long xp;

    public String getSubQuestId() {
        return subQuestId;
    }

    public Quest getQuest() {
        return quest;
    }

    public void setQuest(Quest quest) {
        this.quest = quest;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setCheck(Boolean check) {
        this.check = check;
    }

    public DayOfWeekCustom getDayOfWeekEnum() {
        return dayOfWeekCustom;
    }

    public void setDayOfWeekEnum(DayOfWeekCustom dayOfWeekCustom) {
        this.dayOfWeekCustom = dayOfWeekCustom;
    }

    public LocalDateTime getStart() {
        return start;
    }

    public void setStart(LocalDateTime start) {
        this.start = start;
    }

    public LocalDateTime getEnd() {
        return end;
    }

    public void setEnd(LocalDateTime end) {
        this.end = end;
    }

    public Long getDurationInMin() {
        return durationInMin;
    }

    public void setDurationInMin(Long durationInMin) {
        this.durationInMin = durationInMin;
    }

    public Long getXp() {
        return xp;
    }

    public void setXp(Long xp) {
        this.xp = xp;
    }

    public boolean isCheck() {
        return check;
    }
}
