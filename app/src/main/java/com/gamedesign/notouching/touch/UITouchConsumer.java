package com.gamedesign.notouching.touch;

import static com.gamedesign.notouching.util.ScreenInfo.X_COORD_RETRY_BUTTON;
import static com.gamedesign.notouching.util.ScreenInfo.Y_COORD_RETRY_BUTTON;

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
        if(inBounds(event, X_COORD_RETRY_BUTTON, Y_COORD_RETRY_BUTTON, 80, 80)){
            if(level.state instanceof CheckWinState || level.state instanceof WinState || level.state instanceof LossState){
                level.destroy();
                Level newLevel = new Level(game);
                gameScreen.level = newLevel;
                gameScreen.levelTouchConsumer.level = newLevel; //todo: try to refactor this!
                this.level = newLevel;
            } else if(level.state instanceof TicktockState){
                for(GameObject go: level.gameObjects){
                    Exploding component = go.getComponent(ComponentType.Exploding);
                    if(component != null){
                        level.timeBombStopped = component.timeUntilIgnition;
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
