package com.gamedesign.notouching.screen;

import android.graphics.Color;
import android.util.Log;

import com.gamedesign.notouching.Car;
import com.gamedesign.notouching.Level;
import com.gamedesign.notouching.R;
import com.gamedesign.notouching.Rope;
import com.gamedesign.notouching.component.BoxDrawable;
import com.gamedesign.notouching.component.Component;
import com.gamedesign.notouching.component.ComponentType;
import com.gamedesign.notouching.component.Drawable;
import com.gamedesign.notouching.component.GameObject;
import com.gamedesign.notouching.component.PixmapDrawable;
import com.gamedesign.notouching.component.Position;
import com.gamedesign.notouching.framework.Game;
import com.gamedesign.notouching.framework.Graphics;
import com.gamedesign.notouching.framework.Input;
import com.gamedesign.notouching.framework.Screen;
import com.gamedesign.notouching.util.Assets;
import com.gamedesign.notouching.util.GameObjects;
import com.gamedesign.notouching.util.ScreenInfo;
import com.google.fpl.liquidfun.DistanceJointDef;
import com.google.fpl.liquidfun.Joint;
import com.google.fpl.liquidfun.RopeJointDef;
import com.google.fpl.liquidfun.Vec2;

import java.util.List;

public class GameScreen extends Screen {

    public static final float TILE_ROPE_LENGTH = 1.2f;
    public static final int HALF_SCREEN = 960;
    public static final float PIER_HALF_HEIGHT = -12.775f;
    Level level;
    private static final int TILES_NUMBER = 7;
    private static final int VELOCITY_ITERATIONS = 8;
    private static final int POSITION_ITERATIONS = 3;
    private static final int PARTICLE_ITERATIONS = 3;
    private static final float starting_tile_positionX = 8;
    private static final float starting_tile_positionY = 17;
    private static final float pierDistance = 55;
    private static final Vec2 startingRopeFromFirstPier = new Vec2(64, 169);
    private boolean RUNNING;

    public GameScreen(Game game) {
        super(game);
        this.level = new Level(game.getGraphics());
        GameObject firstTile = level.addGameObject(Assets.gameObjectsJSON.getGameObject(GameObjects.TILE, game));
        for (int i = 1; i < TILES_NUMBER; i++) {
            GameObject tile = Assets.gameObjectsJSON.getGameObject(GameObjects.TILE, game);
            PixmapDrawable component = tile.getComponent(ComponentType.Drawable);
            tile.setPosition(starting_tile_positionX + i * (component.width + 1), starting_tile_positionY);
            tile = level.addGameObject(tile);
            level.addRopeBetweenTiles(insertRopeBetweenTiles(game, firstTile, tile, component));
            firstTile = tile;
        }
        setPiers(game);
//        level.addGameObject(Assets.gameObjectsJSON.getGameObject(GameObjects.BOTTOM, game));
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
        RopeJointDef jointDef = new RopeJointDef();
        jointDef.setBodyA(firstTile.getBody());
        jointDef.setBodyB(tile.getBody());
        jointDef.setLocalAnchorA(component.width, component.height / 2);
        jointDef.setLocalAnchorB(0, component.height / 2);
        jointDef.setMaxLength(TILE_ROPE_LENGTH);
        jointDef.setCollideConnected(true);
        return game.getWorld().createJoint(jointDef);
    }

    private Rope createNewRope(Input.TouchEvent event, GameObject destinationTile){
        RopeJointDef jointDef = new RopeJointDef();
        if(level.startingPointCoordinates.getX() > HALF_SCREEN)
            jointDef.setBodyA(level.getGameObject(8).getBody());
        else
            jointDef.setBodyA(level.getGameObject(7).getBody());
        jointDef.setBodyB(destinationTile.getBody());
        jointDef.setLocalAnchorA(0, PIER_HALF_HEIGHT);
        Drawable drawable = destinationTile.getComponent(ComponentType.Drawable);
        Vec2 localCoordinatesFromWorldCoordinates = drawable.getLocalCoordinatesFromWorldCoordinates(event.x, event.y);
        jointDef.setLocalAnchorB(localCoordinatesFromWorldCoordinates.getX(), localCoordinatesFromWorldCoordinates.getY());
        float xDiff = event.x - level.startingPointCoordinates.getX();
        float yDiff = event.y - level.startingPointCoordinates.getY();
        jointDef.setMaxLength((((float) Math.sqrt(xDiff * xDiff + yDiff * yDiff)) / ScreenInfo.SCALING_FACTOR) + 0.2f);
        jointDef.setCollideConnected(true);
        Joint joint = game.getWorld().createJoint(jointDef);
        return new Rope(joint, localCoordinatesFromWorldCoordinates.getX(), localCoordinatesFromWorldCoordinates.getY());
    }

    @Override
    public void update(float deltaTime) {
        if (RUNNING) {
            List<Input.TouchEvent> touchEvents = game.getInput().getTouchEvents();
            if (touchEvents.size() > 0) {
                touchEvents.forEach(te -> {
                    if (te.type == Input.TouchEvent.TOUCH_DOWN) {
                        if(te.x > HALF_SCREEN)
                            level.startingPointCoordinates.setX(1823);
                        else
                            level.startingPointCoordinates.setX(64);
                        level.startingPointCoordinates.setY(169);
                        level.newRopeCoordinates.setX(te.x);
                        level.newRopeCoordinates.setY(te.y);
                    } else if (te.type == Input.TouchEvent.TOUCH_DRAGGED) {
                        level.newRopeCoordinates.setX(te.x);
                        level.newRopeCoordinates.setY(te.y);
                    } else if (te.type == Input.TouchEvent.TOUCH_UP) {
                        level.newRopeCoordinates.setX(0);
                        level.newRopeCoordinates.setY(0);
                        GameObject gameObjectWithinBound = level.getGameObjectWithinBound(te);
                        if(gameObjectWithinBound != null){
                            Log.println(Log.ASSERT, "TOUCH", gameObjectWithinBound.toString());
                            Rope newRope = createNewRope(te, gameObjectWithinBound);
                            level.addNewRope(newRope);
                        }
                    }
                });

            }
            Graphics g = this.game.getGraphics();
            g.clear(Color.argb(255, 0, 0, 0));
            game.getWorld().step(1 / 60f, VELOCITY_ITERATIONS, POSITION_ITERATIONS, PARTICLE_ITERATIONS);
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
