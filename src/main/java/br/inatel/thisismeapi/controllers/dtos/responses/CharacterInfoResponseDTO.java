package br.inatel.thisismeapi.controllers.dtos.responses;

import br.inatel.thisismeapi.entities.Character;

import java.io.Serializable;

public class CharacterInfoResponseDTO implements Serializable {

    private String id;
    private String characterName;
    private Long clothes;
    private Long xp;
    private Long level;

    public CharacterInfoResponseDTO(Character character) {
        this.id = character.getId();
        this.characterName = character.getCharacterName();
        this.clothes = character.getNumberClothes();
        this.xp = character.getXp();
        this.level = character.getLevel();
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Long getClothes() {
        return clothes;
    }

    public void setClothes(Long clothes) {
        this.clothes = clothes;
    }

    public String getCharacterName() {
        return characterName;
    }

    public void setCharacterName(String characterName) {
        this.characterName = characterName;
    }

    public Long getXp() {
        return xp;
    }

    public void setXp(Long xp) {
        this.xp = xp;
    }

    public Long getLevel() {
        return level;
    }

    public void setLevel(Long level) {
        this.level = level;
    }
}
