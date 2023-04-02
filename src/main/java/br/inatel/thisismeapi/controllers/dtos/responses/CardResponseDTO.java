package br.inatel.thisismeapi.controllers.dtos.responses;

import br.inatel.thisismeapi.entities.Skill;
import br.inatel.thisismeapi.entities.SubQuest;
import br.inatel.thisismeapi.utils.TimeUtils;

import java.io.Serializable;
import java.time.format.DateTimeFormatter;
import java.util.Objects;

public class CardResponseDTO implements Serializable {

    private static DateTimeFormatter FMT = DateTimeFormatter.ofPattern("HH:mm");

    private String questId;

    private String subQuestId;

    private String name;

    private String color;

    private String skill;

    private String date;

    private String startTime;

    private String endTime;

    private String duration;

    private Long xp;

    private Long total;

    private Long finalized;

    private Boolean check;

    public CardResponseDTO(SubQuest subQuest) {
        this.questId = subQuest.getQuest().getQuestId();
        this.subQuestId = subQuest.getSubQuestId();
        this.name = subQuest.getQuest().getName();
        this.color = subQuest.getQuest().getHexColor();
        Skill s = subQuest.getQuest().getSkill();
        this.skill = (s != null) ? s.getName() : "-----";
        this.date = subQuest.getStart().toLocalDate().toString();
        this.startTime = FMT.format(subQuest.getStart());
        this.endTime = FMT.format(subQuest.getEnd());
        this.duration = TimeUtils.getTimeInTextFormat(subQuest.getDurationInMin());
        this.xp = subQuest.getXp();
        this.total = subQuest.getQuest().getTotal();
        this.finalized = subQuest.getQuest().getFinalized();
        this.check = subQuest.isCheck();
    }

    public String getQuestId() {
        return questId;
    }

    public void setQuestId(String questId) {
        this.questId = questId;
    }

    public String getSubQuestId() {
        return subQuestId;
    }

    public void setSubQuestId(String subQuestId) {
        this.subQuestId = subQuestId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public String getSkill() {
        return skill;
    }

    public void setSkill(String skill) {
        this.skill = skill;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public String getEndTime() {
        return endTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }

    public String getDuration() {
        return duration;
    }

    public void setDuration(String duration) {
        this.duration = duration;
    }

    public Long getXp() {
        return xp;
    }

    public void setXp(Long xp) {
        this.xp = xp;
    }

    public Long getTotal() {
        return total;
    }

    public void setTotal(Long total) {
        this.total = total;
    }

    public Long getFinalized() {
        return finalized;
    }

    public void setFinalized(Long finalized) {
        this.finalized = finalized;
    }

    public Boolean getCheck() {
        return check;
    }

    public void setCheck(Boolean check) {
        this.check = check;
    }
}
