package com.gamedesign.notouching.level;

public class MediumSettings extends EasySettings {

    @Override
    public int getMinTileLength() {
        return 6;
    }

    @Override
    public int getMaxTileLength() {
        return 8;
    }

    @Override
    public int getRopeBudget() {
        return 14000;
    }

    @Override
    public int getPointsMultiplier() {
        return 2;
    }
}
