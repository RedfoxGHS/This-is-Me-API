package br.inatel.thisismeapi.entities;

import br.inatel.thisismeapi.enums.SkillLevel;
import io.swagger.v3.oas.annotations.media.Schema;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Document
public class Skill {

    @Id
    @Schema(hidden = true)
    private String skillId;

    @Schema(hidden = true)
    private String email;

    @NotBlank(message = "Nome da habilidade não pode ser vazio")
    @NotNull(message = "Nome da habilidade não pode ser nulo")
    @Size(max = 20, message = "Nome da habilidade deve ter no máximo 20 caracteres")
    @Schema(example = "Java", description = "Name of the skill", required = true, maxLength = 20)
    private String name;

    @Schema(hidden = true)
    private Long xp;

    @Schema(hidden = true)
    private SkillLevel level;


    public Skill() {
        this.name = "";
        this.xp = 0L;
        this.level = SkillLevel.getSkillLevelByXP(this.xp.intValue());
    }

    public String getSkillId() {
        return skillId;
    }

    public void setSkillId(String skillId) {
        this.skillId = skillId;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Long getXp() {
        return xp;
    }

    public void addXp(Long xp) {
        this.xp += xp;
        this.level = SkillLevel.getSkillLevelByXP(this.xp.intValue());
    }

    public void removeXp(Long xp) {
        this.xp -= xp;
        this.level = SkillLevel.getSkillLevelByXP(this.xp.intValue());
    }

    public SkillLevel getLevel() {
        return level;
    }

    @Schema(hidden = true)
    public Integer getPercentage() {
        return (int) ((this.xp - this.level.getMinXP()) * 100 / (this.level.getMaxXP() - this.level.getMinXP()));
    }
}
