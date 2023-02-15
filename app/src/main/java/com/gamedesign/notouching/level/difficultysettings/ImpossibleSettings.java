package com.gamedesign.notouching.level.difficultysettings;

import static com.gamedesign.notouching.util.ScreenInfo.SCALING_FACTOR;

import com.gamedesign.notouching.level.Car;

public class ImpossibleSettings extends HardSettings {

    @Override
    public Car getCar(int tilesNumber) {
        int firstBombIndex = level.random.nextInt(tilesNumber - 1) + 1;
        int secondBombIndex = level.random.nextInt(level.TILES_NUMBER - firstBombIndex) + firstBombIndex;

        float [] xCoordinatesOfTileLeftEdge = {
                (this.getXCoordinatesOfTileRightEdge(firstBombIndex) * SCALING_FACTOR),
                (this.getXCoordinatesOfTileRightEdge(secondBombIndex) * SCALING_FACTOR)
        };

        return new Car(level.game, xCoordinatesOfTileLeftEdge, level, 5f, level.ropesBetweenTiles.get(firstBombIndex-1), level.ropesBetweenTiles.get(secondBombIndex-1));
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
        return 5;
    }
}
