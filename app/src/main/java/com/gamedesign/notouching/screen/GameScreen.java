package com.gamedesign.notouching.screen;

import android.graphics.Color;

import com.gamedesign.notouching.GameWorld;
import com.gamedesign.notouching.framework.Game;
import com.gamedesign.notouching.framework.Graphics;
import com.gamedesign.notouching.framework.Screen;
import com.gamedesign.notouching.util.Assets;
import com.gamedesign.notouching.util.Box;

public class GameScreen extends Screen {

    GameWorld gameWorld;
    private static final int VELOCITY_ITERATIONS = 8;
    private static final int POSITION_ITERATIONS = 3;
    private static final int PARTICLE_ITERATIONS = 3;

    public GameScreen(Game game) {
        super(game);
        this.gameWorld = new GameWorld();
        gameWorld.addGameObject(Assets.gameObjectsJSON.getGameObject("TERRORIST",game));
    }

    @Override
    public void update(float deltaTime) {
        Graphics g = this.game.getGraphics();
        g.clear(Color.argb(255,0,0,0));
        game.getWorld().step(deltaTime, VELOCITY_ITERATIONS, POSITION_ITERATIONS, PARTICLE_ITERATIONS);
        gameWorld.render();
    }

    @Override
    public void present(float deltaTime) {

    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void dispose() {
        this.game.getWorld().delete();
    }
}
