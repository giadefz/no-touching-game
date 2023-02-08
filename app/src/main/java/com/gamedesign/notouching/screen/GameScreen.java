package com.gamedesign.notouching.screen;

import android.graphics.Color;

import com.gamedesign.notouching.Car;
import com.gamedesign.notouching.Level;
import com.gamedesign.notouching.component.BoxDrawable;
import com.gamedesign.notouching.component.ComponentType;
import com.gamedesign.notouching.component.GameObject;
import com.gamedesign.notouching.component.PixmapDrawable;
import com.gamedesign.notouching.component.Position;
import com.gamedesign.notouching.framework.Game;
import com.gamedesign.notouching.framework.Graphics;
import com.gamedesign.notouching.framework.Input;
import com.gamedesign.notouching.framework.Screen;
import com.gamedesign.notouching.util.Assets;
import com.gamedesign.notouching.util.GameObjects;
import com.google.fpl.liquidfun.DistanceJointDef;
import com.google.fpl.liquidfun.Joint;
import com.google.fpl.liquidfun.RopeJointDef;

import java.util.List;

public class GameScreen extends Screen {

    public static final float TILE_ROPE_LENGTH = 1.2f;
    Level level;
    private static final int TILES_NUMBER = 7;
    private static final int VELOCITY_ITERATIONS = 8;
    private static final int POSITION_ITERATIONS = 3;
    private static final int PARTICLE_ITERATIONS = 3;
    private static final float starting_tile_positionX = 8;
    private static final float starting_tile_positionY = 17;
    private static final float pierDistance = 55;
    private boolean RUNNING;

    public GameScreen(Game game) {
        super(game);
        this.level = new Level(game.getGraphics());
        GameObject firstTile = level.addGameObject(Assets.gameObjectsJSON.getGameObject(GameObjects.TILE, game));
        for (int i = 1; i < TILES_NUMBER; i++) {
            GameObject tile = Assets.gameObjectsJSON.getGameObject(GameObjects.TILE, game);
            PixmapDrawable component = tile.<PixmapDrawable>getComponent(ComponentType.Drawable);
            tile.setPosition(starting_tile_positionX + i * (component.width + 1), starting_tile_positionY);
            tile = level.addGameObject(tile);
            level.addRopeBetweenTiles(insertRopeBetweenTiles(game, firstTile, tile, component));
            firstTile = tile;
        }
        setPiers(game);
        level.addGameObject(Assets.gameObjectsJSON.getGameObject(GameObjects.BOTTOM, game));
        level.addCar(new Car(game));
    }

    private void setPiers(Game game) {
        GameObject firstPier = level.addGameObject(Assets.gameObjectsJSON.getGameObject(GameObjects.PIER, game));
        GameObject secondPier = Assets.gameObjectsJSON.getGameObject(GameObjects.PIER, game);
        GameObject firstTile = level.getGameObject(0);
        PixmapDrawable firstTileDrawable = firstTile.getComponent(ComponentType.Drawable);
        GameObject lastTile = level.getGameObject(TILES_NUMBER - 1);
        Position firstPierPosition = firstPier.getComponent(ComponentType.Position);
        secondPier.setPosition(firstPierPosition.x + pierDistance, firstPierPosition.y);
        secondPier = level.addGameObject(secondPier);
        setUpRopeBetweenPierAndTile(firstPier, firstTile);
        setUpRopeBetweenSecondPierAndTile(secondPier, lastTile);
    }

    private void setUpRopeBetweenPierAndTile(GameObject pier, GameObject tile) {
        BoxDrawable pierDrawable = pier.getComponent(ComponentType.Drawable);
        PixmapDrawable tileDrawable = tile.getComponent(ComponentType.Drawable);
        RopeJointDef pierRope = new RopeJointDef();
        pierRope.setBodyA(pier.getBody());
        pierRope.setBodyB(tile.getBody());
        pierRope.setLocalAnchorA(0, 0);
        pierRope.setLocalAnchorB(-tileDrawable.width / 2, tileDrawable.height / 2);
        pierRope.setCollideConnected(true);
        pierRope.setMaxLength(3f);
        level.setFirstRopeFromPier(game.getWorld().createJoint(pierRope));
    }

    private void setUpRopeBetweenSecondPierAndTile(GameObject pier, GameObject tile) {
        BoxDrawable pierDrawable = pier.getComponent(ComponentType.Drawable);
        PixmapDrawable tileDrawable = tile.getComponent(ComponentType.Drawable);
        RopeJointDef pierRope = new RopeJointDef();
        pierRope.setBodyA(pier.getBody());
        pierRope.setBodyB(tile.getBody());
        pierRope.setLocalAnchorA(0, 0);
        pierRope.setLocalAnchorB(tileDrawable.width / 2, tileDrawable.height / 2);
        pierRope.setCollideConnected(true);
        pierRope.setMaxLength(3f);
        level.setSecondRopeFromPier(game.getWorld().createJoint(pierRope));
    }

    private Joint insertRopeBetweenTiles(Game game, GameObject firstTile, GameObject tile, PixmapDrawable component) {
        DistanceJointDef jointDef = new DistanceJointDef();
        jointDef.setBodyA(firstTile.getBody());
        jointDef.setBodyB(tile.getBody());
        jointDef.setLocalAnchorA(component.width, component.height / 2);
        jointDef.setLocalAnchorB(0, component.height / 2);
        jointDef.setLength(TILE_ROPE_LENGTH);
        jointDef.setCollideConnected(true);
        return game.getWorld().createDistanceJoint(jointDef);
    }

    @Override
    public void update(float deltaTime) {
        if (RUNNING) {
            List<Input.TouchEvent> touchEvents = game.getInput().getTouchEvents();
            if (touchEvents.size() > 0) {
                touchEvents.forEach(te -> {
                    if(te.type == Input.TouchEvent.TOUCH_DOWN){
                        level.startingPointCoordinates.setX(te.x);
                        level.startingPointCoordinates.setY(te.y);
                    }else if(te.type == Input.TouchEvent.TOUCH_DRAGGED){
                        level.newRopeCoordinates.setX(te.x);
                        level.newRopeCoordinates.setY(te.y);
                    }
                    else if(te.type == Input.TouchEvent.TOUCH_UP){
                        level.newRopeCoordinates.setX(0);
                        level.newRopeCoordinates.setY(0);
                    }
                });

            }
            Graphics g = this.game.getGraphics();
            g.clear(Color.argb(255, 0, 0, 0));
            game.getWorld().step(1/60f, VELOCITY_ITERATIONS, POSITION_ITERATIONS, PARTICLE_ITERATIONS);
            level.render();
        }
    }

    @Override
    public void present(float deltaTime) {
        RUNNING = true;
    }

    @Override
    public void pause() {
        RUNNING = false;
    }

    @Override
    public void resume() {
        RUNNING = true;
    }

    @Override
    public void dispose() {
        this.game.getWorld().delete();
    }
}
