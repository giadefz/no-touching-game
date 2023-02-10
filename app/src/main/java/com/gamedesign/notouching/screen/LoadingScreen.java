package com.gamedesign.notouching.screen;


import com.gamedesign.notouching.framework.Game;
import com.gamedesign.notouching.framework.Graphics;
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
        Graphics graphics = game.getGraphics();

        Assets.click = game.getAudio().newSound("click.ogg");
        Assets.explosion = game.getAudio().newSound("explosion.ogg");
        Assets.background = graphics.newPixmap("background.png", Graphics.PixmapFormat.RGB565);
        Assets.mainMenu = graphics.newPixmap("mainmenu.png", Graphics.PixmapFormat.ARGB4444);
        Assets.buttons = graphics.newPixmap("buttons.png", Graphics.PixmapFormat.ARGB4444);
        Assets.tile = graphics.newPixmap("BridgeTiles.png", Graphics.PixmapFormat.ARGB4444);

        try (Reader reader = new InputStreamReader(game.getFileIO().readAsset("gameobjects.json"))) {
            Assets.gameObjectsJSON = gson.fromJson(reader, GameObjectsJSON.class);
        } catch (IOException e) {
            e.printStackTrace();
        }

        Assets.icon = graphics.newPixmap("icona.png", Graphics.PixmapFormat.ARGB4444);

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
