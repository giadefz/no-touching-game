package com.gamedesign.notouching.screen;

import android.graphics.Color;

import com.gamedesign.notouching.level.Level;
import com.gamedesign.notouching.framework.Game;
import com.gamedesign.notouching.framework.Graphics;
import com.gamedesign.notouching.framework.Input;
import com.gamedesign.notouching.framework.Screen;
import com.gamedesign.notouching.framework.TouchConsumer;
import com.gamedesign.notouching.level.TicktockState;
import com.gamedesign.notouching.util.Collision;
import com.gamedesign.notouching.util.GameObjects;
import com.gamedesign.notouching.util.MyContactListener;
import com.gamedesign.notouching.util.MyTouchConsumer;
import com.gamedesign.notouching.world.WorldHandler;

import java.util.Collection;
import java.util.List;

public class GameScreen extends Screen {

    public static final float PIER_HALF_HEIGHT = -12.775f;
    public static final int SECOND_PIER_X_COORDINATE = 1823;
    Level level;
    public static final int FIRST_PIER_X_COORDINATE = 64;
    public static final int PIER_Y_COORDINATE = 169;
    private final MyContactListener contactListener = new MyContactListener();
    private final TouchConsumer touchConsumer;

    private boolean RUNNING;

    public GameScreen(Game game) {
        super(game);
        this.level = new Level(game);
        this.touchConsumer = new MyTouchConsumer(level, SECOND_PIER_X_COORDINATE, FIRST_PIER_X_COORDINATE, PIER_Y_COORDINATE, PIER_HALF_HEIGHT);
        WorldHandler.setContactListener(contactListener);
//        level.addGameObject((Assets.gameObjectsJSON.getGameObject(GameObjects.BOMB, game)));
//        level.addGameObject(Assets.gameObjectsJSON.getGameObject(GameObjects.BOTTOM, game));
    }



    @Override
    public void update(float deltaTime) {
        if (RUNNING) {
            List<Input.TouchEvent> touchEvents = game.getInput().getTouchEvents();
            if (touchEvents.size() > 0 && level.state instanceof TicktockState) {
                touchEvents.forEach(touchConsumer::handleTouchEvent);
            }

            Graphics g = this.game.getGraphics();
            g.clear(Color.argb(255, 0, 0, 0));

            WorldHandler.step();

            handleCollisions(contactListener.getCollisions());

            level.moveCar();

            level.updateLevel(deltaTime);

//            Log.println(Log.ASSERT, "TIME", String.valueOf(deltaTime));
        }
    }

    private void handleCollisions(Collection<Collision> collisions){
        for(Collision event: collisions){
            if(event.a.name.equals(GameObjects.CHASSIS) && event.b.name.equals(GameObjects.PIER) ||
                    event.b.name.equals(GameObjects.CHASSIS) && event.a.name.equals(GameObjects.PIER)){
                level.destroyCar();
            }
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
