package br.inatel.thisismeapi.controllers.dtos.responses;

import br.inatel.thisismeapi.entities.Skill;

import java.io.Serializable;

public class SkillResponseDTO implements Serializable {

    private String id;
    private String name;
    private String level;
    private Integer minXp;
    private Integer maxXp;
    private Integer currentXp;
    private Integer percentage;

    public SkillResponseDTO(Skill skill) {
        this.id = skill.getSkillId();
        this.name = skill.getName();
        this.level = skill.getLevel().getLevelName();
        this.minXp = skill.getLevel().getMinXP();
        this.maxXp = skill.getLevel().getMaxXP();
        this.currentXp = skill.getXp().intValue();
        this.percentage = skill.getPercentage();
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

    public String getLevel() {
        return level;
    }

    public void setLevel(String level) {
        this.level = level;
    }

    public Integer getMinXp() {
        return minXp;
    }

    public void setMinXp(Integer minXp) {
        this.minXp = minXp;
    }

    public Integer getMaxXp() {
        return maxXp;
    }

    public void setMaxXp(Integer maxXp) {
        this.maxXp = maxXp;
    }

    public Integer getCurrentXp() {
        return currentXp;
    }

    public void setCurrentXp(Integer currentXp) {
        this.currentXp = currentXp;
    }

    public Integer getPercentage() {
        return percentage;
    }

    public void setPercentage(Integer percentage) {
        this.percentage = percentage;
    }
}

