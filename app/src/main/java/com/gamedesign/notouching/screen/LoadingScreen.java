package com.gamedesign.notouching.screen;


import com.gamedesign.notouching.framework.Game;
import com.gamedesign.notouching.framework.Graphics;
import com.gamedesign.notouching.framework.Screen;
import com.gamedesign.notouching.parse.GameObjectsJSON;
import com.gamedesign.notouching.util.Assets;
import com.gamedesign.notouching.util.GsonMapper;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;

public class LoadingScreen extends Screen {

    public LoadingScreen(Game game) {
        super(game);
    }

    @Override
    public void update(float deltaTime) {
        Graphics graphics = game.getGraphics();

        Assets.click = game.getAudio().newSound("click.ogg");
        Assets.explosion = game.getAudio().newSound("explosion.ogg");
        Assets.ost = game.getAudio().newMusic("ost.ogg");
        Assets.background = graphics.newPixmap("menuBackground.png", Graphics.PixmapFormat.RGB565);
        Assets.mainMenuPlayButton = graphics.newPixmap("mainMenu.png", Graphics.PixmapFormat.ARGB4444);
        Assets.buttons = graphics.newPixmap("buttons.png", Graphics.PixmapFormat.ARGB4444);
        Assets.tile = graphics.newPixmap("bridgeTile1.png", Graphics.PixmapFormat.ARGB4444);
        Assets.tile2 = graphics.newPixmap("bridgeTile2.png", Graphics.PixmapFormat.ARGB4444);
        Assets.tile3 = graphics.newPixmap("bridgeTile3.png", Graphics.PixmapFormat.ARGB4444);
        Assets.bomb = graphics.newPixmap("bomb.png", Graphics.PixmapFormat.ARGB4444);
        Assets.stopButton = graphics.newPixmap("stop.png", Graphics.PixmapFormat.ARGB4444);
        Assets.pauseButton = graphics.newPixmap("pause.png", Graphics.PixmapFormat.ARGB4444);
        Assets.nextLevelButton = graphics.newPixmap("nextlevel.png", Graphics.PixmapFormat.ARGB4444);
        Assets.playButton = graphics.newPixmap("play.png", Graphics.PixmapFormat.ARGB4444);
        Assets.retryButton = graphics.newPixmap("retry.png", Graphics.PixmapFormat.ARGB4444);
        Assets.lvl1background = graphics.newPixmap("levelBackground1.png", Graphics.PixmapFormat.RGB565);
        Assets.pier = graphics.newPixmap("pier.png", Graphics.PixmapFormat.RGB565);
        Assets.pier2 = graphics.newPixmap("pier2.png", Graphics.PixmapFormat.RGB565);
        Assets.wheel = graphics.newPixmap("wheel.png", Graphics.PixmapFormat.RGB565);
        Assets.chassis = graphics.newPixmap("chassis.png", Graphics.PixmapFormat.RGB565);
        Assets.stopMusicButton = graphics.newPixmap("stopmusic.png", Graphics.PixmapFormat.RGB565);
        Assets.startMusicButton = graphics.newPixmap("startmusic.png", Graphics.PixmapFormat.RGB565);
        Assets.terroristChassis = graphics.newPixmap("terroristChassis.png", Graphics.PixmapFormat.RGB565);
        Assets.lvl2background = graphics.newPixmap("levelBackground2.png", Graphics.PixmapFormat.RGB565);
        Assets.lvl3background = graphics.newPixmap("levelBackground3.png", Graphics.PixmapFormat.RGB565);
        Assets.engine = game.getAudio().newMusic("engine.ogg");
        Assets.tileHit[0] = game.getAudio().newSound("tileHit1.ogg");
        Assets.tileHit[1] = game.getAudio().newSound("tileHit2.ogg");
        Assets.tileHit[2] = game.getAudio().newSound("tileHit3.ogg");
        Assets.tileHit[3] = game.getAudio().newSound("tileHit4.ogg");
        Assets.tutorial1 = graphics.newPixmap("tutorial1.png", Graphics.PixmapFormat.RGB565);
        Assets.tutorial2 = graphics.newPixmap("tutorial2.png", Graphics.PixmapFormat.RGB565);
        Assets.tutorial3 = graphics.newPixmap("tutorial3.png", Graphics.PixmapFormat.RGB565);
        Assets.tutorial4 = graphics.newPixmap("tutorial4.png", Graphics.PixmapFormat.RGB565);


        try (Reader reader = new InputStreamReader(game.getFileIO().readAsset("gameobjects.json"))) {
            Assets.gameObjectsJSON = GsonMapper.fromJson(reader, GameObjectsJSON.class);
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
