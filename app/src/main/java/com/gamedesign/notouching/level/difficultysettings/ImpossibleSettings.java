package com.gamedesign.notouching.level.difficultysettings;

import static com.gamedesign.notouching.util.ScreenInfo.SCALING_FACTOR;

import com.gamedesign.notouching.level.Car;
import com.gamedesign.notouching.util.Assets;

public class ImpossibleSettings extends HardSettings {

    @Override
    public void initCar(Car car, int tilesNumber) {
        int firstBombIndex = level.random.nextInt(tilesNumber - 2) + 1;
        int secondBombIndex = level.random.nextInt(level.TILES_NUMBER - (firstBombIndex + 1)) + (firstBombIndex + 1);

        float [] xCoordinatesOfTileLeftEdge = {
                (this.getXCoordinatesOfTileRightEdge(firstBombIndex) * SCALING_FACTOR),
                (this.getXCoordinatesOfTileRightEdge(secondBombIndex) * SCALING_FACTOR)
        };

        car.initCar(level.game, xCoordinatesOfTileLeftEdge, level, 8f, Assets.terroristChassis, level.ropesBetweenTiles.get(firstBombIndex-1), level.ropesBetweenTiles.get(secondBombIndex-1));
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
