package com.gamedesign.notouching.level;

public class LevelDifficultySettingsFactory {

    private static final EasySettings EASY = new EasySettings();
    private static final MediumSettings MEDIUM = new MediumSettings();
    private static final HardSettings HARD = new HardSettings();
    private static final ImpossibleSettings IMPOSSIBLE = new ImpossibleSettings();

    public static LevelDifficultySettings getDifficultySettings(Level level){
        if(level.levelNumber < 3){
            EASY.level = level;
            return EASY;
        } else if (level.levelNumber < 6){
            MEDIUM.level = level;
            return MEDIUM;
        } else if (level.levelNumber < 9){
            HARD.level = level;
            return HARD;
        } else {
            IMPOSSIBLE.level = level;
            return IMPOSSIBLE;
        }
    }


}
