package com.gamedesign.notouching.level.states;

import static com.gamedesign.notouching.util.ScreenInfo.X_COORD_BUTTON;
import static com.gamedesign.notouching.util.ScreenInfo.Y_COORD_BUTTON;

import android.graphics.Color;

import com.gamedesign.notouching.util.Assets;

public class LossState extends LevelState {

    private static final String LOSS_STRING = "HAI PERSO!!!!!";

    public LossState() {
    }

    @Override
    public void updateLevel(float deltaTime) {
        commonUpdates();
        drawRetryButton();
        drawLevelNumber();
        level.game.getGraphics().drawText(LOSS_STRING, 679, 122);
        level.game.getGraphics().drawText("TOTALE PUNTI PARTITA: " + level.totalPoints, 679, 450, Color.RED);
    }

    @Override
    public void nextState() {

    }

    private void drawRetryButton(){
        level.game.getGraphics().drawPixmap(Assets.retryButton, X_COORD_BUTTON, Y_COORD_BUTTON);
    }

}
