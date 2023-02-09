package com.gamedesign.notouching.screen;

import android.graphics.Color;
import android.util.Log;

import com.gamedesign.notouching.Level;
import com.gamedesign.notouching.component.ComponentType;
import com.gamedesign.notouching.component.GameObject;
import com.gamedesign.notouching.component.PixmapDrawable;
import com.gamedesign.notouching.component.Position;
import com.gamedesign.notouching.framework.Game;
import com.gamedesign.notouching.framework.Graphics;
import com.gamedesign.notouching.framework.Input;
import com.gamedesign.notouching.framework.Screen;
import com.gamedesign.notouching.framework.TouchConsumer;
import com.gamedesign.notouching.util.Assets;
import com.gamedesign.notouching.util.Collision;
import com.gamedesign.notouching.util.GameObjects;
import com.gamedesign.notouching.util.MyContactListener;
import com.gamedesign.notouching.util.MyTouchConsumer;
import com.gamedesign.notouching.world.WorldHandler;
import com.google.fpl.liquidfun.Joint;
import com.google.fpl.liquidfun.RopeJointDef;
import com.google.fpl.liquidfun.World;

import java.util.Collection;
import java.util.List;

public class GameScreen extends Screen {

    public static final float TILE_ROPE_LENGTH = 1.2f;
    public static final float PIER_HALF_HEIGHT = -12.775f;
    public static final int SECOND_PIER_X_COORDINATE = 1823;
    public static final float PIER_TILE_ROPE_LENGTH = 3f;
    Level level;
    private static final int TILES_NUMBER = 7;
    private static final float starting_tile_positionX = 8;
    private static final float starting_tile_positionY = 17;
    private static final float pierDistance = 55;
    public static final int FIRST_PIER_X_COORDINATE = 64;
    public static final int PIER_Y_COORDINATE = 169;
    private final MyContactListener contactListener = new MyContactListener();
    private final TouchConsumer touchConsumer;

    private boolean RUNNING;

    public GameScreen(Game game) {
        super(game);
        this.level = new Level(game.getGraphics());
        this.touchConsumer = new MyTouchConsumer(level, SECOND_PIER_X_COORDINATE, FIRST_PIER_X_COORDINATE, PIER_Y_COORDINATE, PIER_HALF_HEIGHT);
        WorldHandler.setContactListener(contactListener);
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
        level.addGameObject((Assets.gameObjectsJSON.getGameObject(GameObjects.BOMB, game)));
//        level.addGameObject(Assets.gameObjectsJSON.getGameObject(GameObjects.BOTTOM, game));
//        level.addCar(new Car(game));
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
        Joint firstJoint = setRopeBetweenPierAndTile(firstPier, firstTile, - firstTileDrawable.width / 2, firstTileDrawable.height / 2);
        Joint secondJoint = setRopeBetweenPierAndTile(secondPier, lastTile, firstTileDrawable.width / 2, firstTileDrawable.height / 2);
        level.setFirstRopeFromPier(firstJoint);
        level.setSecondRopeFromPier(secondJoint);
    }

    private Joint setRopeBetweenPierAndTile(GameObject pier, GameObject tile, float xCoordinateOnTile, float yCoordinateOnTile) {
        RopeJointDef pierRope = new RopeJointDef();
        pierRope.setBodyA(pier.getBody());
        pierRope.setBodyB(tile.getBody());
        pierRope.setLocalAnchorA(0, 0);
        pierRope.setLocalAnchorB(xCoordinateOnTile, yCoordinateOnTile);
        pierRope.setCollideConnected(true);
        pierRope.setMaxLength(PIER_TILE_ROPE_LENGTH);
        return WorldHandler.createJoint(pierRope);
    }

    private Joint insertRopeBetweenTiles(Game game, GameObject firstTile, GameObject tile, PixmapDrawable component) {
        RopeJointDef jointDef = new RopeJointDef();
        jointDef.setBodyA(firstTile.getBody());
        jointDef.setBodyB(tile.getBody());
        jointDef.setLocalAnchorA(component.width, component.height / 2);
        jointDef.setLocalAnchorB(0, component.height / 2);
        jointDef.setMaxLength(TILE_ROPE_LENGTH);
        jointDef.setCollideConnected(true);
        return WorldHandler.createJoint(jointDef);
    }



    @Override
    public void update(float deltaTime) {
        if (RUNNING) {
            List<Input.TouchEvent> touchEvents = game.getInput().getTouchEvents();
            if (touchEvents.size() > 0) {
                touchEvents.forEach(touchConsumer::handleTouchEvent);
            }

            Graphics g = this.game.getGraphics();
            g.clear(Color.argb(255, 0, 0, 0));

            WorldHandler.step();

            handleCollisions(contactListener.getCollisions());

            level.render();

            Log.println(Log.ASSERT, "TIME", String.valueOf(deltaTime));
        }
    }

    private void handleCollisions(Collection<Collision> collisions){
        for(Collision event: collisions){
            event.toString();
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
        WorldHandler.delete();
    }
}
