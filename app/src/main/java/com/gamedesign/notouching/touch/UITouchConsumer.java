package com.gamedesign.notouching.touch;

import static com.gamedesign.notouching.level.LevelStates.LOSS_STATE;
import static com.gamedesign.notouching.util.ScreenInfo.X_COORD_BUTTON;
import static com.gamedesign.notouching.util.ScreenInfo.Y_COORD_BUTTON;

import android.util.Log;

import com.gamedesign.notouching.component.ComponentType;
import com.gamedesign.notouching.component.Exploding;
import com.gamedesign.notouching.component.GameObject;
import com.gamedesign.notouching.framework.Game;
import com.gamedesign.notouching.framework.Input;
import com.gamedesign.notouching.framework.TouchConsumer;
import com.gamedesign.notouching.level.CheckWinState;
import com.gamedesign.notouching.level.Level;
import com.gamedesign.notouching.level.LossState;
import com.gamedesign.notouching.level.TicktockState;
import com.gamedesign.notouching.level.WinState;
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
        if(inBounds(event, X_COORD_BUTTON, Y_COORD_BUTTON, 125, 125)){
            if(level.state instanceof  WinState){
                level.destroy();
                level.seed = System.currentTimeMillis();
                //TODO: controlla quante volte di fila hai vinto e incrementa la difficoltà
                gameScreen.totalPoints += ((WinState) level.state).points;
                level.setUpLevel(game, gameScreen.totalPoints);
                gameScreen.levelTouchConsumer.pierIndex = level.PIER_INDEX;
            }
            else if(level.state instanceof CheckWinState){
                LOSS_STATE.initializeState(level);
                level.state = LOSS_STATE;
            }
            else if (level.state instanceof LossState){
                level.destroy();
                level.levelNumber = 0;
                level.setUpLevel(game, 0);
                gameScreen.levelTouchConsumer.pierIndex = level.PIER_INDEX;
                gameScreen.totalPoints = 0;
            } else if(level.state instanceof TicktockState){
                for(GameObject go: level.gameObjects){
                    Exploding component = go.getComponent(ComponentType.Exploding);
                    if(component != null){
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
        if(event.x > x && event.x < x + width - 1 &&
                event.y > y && event.y < y + height - 1)
            return true;
        else
            return false;
    }

}
