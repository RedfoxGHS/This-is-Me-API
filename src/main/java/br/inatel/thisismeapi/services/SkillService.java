package br.inatel.thisismeapi.services;

import br.inatel.thisismeapi.entities.Skill;

import java.util.List;

public interface SkillService {

    Skill createSkill(Skill skill, String email);

    List<Skill> getAllSkills(String email);
}
