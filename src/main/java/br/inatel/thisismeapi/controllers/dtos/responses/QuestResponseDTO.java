package br.inatel.thisismeapi.controllers.dtos.responses;

import br.inatel.thisismeapi.entities.Quest;
import br.inatel.thisismeapi.entities.Skill;

import java.io.Serializable;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Objects;

public class QuestResponseDTO implements Serializable {

    private static DateTimeFormatter FMT = DateTimeFormatter.ofPattern("dd/MM/yyyy");

    private String id;
    private String name;

    private String desc;

    private String hexColor;

    private String skill;

    private String subQuests;

    private String status;

    private String period;

    private String xp;

    private List<DayResponseDTO> week;

    public QuestResponseDTO(Quest quest) {
        this.id = quest.getQuestId();
        this.name = quest.getName();
        this.desc = quest.getDesc();
        this.hexColor = quest.getHexColor();
        Skill s = quest.getSkill();
        this.skill = (s != null) ? s.getName() : "None";
        this.subQuests = quest.getFinalized() + "/" + quest.getTotal();
        this.status = quest.getStatus().toString();
        this.period = FMT.format(quest.getStartDate()) + " - " + FMT.format(quest.getEndDate());
        this.xp = quest.getXpGained() + "/" + quest.getTotalXp();
        this.week = quest.getWeek().stream().map(DayResponseDTO::new).toList();
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public String getHexColor() {
        return hexColor;
    }

    public void setHexColor(String hexColor) {
        this.hexColor = hexColor;
    }

    public String getSkill() {
        return skill;
    }

    public void setSkill(String skill) {
        this.skill = skill;
    }

    public String getSubQuests() {
        return subQuests;
    }

    public void setSubQuests(String subQuests) {
        this.subQuests = subQuests;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getXp() {
        return xp;
    }

    public void setXp(String xp) {
        this.xp = xp;
    }

    public List<DayResponseDTO> getWeek() {
        return week;
    }

    public void setWeek(List<DayResponseDTO> week) {
        this.week = week;
    }

    public String getPeriod() {
        return period;
    }

    public void setPeriod(String period) {
        this.period = period;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        QuestResponseDTO that = (QuestResponseDTO) o;
        return Objects.equals(id, that.id) && Objects.equals(name, that.name) && Objects.equals(desc, that.desc) && Objects.equals(hexColor, that.hexColor) && Objects.equals(skill, that.skill) && Objects.equals(subQuests, that.subQuests) && Objects.equals(status, that.status) && Objects.equals(period, that.period) && Objects.equals(xp, that.xp) && Objects.equals(week, that.week);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, desc, hexColor, skill, subQuests, status, period, xp, week);
    }
}
