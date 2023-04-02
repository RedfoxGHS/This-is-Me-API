package br.inatel.thisismeapi.enums;

public enum SkillLevel {

    BEGINNER_1("Beginner I", 1, 0, 26),
    BEGINNER_2("Beginner II", 2, 27, 78),
    BEGINNER_3("Beginner III", 3, 79, 156),
    BEGINNER_4("Beginner IV", 4, 157, 260),
    BEGINNER_5("Beginner V", 5, 261, 390),
    BEGINNER_6("Beginner VI", 6, 391, 546),

    INTERMEDIATE_1("Intermediate I", 7, 547, 728),
    INTERMEDIATE_2("Intermediate II", 8, 729, 936),
    INTERMEDIATE_3("Intermediate III", 9, 937, 1170),
    INTERMEDIATE_4("Intermediate IV", 10, 1171, 1430),
    INTERMEDIATE_5("Intermediate V", 11, 1431, 1716),

    ADVANCED_1("Advanced I", 12, 1717, 2028),
    ADVANCED_2("Advanced II", 13, 2029, 2366),
    ADVANCED_3("Advanced III", 14, 2367, 2730),
    ADVANCED_4("Advanced IV", 15, 2731, 3120),

    ESPECIALIST_1("Especialist I", 16, 3121, 3536),
    ESPECIALIST_2("Especialist II", 17, 3537, 3978),
    ESPECIALIST_3("Especialist III", 18, 3979, 4446),

    EXPERT_1("Expert I", 19, 4447, 4940),
    EXPERT_2("Expert II", 20, 4941, 5460),

    LEGENDARY("Legendary", 21, 5461, 999999999);


    private String levelName;

    private Integer level;

    private Integer minXP;

    private Integer maxXP;


    SkillLevel(String levelName, Integer level, Integer minXP, Integer maxXP) {
        this.levelName = levelName;
        this.level = level;
        this.minXP = minXP;
        this.maxXP = maxXP;
    }

    public String getLevelName() {
        return levelName;
    }

    public Integer getLevel() {
        return level;
    }

    public Integer getMinXP() {
        return minXP;
    }

    public Integer getMaxXP() {
        return maxXP;
    }

    public static SkillLevel getSkillLevelByXP(Integer xp) {
        for (SkillLevel skillLevel : SkillLevel.values()) {
            if (xp >= skillLevel.getMinXP() && xp <= skillLevel.getMaxXP()) {
                return skillLevel;
            }
        }
        return null;
    }

    public static SkillLevel getSkillLevelByLevel(Integer level) {
        for (SkillLevel skillLevel : SkillLevel.values()) {
            if (skillLevel.getLevel().equals(level)) {
                return skillLevel;
            }
        }
        return null;
    }

    public static SkillLevel getSkillLevelByLevelName(String levelName) {
        for (SkillLevel skillLevel : SkillLevel.values()) {
            if (skillLevel.getLevelName().equals(levelName)) {
                return skillLevel;
            }
        }
        return null;
    }

    public static String getLevelNameByXP(Integer xp) {
        for (SkillLevel skillLevel : SkillLevel.values()) {
            if (xp >= skillLevel.getMinXP() && xp <= skillLevel.getMaxXP()) {
                return skillLevel.getLevelName();
            }
        }
        return null;
    }

    public static Integer getLevelByXP(Integer xp) {
        for (SkillLevel skillLevel : SkillLevel.values()) {
            if (xp >= skillLevel.getMinXP() && xp <= skillLevel.getMaxXP()) {
                return skillLevel.getLevel();
            }
        }
        return null;
    }

    public static Integer getLevelByLevelName(String levelName) {
        for (SkillLevel skillLevel : SkillLevel.values()) {
            if (skillLevel.getLevelName().equals(levelName)) {
                return skillLevel.getLevel();
            }
        }
        return null;
    }
}
