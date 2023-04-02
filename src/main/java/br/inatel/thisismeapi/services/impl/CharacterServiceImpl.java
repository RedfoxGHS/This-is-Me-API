package br.inatel.thisismeapi.services.impl;

import br.inatel.thisismeapi.entities.Character;
import br.inatel.thisismeapi.enums.Clothes;
import br.inatel.thisismeapi.exceptions.UnregisteredUserException;
import br.inatel.thisismeapi.exceptions.mongo.UniqueViolationConstraintException;
import br.inatel.thisismeapi.repositories.CharacterRepository;
import br.inatel.thisismeapi.services.CharacterService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class CharacterServiceImpl implements CharacterService {

    private static final Logger LOGGER = LoggerFactory.getLogger(CharacterServiceImpl.class);

    @Autowired
    private CharacterRepository characterRepository;

    @Override
    public Character findCharacterByEmail(String email) {

        LOGGER.info("m=findCharacterByEmail, email={}", email);
        Optional<Character> characterOptional = characterRepository.findCharacterByEmail(email);

        if (characterOptional.isEmpty()) {
            throw new UnregisteredUserException("Personagem com email [" + email + "] não encontrado!");
        }

        return characterOptional.get();
    }

    @Override
    public Character saveNewCharacter(Character character) {

        LOGGER.info("m=saveNewCharacter, characterName={}", character.getCharacterName());
        try {
            return characterRepository.save(character);
        } catch (DuplicateKeyException e) {
            throw new UniqueViolationConstraintException("Já Existe uma conta cadastrada com esse e-mail!");
        }
    }

    @Override
    public Character updateCharacter(Character character) {

        LOGGER.info("m=updateCharacter, characterName={}", character.getCharacterName());
        if (character.getId() == null) {
            throw new UnregisteredUserException("Usuário não tem um personagem criado!");
        }
        return characterRepository.save(character);
    }

    @Override
    public Character setClothes(String email, Long numberClothes) {

        LOGGER.info("m=setClothes, email={}, numberClothes={}", email, numberClothes);

        Clothes clothes = Clothes.findById(numberClothes);
        Character character = this.findCharacterByEmail(email);

        character.setNumberClothes(clothes.getNumber());
        return characterRepository.save(character);
    }

    public void deleteCharacterById(String id) {
        characterRepository.deleteById(id);
    }

    public void deleteCharacterByEmail(String id) {
        characterRepository.deleteCharacterByEmail(id);
    }
}
