package com.gamedesign.notouching.screen;

import android.graphics.Color;
import android.util.Log;

import com.gamedesign.notouching.framework.Game;
import com.gamedesign.notouching.framework.Graphics;
import com.gamedesign.notouching.framework.Input;
import com.gamedesign.notouching.framework.Screen;
import com.gamedesign.notouching.framework.TouchConsumer;
import com.gamedesign.notouching.level.Level;
import com.gamedesign.notouching.level.save.SaveFile;
import com.gamedesign.notouching.level.save.SaveFileHandler;
import com.gamedesign.notouching.level.states.TicktockState;
import com.gamedesign.notouching.touch.LevelTouchConsumer;
import com.gamedesign.notouching.touch.UITouchConsumer;
import com.gamedesign.notouching.util.Assets;
import com.gamedesign.notouching.util.Collision;
import com.gamedesign.notouching.util.GameObjects;
import com.gamedesign.notouching.world.WorldHandler;

import java.util.Collection;
import java.util.List;

public class GameScreen extends Screen {

    public static final float PIER_HALF_HEIGHT = -12.775f;
    public static final int SECOND_PIER_X_COORDINATE = 1823;
    public Level level;
    public static final int FIRST_PIER_X_COORDINATE = 64;
    public static final int PIER_Y_COORDINATE = 169;
    public final LevelTouchConsumer levelTouchConsumer;
    private final TouchConsumer UItouchConsumer;
    public int totalPoints;

    public boolean running;
    public static boolean musicOn;

    public GameScreen(Game game) {
        super(game);
        this.running = true;
        SaveFile saveFile = SaveFileHandler.readSave(game);
        if(saveFile != null && saveFile.seed != 0){
            this.level = new Level(game, saveFile.seed, saveFile.totalPoints, saveFile.levelNumber);
        }else{
            this.level = new Level(game, System.currentTimeMillis(), 0, 0);
        }
        Log.println(Log.INFO, "SEED", String.valueOf(level.seed));
        this.levelTouchConsumer = new LevelTouchConsumer(level, SECOND_PIER_X_COORDINATE, FIRST_PIER_X_COORDINATE, PIER_Y_COORDINATE, PIER_HALF_HEIGHT, level.PIER_INDEX);
        this.UItouchConsumer = new UITouchConsumer(level, this, game);
        startMusic();
    }

    @Override
    public void update(float deltaTime) {
        List<Input.TouchEvent> touchEvents = game.getInput().getTouchEvents();
        if (touchEvents.size() > 0) {
            touchEvents.forEach(UItouchConsumer::handleTouchEvent);
        }
        if (touchEvents.size() > 0 && level.ropeBudget > 0 && level.state instanceof TicktockState) {
            touchEvents.forEach(levelTouchConsumer::handleTouchEvent);
        }
        Graphics g = this.game.getGraphics();
        g.clear(Color.argb(255, 0, 0, 0));
        if (running) {
            WorldHandler.step();
            handleCollisions(WorldHandler.getCollisions());
        }
        if (deltaTime < WorldHandler.step) { //limit to 60FPS
            try {
                Thread.sleep((long) (WorldHandler.step - deltaTime));
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
        level.updateLevel(deltaTime);

    }

    public void startMusic(){
        Assets.ost.setLooping(true);
        Assets.ost.setVolume(0.7f);
        Assets.ost.play();
        musicOn = true;
    }

    public void stopMusic(){
        Assets.ost.stop();
        musicOn = false;
    }

    private void handleCollisions(Collection<Collision> collisions) {
        for (Collision event : collisions) {
            if ( (event.a.name.equals(GameObjects.CHASSIS) || event.a.name.equals(GameObjects.WHEEL)) && event.b.name.equals(GameObjects.PIER) ||
                    (event.b.name.equals(GameObjects.CHASSIS) || event.b.name.equals(GameObjects.WHEEL)) && event.a.name.equals(GameObjects.PIER) ) {
                Level.car.destroy();
                break;
            }
        }
    }

    @Override
    public void present(float deltaTime) {

    }

    @Override
    public void pause() {
        running = false;
        if(Assets.engine.isPlaying())
            Assets.engine.pause();
        if(musicOn)
            Assets.ost.pause();
    }

    @Override
    public void resume() {
        running = true;
        if(musicOn && Level.car.isPlaying())
            Assets.engine.play();
        if(musicOn)
            Assets.ost.play();
    }

    @Override
    public void dispose() {
        WorldHandler.delete();
        if(Assets.engine.isPlaying())
            Assets.engine.stop();
        if(musicOn)
            Assets.ost.stop();
    }
}
