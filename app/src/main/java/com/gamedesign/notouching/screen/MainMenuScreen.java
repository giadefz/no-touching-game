package com.gamedesign.notouching.screen;

import com.gamedesign.notouching.framework.Game;
import com.gamedesign.notouching.framework.Screen;

public class MainMenuScreen extends Screen {

    public MainMenuScreen(Game game) {
        super(game);
    }

    @Override
    public void update(float deltaTime) {
        game.setScreen(new GameScreen(game));
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

    }
}
