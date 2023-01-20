package com.gamedesign.notouching.screen;


import com.gamedesign.notouching.framework.Game;
import com.gamedesign.notouching.framework.Screen;
import com.gamedesign.notouching.parse.GameObjectsJSON;
import com.gamedesign.notouching.util.Assets;
import com.google.gson.Gson;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;

public class LoadingScreen extends Screen {

    public LoadingScreen(Game game) {
        super(game);
    }

    @Override
    public void update(float deltaTime) {
        Gson gson = new Gson();

        try (Reader reader = new InputStreamReader(game.getFileIO().readAsset("gameobjects.json"))) {
            Assets.gameObjectsJSON = gson.fromJson(reader, GameObjectsJSON.class);
        } catch (IOException e) {
            e.printStackTrace();
        }

        game.setScreen(new MainMenuScreen(game));
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
