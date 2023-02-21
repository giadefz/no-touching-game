package com.gamedesign.notouching.level.difficultysettings;

import com.gamedesign.notouching.level.Car;

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
        return 9;
    }

    @Override
    public int getRopeBudget() {
        return 12000;
    }

    @Override
    public int getPointsMultiplier() {
        return 10;
    }

    @Override
    public void initCar(Car car, int tilesNumber) {
        if(level.random.nextInt(11) == 10){ // 1 in 50 chance to spawn two bombs
            initCarWithTwoBombs(car, level.TILES_NUMBER);
        } else {
            initCarWithOneBomb(car, level.TILES_NUMBER);
        }
    }
}
