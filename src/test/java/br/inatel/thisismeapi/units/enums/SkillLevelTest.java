package br.inatel.thisismeapi.units.enums;


import br.inatel.thisismeapi.enums.SkillLevel;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;


public class SkillLevelTest {

    @Test
    void testGetLevelName() {
        SkillLevel skillLevel = SkillLevel.BEGINNER_1;
        String expectedLevelName = "Beginner I";
        String actualLevelName = skillLevel.getLevelName();
        assertEquals(expectedLevelName, actualLevelName);
    }

    @Test
    void testGetLevelNameWhenLevelIsIntermediate() {
        SkillLevel skillLevel = SkillLevel.INTERMEDIATE_2;
        String expectedLevelName = "Intermediate II";
        String actualLevelName = skillLevel.getLevelName();
        assertEquals(expectedLevelName, actualLevelName);
    }

    @Test
    void testGetLevelNameWhenLevelIsAdvanced() {
        SkillLevel skillLevel = SkillLevel.ADVANCED_3;
        String expectedLevelName = "Advanced III";
        String actualLevelName = skillLevel.getLevelName();
        assertEquals(expectedLevelName, actualLevelName);
    }

    @Test
    void testGetLevelNameWhenLevelIsExpert() {
        SkillLevel skillLevel = SkillLevel.EXPERT_2;
        String expectedLevelName = "Expert II";
        String actualLevelName = skillLevel.getLevelName();
        assertEquals(expectedLevelName, actualLevelName);
    }

    @Test
    void testGetLevel() {
        SkillLevel skillLevel = SkillLevel.BEGINNER_1;
        Integer expectedLevel = 1;
        Integer actualLevel = skillLevel.getLevel();
        assertEquals(expectedLevel, actualLevel);
    }

    @Test
    void testGetLevelWhenLevelIsIntermediate() {
        SkillLevel skillLevel = SkillLevel.INTERMEDIATE_2;
        Integer expectedLevel = 8;
        Integer actualLevel = skillLevel.getLevel();
        assertEquals(expectedLevel, actualLevel);
    }

    @Test
    void testGetLevelWhenLevelIsAdvanced() {
        SkillLevel skillLevel = SkillLevel.ADVANCED_3;
        Integer expectedLevel = 14;
        Integer actualLevel = skillLevel.getLevel();
        assertEquals(expectedLevel, actualLevel);
    }

    @Test
    void testGetLevelWhenLevelIsExpert() {
        SkillLevel skillLevel = SkillLevel.EXPERT_2;
        Integer expectedLevel = 20;
        Integer actualLevel = skillLevel.getLevel();
        assertEquals(expectedLevel, actualLevel);
    }

    @Test
    void testGetLevelWhenLevelIsMaster() {
        SkillLevel skillLevel = SkillLevel.LEGENDARY;
        Integer expectedLevel = 21;
        Integer actualLevel = skillLevel.getLevel();
        assertEquals(expectedLevel, actualLevel);
    }

    @Test
    void testGetMinXP() {
        SkillLevel skillLevel = SkillLevel.BEGINNER_1;
        Integer expectedMinXP = 0;
        Integer actualMinXP = skillLevel.getMinXP();
        assertEquals(expectedMinXP, actualMinXP);
    }

    @Test
    void testGetMinXPWhenLevelIsIntermediate() {
        SkillLevel skillLevel = SkillLevel.INTERMEDIATE_2;
        Integer expectedMinXP = 729;
        Integer actualMinXP = skillLevel.getMinXP();
        assertEquals(expectedMinXP, actualMinXP);
    }

    @Test
    void testGetMinXPWhenLevelIsAdvanced() {
        SkillLevel skillLevel = SkillLevel.ADVANCED_3;
        Integer expectedMinXP = 2367;
        Integer actualMinXP = skillLevel.getMinXP();
        assertEquals(expectedMinXP, actualMinXP);
    }

    @Test
    void testGetLevelNameByXP() {
        Integer xp = 365;
        String expectedLevelName = "Beginner V";
        String actualLevelName = SkillLevel.getLevelNameByXP(xp);
        assertEquals(expectedLevelName, actualLevelName);
    }

    @Test
    void testGetLevelNameByXPWhenXPIsZero() {
        Integer xp = 0;
        String expectedLevelName = "Beginner I";
        String actualLevelName = SkillLevel.getLevelNameByXP(xp);
        assertEquals(expectedLevelName, actualLevelName);
    }

    @Test
    void testGetLevelNameByXPWhenXPIsLessThanZero() {
        Integer xp = -1;
        String actualLevelName = SkillLevel.getLevelNameByXP(xp);
        assertNull(actualLevelName);
    }

    @Test
    void testGetLevelNameByXPWhenXPIsGreaterThanMaxXP() {
        Integer xp = 100000;
        String expectedLevelName = "Legendary";
        String actualLevelName = SkillLevel.getLevelNameByXP(xp);
        assertEquals(expectedLevelName, actualLevelName);
    }

    @Test
    void testGetLevelByXP() {
        Integer xp = 365;
        Integer expectedLevel = 5;
        Integer actualLevel = SkillLevel.getLevelByXP(xp);
        assertEquals(expectedLevel, actualLevel);
    }

    @Test
    void testGetLevelByXPWhenXPIsZero() {
        Integer xp = 0;
        Integer expectedLevel = 1;
        Integer actualLevel = SkillLevel.getLevelByXP(xp);
        assertEquals(expectedLevel, actualLevel);
    }

    @Test
    void testGetLevelByXPWhenXPIsLessThanZero() {
        Integer xp = -1;
        Integer actualLevel = SkillLevel.getLevelByXP(xp);
        assertNull(actualLevel);
    }

    @Test
    void testGetLevelByXPWhenXPIsGreaterThanMaxXP() {
        Integer xp = 100000;
        Integer expectedLevel = 21;
        Integer actualLevel = SkillLevel.getLevelByXP(xp);
        assertEquals(expectedLevel, actualLevel);
    }

    @Test
    void testgetSkillLevelByLevel() {
        Integer level = 8;
        SkillLevel expectedSkillLevel = SkillLevel.INTERMEDIATE_2;
        SkillLevel actualSkillLevel = SkillLevel.getSkillLevelByLevel(level);
        assertEquals(expectedSkillLevel, actualSkillLevel);
    }

    @Test
    void testgetSkillLevelByLevelWhenLevelIsZero() {
        Integer level = 0;
        SkillLevel actualSkillLevel = SkillLevel.getSkillLevelByLevel(level);
        assertNull(actualSkillLevel);
    }

    @Test
    void testgetSkillLevelByLevelWhenLevelIsLessThanZero() {
        Integer level = -1;
        SkillLevel actualSkillLevel = SkillLevel.getSkillLevelByLevel(level);
        assertNull(actualSkillLevel);
    }

    @Test
    void testgetSkillLevelByLevelWhenLevelIsGreaterThanMaxLevel() {
        Integer level = 22;
        SkillLevel actualSkillLevel = SkillLevel.getSkillLevelByLevel(level);
        assertNull(actualSkillLevel);
    }

    @Test
    void testSkillLevelByXP() {
        Integer xp = 365;
        SkillLevel expectedSkillLevel = SkillLevel.BEGINNER_5;
        SkillLevel actualSkillLevel = SkillLevel.getSkillLevelByXP(xp);
        assertEquals(expectedSkillLevel, actualSkillLevel);
    }

    @Test
    void testSkillLevelByXPWhenXPIsZero() {
        Integer xp = 0;
        SkillLevel expectedSkillLevel = SkillLevel.BEGINNER_1;
        SkillLevel actualSkillLevel = SkillLevel.getSkillLevelByXP(xp);
        assertEquals(expectedSkillLevel, actualSkillLevel);
    }

    @Test
    void testSkillLevelByXPWhenXPIsLessThanZero() {
        Integer xp = -1;
        SkillLevel actualSkillLevel = SkillLevel.getSkillLevelByXP(xp);
        assertNull(actualSkillLevel);
    }

    @Test
    void testSkillLevelByXPWhenXPIsGreaterThanMaxXP() {
        Integer xp = 100000;
        SkillLevel expectedSkillLevel = SkillLevel.LEGENDARY;
        SkillLevel actualSkillLevel = SkillLevel.getSkillLevelByXP(xp);
        assertEquals(expectedSkillLevel, actualSkillLevel);
    }

    @Test
    void testGetLevelByLevelName() {
        String levelName = "Intermediate II";
        Integer expectedLevel = 8;
        Integer actualLevel = SkillLevel.getLevelByLevelName(levelName);
        assertEquals(expectedLevel, actualLevel);
    }

    @Test
    void testGetLevelByLevelNameWhenLevelNameIsNull() {
        String levelName = null;
        Integer actualLevel = SkillLevel.getLevelByLevelName(levelName);
        assertNull(actualLevel);
    }

    @Test
    void testGetLevelByLevelNameWhenLevelNameIsEmpty() {
        String levelName = "";
        Integer actualLevel = SkillLevel.getLevelByLevelName(levelName);
        assertNull(actualLevel);
    }

    @Test
    void testGetLevelByLevelNameWhenLevelNameIsInvalid() {
        String levelName = "Invalid";
        Integer actualLevel = SkillLevel.getLevelByLevelName(levelName);
        assertNull(actualLevel);
    }

    @Test
    void testGetSkillLevelByLevelName() {
        String levelName = "Intermediate II";
        SkillLevel expectedSkillLevel = SkillLevel.INTERMEDIATE_2;
        SkillLevel actualSkillLevel = SkillLevel.getSkillLevelByLevelName(levelName);
        assertEquals(expectedSkillLevel, actualSkillLevel);
    }

    @Test
    void testGetSkillLevelByLevelNameWhenLevelNameIsNull() {
        String levelName = null;
        SkillLevel actualSkillLevel = SkillLevel.getSkillLevelByLevelName(levelName);
        assertNull(actualSkillLevel);
    }

    @Test
    void testGetSkillLevelByLevelNameWhenLevelNameIsEmpty() {
        String levelName = "";
        SkillLevel actualSkillLevel = SkillLevel.getSkillLevelByLevelName(levelName);
        assertNull(actualSkillLevel);
    }

    @Test
    void testGetSkillLevelByLevelNameWhenLevelNameIsInvalid() {
        String levelName = "Invalid";
        SkillLevel actualSkillLevel = SkillLevel.getSkillLevelByLevelName(levelName);
        assertNull(actualSkillLevel);
    }
}
