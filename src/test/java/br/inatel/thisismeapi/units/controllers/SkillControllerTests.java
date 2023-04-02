package br.inatel.thisismeapi.units.controllers;

import br.inatel.thisismeapi.controllers.SkillController;
import br.inatel.thisismeapi.controllers.dtos.responses.SkillResponseDTO;
import br.inatel.thisismeapi.entities.Skill;
import br.inatel.thisismeapi.services.SkillService;
import br.inatel.thisismeapi.units.classesToTest.EmailConstToTest;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.core.Authentication;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;

@SpringBootTest(classes = {SkillController.class})
public class SkillControllerTests {

    @Autowired
    private SkillController skillController;

    @MockBean
    private SkillService skillService;

    @Mock
    private Authentication authentication;

    @Test
    void testCreateSkillSuccess(){

        Skill expectedSkill = new Skill();
        when(authentication.getName()).thenReturn(EmailConstToTest.EMAIL_DEFAULT);
        when(skillService.createSkill(any(), anyString())).thenReturn(expectedSkill);

        skillController.createSkill(expectedSkill, authentication);

        verify(skillService).createSkill(expectedSkill, EmailConstToTest.EMAIL_DEFAULT);
    }

    @Test
    void testGetAllSkillsSuccess(){

        Skill expectedSkill = new Skill();
        List<Skill> expectedList = List.of(expectedSkill);

        when(authentication.getName()).thenReturn(EmailConstToTest.EMAIL_DEFAULT);
        when(skillService.getAllSkills(anyString())).thenReturn(expectedList);

        List<SkillResponseDTO> actualList = skillController.getAllSkills(authentication);

        assertEquals(expectedList.get(0).getName(), actualList.get(0).getName());
        verify(skillService).getAllSkills(EmailConstToTest.EMAIL_DEFAULT);
    }

}
