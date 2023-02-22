package com.gamedesign.notouching.level.difficultysettings;

import static com.gamedesign.notouching.util.ScreenInfo.SCALING_FACTOR;

import com.gamedesign.notouching.level.Car;
import com.gamedesign.notouching.level.Level;
import com.gamedesign.notouching.util.Assets;

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
        return 5;
    }

    @Override
    public void initCar() {
        int sample = level.random.nextInt(21);
        if(sample == 20){ // bernoulliana dove 1 = 1/20
             initCarWithTwoBombs();
         } else {
             initCarWithOneBomb();
         }

    }

    protected void initCarWithTwoBombs() {
        int firstBombIndex = level.random.nextInt(level.TILES_NUMBER - 2) + 1;
        int secondBombIndex = level.random.nextInt(level.TILES_NUMBER - (firstBombIndex + 1)) + (firstBombIndex + 1);

        float [] xCoordinatesOfTileLeftEdge = {
                (this.getXCoordinatesOfTileRightEdge(firstBombIndex) * SCALING_FACTOR),
                (this.getXCoordinatesOfTileRightEdge(secondBombIndex) * SCALING_FACTOR)
        };

        Level.car.initCar(level.game, xCoordinatesOfTileLeftEdge, level, 7f, Assets.terroristChassis,
                level.ropesBetweenTiles.get(firstBombIndex-1),
                level.ropesBetweenTiles.get(secondBombIndex-1));
    }
}
