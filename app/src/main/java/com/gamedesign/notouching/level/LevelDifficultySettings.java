package com.gamedesign.notouching.level;

import com.gamedesign.notouching.framework.Game;

public interface LevelDifficultySettings {

    int getMinTileLength();
    int getMaxTileLength();
    int getRopeBudget();
    float getTimeUntilIgnition();

    int getPointsMultiplier();

    Car getCar(int tilesNumber);
}
