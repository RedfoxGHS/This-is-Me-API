package br.inatel.thisismeapi.services;

import br.inatel.thisismeapi.entities.Character;

public interface CharacterService {

    Character findCharacterByEmail(String email);

    Character saveNewCharacter(Character character);

    Character updateCharacter(Character character);

    Character setClothes(String email, Long number);
}
