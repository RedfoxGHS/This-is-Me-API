package br.inatel.thisismeapi.units.services.impl;


import br.inatel.thisismeapi.entities.Character;
import br.inatel.thisismeapi.entities.Skill;
import br.inatel.thisismeapi.exceptions.SkillAlreadyExistsException;
import br.inatel.thisismeapi.repositories.SkillRepository;
import br.inatel.thisismeapi.services.CharacterService;
import br.inatel.thisismeapi.services.impl.SkillServiceImpl;
import br.inatel.thisismeapi.units.classesToTest.EmailConstToTest;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.when;

@SpringBootTest(classes = {SkillServiceImpl.class})
public class SkillServiceImplTests {

    @Autowired
    private SkillServiceImpl skillService;

    @MockBean
    private SkillRepository skillRepository;

    @MockBean
    private CharacterService characterService;

    @Test
    void testGetAllSkills() {

        when(skillRepository.findAllByEmail(any())).thenReturn(this.getInstanceOfSkillListWithSize2());

        List<Skill> skillList = skillService.getAllSkills(EmailConstToTest.EMAIL_DEFAULT);

        assertEquals(2, skillList.size());
        verify(skillRepository).findAllByEmail(any());
    }

    @Test
    void testGetAllSkillsWithEmptyList() {

        when(skillRepository.findAllByEmail(any())).thenReturn(new ArrayList<>());

        List<Skill> skillList = skillService.getAllSkills(EmailConstToTest.EMAIL_DEFAULT);

        assertEquals(0, skillList.size());
        verify(skillRepository).findAllByEmail(any());
    }

    @Test
    void testCreateNewSkillSuccess() {

        Skill skill = new Skill();
        skill.setName("SkillTest");
        skill.setEmail(EmailConstToTest.EMAIL_DEFAULT);

        when(skillRepository.save(any())).thenReturn(skill);
        when(characterService.findCharacterByEmail(any())).thenReturn(new Character());
        Skill skillCreated = skillService.createSkill(skill, EmailConstToTest.EMAIL_DEFAULT);

        assertEquals(skill, skillCreated);
        verify(skillRepository).save(any());
    }

    @Test
    void testCreateNewSkillThrowExceptionWhenSkillNameIsBlank() {

        Skill skill = new Skill();
        skill.setName("");
        skill.setEmail(EmailConstToTest.EMAIL_DEFAULT);

        when(skillRepository.save(any())).thenReturn(skill);

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () ->
                skillService.createSkill(skill, EmailConstToTest.EMAIL_DEFAULT));

        assertEquals("Nome da skill não pode ser vazio", exception.getMessage());
    }

    @Test
    void testCreateNewSkillThrowExceptionWhenSkillAlreadyExists() {

        Skill skill = new Skill();
        skill.setName("SkillTest");
        skill.setEmail(EmailConstToTest.EMAIL_DEFAULT);

        when(skillRepository.save(any())).thenReturn(skill);
        when(skillRepository.findByNameAndEmail(any(), any())).thenReturn(Optional.of(skill));

        SkillAlreadyExistsException exception = assertThrows(SkillAlreadyExistsException.class, () ->
                skillService.createSkill(skill, EmailConstToTest.EMAIL_DEFAULT));

        assertEquals("Skill já existe", exception.getMessage());
    }

    @Test
    public void testCreateSkillThrowExceptionWhenSkillNameHasMoreThanMaximumCharacters(){

        Skill skill = new Skill();
        skill.setName("nome com mais de 20 caracters");
        skill.setEmail(EmailConstToTest.EMAIL_DEFAULT);

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () ->
                skillService.createSkill(skill, EmailConstToTest.EMAIL_DEFAULT));

        assertEquals("Nome da skill não pode ter mais de 20 caracteres", exception.getMessage());
    }

    @Test
    public void testCreateSkillThrowExceptionOnSave(){

            Skill skill = new Skill();
            skill.setName("SkillTest");
            skill.setEmail(EmailConstToTest.EMAIL_DEFAULT);

            when(skillRepository.findByNameAndEmail(any(), any())).thenReturn(Optional.empty());
            when(skillRepository.save(any())).thenThrow(new RuntimeException());

            IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () ->
                    skillService.createSkill(skill, EmailConstToTest.EMAIL_DEFAULT));

            assertEquals("Erro ao salvar skill", exception.getMessage());
    }

    private List<Skill> getInstanceOfSkillListWithSize2() {

        List<Skill> skillList = new ArrayList<>();
        skillList.add(new Skill());
        skillList.add(new Skill());

        return skillList;
    }

}
