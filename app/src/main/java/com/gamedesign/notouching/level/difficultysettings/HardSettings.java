package com.gamedesign.notouching.level.difficultysettings;

public class HardSettings extends MediumSettings {

    @Override
    public float getTimeUntilIgnition() {
        return 40;
    }

    @Override
    public int getMinTileLength() {
        return 4;
    }

    @Override
    public int getMaxTileLength() {
        return 6;
    }

    @Override
    public int getRopeBudget() {
        return 12000;
    }

    @Override
    public int getPointsMultiplier() {
        return 10;
    }
}
