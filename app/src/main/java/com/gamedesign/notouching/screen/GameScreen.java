package com.gamedesign.notouching.screen;

import android.graphics.Color;

import com.gamedesign.notouching.Level;
import com.gamedesign.notouching.component.ComponentType;
import com.gamedesign.notouching.component.GameObject;
import com.gamedesign.notouching.component.PixmapDrawable;
import com.gamedesign.notouching.framework.Game;
import com.gamedesign.notouching.framework.Graphics;
import com.gamedesign.notouching.framework.Screen;
import com.gamedesign.notouching.util.Assets;
import com.gamedesign.notouching.util.GameObjects;
import com.google.fpl.liquidfun.DistanceJoint;
import com.google.fpl.liquidfun.DistanceJointDef;
import com.google.fpl.liquidfun.Joint;

public class GameScreen extends Screen {

    Level level;
    private static final int TILES_NUMBER = 7;
    private static final int VELOCITY_ITERATIONS = 8;
    private static final int POSITION_ITERATIONS = 3;
    private static final int PARTICLE_ITERATIONS = 3;
    private static final float starting_tile_positionX = 9;
    private static final float starting_tile_positionY = 0;
    private boolean RUNNING;

    public GameScreen(Game game) {
        super(game);
        this.level = new Level();
        GameObject firstTile = level.addGameObject(Assets.gameObjectsJSON.getGameObject(GameObjects.TILE, game));
        for(int i = 1; i < TILES_NUMBER; i++){
            GameObject tile = level.addGameObject(Assets.gameObjectsJSON.getGameObject(GameObjects.TILE, game));
            PixmapDrawable component = tile.<PixmapDrawable>getComponent(ComponentType.Drawable);
            tile.setPosition(starting_tile_positionX + i * (component.width + 1), starting_tile_positionY);
            level.addRope(insertRope(game, firstTile, tile, component));
            firstTile = tile;
        }
        level.addGameObject(Assets.gameObjectsJSON.getGameObject(GameObjects.BOTTOM, game));
    }

    private Joint insertRope(Game game, GameObject firstTile, GameObject tile, PixmapDrawable component) {
        DistanceJointDef jointDef = new DistanceJointDef();
        jointDef.setBodyA(firstTile.getBody());
        jointDef.setBodyB(tile.getBody());
        jointDef.setLocalAnchorA(component.width, component.height / 2);
        jointDef.setLocalAnchorB(0, component.height / 2);
        jointDef.setLength(1.2f);
        jointDef.setCollideConnected(true);
        return game.getWorld().createDistanceJoint(jointDef);
    }

    @Override
    public void update(float deltaTime) {
        if (RUNNING) {
            Graphics g = this.game.getGraphics();
            g.clear(Color.argb(255, 0, 0, 0));
            game.getWorld().step(deltaTime, VELOCITY_ITERATIONS, POSITION_ITERATIONS, PARTICLE_ITERATIONS);
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
