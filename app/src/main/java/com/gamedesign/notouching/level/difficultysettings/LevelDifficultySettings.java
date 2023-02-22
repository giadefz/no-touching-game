package com.gamedesign.notouching.level.difficultysettings;

import com.gamedesign.notouching.level.Car;

public interface LevelDifficultySettings {

    int getMinTileLength();
    int getMaxTileLength();
    int getRopeBudget();
    float getTimeUntilIgnition();

    int getPointsMultiplier();

    void initCar();
}
