package com.gamedesign.notouching.level.difficultysettings;

import static com.gamedesign.notouching.util.ScreenInfo.SCALING_FACTOR;

import com.gamedesign.notouching.component.ComponentType;
import com.gamedesign.notouching.component.GameObject;
import com.gamedesign.notouching.component.PixmapDrawable;
import com.gamedesign.notouching.level.Car;
import com.gamedesign.notouching.level.Level;
import com.gamedesign.notouching.util.Assets;
import com.google.fpl.liquidfun.Vec2;

public class EasySettings implements LevelDifficultySettings {

    protected Level level;
    private final Vec2 temp = new Vec2();

    @Override
    public int getMinTileLength() {
        return 8;
    }

    @Override
    public int getMaxTileLength() {
        return 8;
    }

    @Override
    public int getRopeBudget() {
        return 20000;
    }

    @Override
    public float getTimeUntilIgnition() {
        return 60;
    }

    @Override
    public int getPointsMultiplier() {
        return 1;
    }

    @Override
    public void initCar(Car car, int tilesNumber) {
        int firstBombIndex = level.random.nextInt(tilesNumber - 1) + 1;

        float [] xCoordinatesOfTileLeftEdge = {(this.getXCoordinatesOfTileRightEdge(firstBombIndex) * SCALING_FACTOR)};
        car.initCar(level.game, xCoordinatesOfTileLeftEdge, level, 5f, Assets.terroristChassis, level.ropesBetweenTiles.get(firstBombIndex-1));
    }

    protected float getXCoordinatesOfTileRightEdge(int tileNumber){
        GameObject tile = level.gameObjects.get(tileNumber);
        PixmapDrawable component = tile.getComponent(ComponentType.Drawable);
        temp.setX(component.width / 2);
        temp.setY(0);
        return tile.getBody().getWorldPoint(temp).getX();
    }

    public void setLevel(Level level) {
        this.level = level;
    }

}
