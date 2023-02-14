package com.gamedesign.notouching.level;

import static com.gamedesign.notouching.level.LevelStates.CHECK_WIN_STATE;
import static com.gamedesign.notouching.util.ScreenInfo.X_COORD_BUTTON;
import static com.gamedesign.notouching.util.ScreenInfo.Y_COORD_BUTTON;

import com.gamedesign.notouching.component.ComponentType;
import com.gamedesign.notouching.component.Exploding;
import com.gamedesign.notouching.component.GameObject;
import com.gamedesign.notouching.util.Assets;

import java.util.Locale;

public class TicktockState extends LevelState {

    public TicktockState() {
    }

    @Override
    public void updateLevel(float deltaTime) {
        commonUpdates();
        drawBudget();
        ticktockBomb(deltaTime);
        drawStopButton();
    }

    @Override
    public void nextState() {
        CHECK_WIN_STATE.initializeState(level);
        level.state = CHECK_WIN_STATE;
    }


    private void drawBudget() {
        level.game.getGraphics().drawText("Budget: " + String.valueOf(level.ropeBudget), 50, 40);
    }

    private void ticktockBomb(float deltaTime) {
        for(GameObject go: level.gameObjects){
            Exploding bomb = go.getComponent(ComponentType.Exploding);
            if(bomb != null){
                bomb.shortenFuse(deltaTime);
                if (bomb.isExploded()){
                    level.handleExplosion(go, bomb);
                }else{
                    drawTimer(bomb.timeUntilIgnition);
                }
            }
        }
    }

    private void drawTimer(float timeUntilIgnition){
        String timeString = String.format(Locale.getDefault(), "%.2f", timeUntilIgnition);
        level.game.getGraphics().drawText(timeString ,850, 100);
    }

    private void drawStopButton(){
        level.game.getGraphics().drawPixmap(Assets.stopButton, X_COORD_BUTTON, Y_COORD_BUTTON);
    }


}
