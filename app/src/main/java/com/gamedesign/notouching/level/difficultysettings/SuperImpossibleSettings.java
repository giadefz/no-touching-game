package com.gamedesign.notouching.level.difficultysettings;

public class SuperImpossibleSettings extends ImpossibleSettings {


    @Override
    public float getTimeUntilIgnition() {
        return 15;
    }

    @Override
    public int getRopeBudget() {
        return 8000;
    }

    @Override
    public int getPointsMultiplier() {
        return 50;
    }
}
