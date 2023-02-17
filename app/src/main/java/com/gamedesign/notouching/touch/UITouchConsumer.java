package com.gamedesign.notouching.touch;

import static com.gamedesign.notouching.level.states.LevelStates.LOSS;
import static com.gamedesign.notouching.level.states.LevelStates.PAUSE;
import static com.gamedesign.notouching.util.ScreenInfo.X_COORD_BUTTON;
import static com.gamedesign.notouching.util.ScreenInfo.X_COORD_PAUSE_BUTTON;
import static com.gamedesign.notouching.util.ScreenInfo.X_COORD_START_BUTTON;
import static com.gamedesign.notouching.util.ScreenInfo.Y_COORD_BUTTON;
import static com.gamedesign.notouching.util.ScreenInfo.Y_COORD_START_BUTTON;

import com.gamedesign.notouching.component.ComponentType;
import com.gamedesign.notouching.component.Exploding;
import com.gamedesign.notouching.component.GameObject;
import com.gamedesign.notouching.framework.Game;
import com.gamedesign.notouching.framework.Input;
import com.gamedesign.notouching.framework.TouchConsumer;
import com.gamedesign.notouching.level.states.CheckWinState;
import com.gamedesign.notouching.level.Level;
import com.gamedesign.notouching.level.states.LossState;
import com.gamedesign.notouching.level.states.PauseState;
import com.gamedesign.notouching.level.states.TicktockState;
import com.gamedesign.notouching.level.states.WinState;
import com.gamedesign.notouching.screen.GameScreen;

public class UITouchConsumer extends TouchConsumer {

    private Level level;
    private final GameScreen gameScreen;
    private final Game game;

    public UITouchConsumer(Level level, GameScreen gameScreen, Game game) {
        this.level = level;
        this.gameScreen = gameScreen;
        this.game = game;
    }

    @Override
    protected void handleTouchDown(Input.TouchEvent event) {
        if (inBounds(event, X_COORD_PAUSE_BUTTON, Y_COORD_BUTTON, 125, 125)) {
            if (level.state instanceof TicktockState) {
                gameScreen.running = false;
                PAUSE.initializeState(level, level.state);
                level.state = PAUSE;
            }
        }
        if (inBounds(event, X_COORD_START_BUTTON, Y_COORD_START_BUTTON, 125, 125)) {
            if (level.state instanceof PauseState) {
                gameScreen.running = true;
                PAUSE.nextState();
            }
        }
        if (inBounds(event, X_COORD_BUTTON, Y_COORD_BUTTON, 125, 125)) {
            if (level.state instanceof WinState) {
                level.destroy();
                level.seed = System.currentTimeMillis();
                gameScreen.totalPoints += ((WinState) level.state).points;
                level.setUpLevel(gameScreen.totalPoints);
                gameScreen.levelTouchConsumer.pierIndex = level.PIER_INDEX;
            } else if (level.state instanceof CheckWinState) {
                LOSS.initializeState(level);
                level.state = LOSS;
            } else if (level.state instanceof LossState) {
                level.destroy();
                level.seed = System.currentTimeMillis();
                level.levelNumber = 0;
                level.setUpLevel(0);
                gameScreen.levelTouchConsumer.pierIndex = level.PIER_INDEX;
                gameScreen.totalPoints = 0;
            } else if (level.state instanceof TicktockState) {
                for (GameObject go : level.gameObjects) {
                    Exploding component = go.getComponent(ComponentType.Exploding);
                    if (component != null) {
                        level.timeBombStopped = component.timeStopped;
                        component.setExplodedToTrue();
                    }
                }
            }

        }
    }

    @Override
    protected void handleTouchUp(Input.TouchEvent event) {

    }

    @Override
    protected void handleTouchDragged(Input.TouchEvent event) {

    }

    private boolean inBounds(Input.TouchEvent event, int x, int y, int width, int height) {
        if (event.x > x && event.x < x + width - 1 &&
                event.y > y && event.y < y + height - 1)
            return true;
        else
            return false;
    }

}
