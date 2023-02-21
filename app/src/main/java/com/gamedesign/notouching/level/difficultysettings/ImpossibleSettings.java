package com.gamedesign.notouching.level.difficultysettings;

import static com.gamedesign.notouching.util.ScreenInfo.SCALING_FACTOR;

import com.gamedesign.notouching.level.Car;
import com.gamedesign.notouching.util.Assets;

public class ImpossibleSettings extends HardSettings {

    @Override
    public void initCar(Car car, int tilesNumber) {
        initCarWithTwoBombs(car, tilesNumber);
    }



    @Override
    public float getTimeUntilIgnition() {
        return 20;
    }

    @Override
    public int getRopeBudget() {
        return 10000;
    }

    @Override
    public int getPointsMultiplier() {
        return 20;
    }
}
