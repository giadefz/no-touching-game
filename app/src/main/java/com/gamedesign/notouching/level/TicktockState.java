package com.gamedesign.notouching.level;

import static com.gamedesign.notouching.util.ScreenInfo.X_COORD_RETRY_BUTTON;
import static com.gamedesign.notouching.util.ScreenInfo.Y_COORD_RETRY_BUTTON;

import com.gamedesign.notouching.component.ComponentType;
import com.gamedesign.notouching.component.Exploding;
import com.gamedesign.notouching.component.GameObject;
import com.gamedesign.notouching.util.Assets;

import java.util.Locale;

public class TicktockState extends LevelState {

    public TicktockState(Level level) {
        super(level);
    }

    @Override
    public void updateLevel(float deltaTime) {
        commonUpdates();
        ticktockBomb(deltaTime);
        drawStopButton();
    }

    @Override
    public void nextState() {
        level.state = new CheckWinState(level);
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
        level.game.getGraphics().drawPixmap(Assets.stopButton, X_COORD_RETRY_BUTTON, Y_COORD_RETRY_BUTTON);
    }


}
