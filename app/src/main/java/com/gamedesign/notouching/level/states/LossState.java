package com.gamedesign.notouching.level.states;

import static com.gamedesign.notouching.util.ScreenInfo.X_COORD_BUTTON;
import static com.gamedesign.notouching.util.ScreenInfo.Y_COORD_BUTTON;

import android.graphics.Color;

import com.gamedesign.notouching.level.save.SaveFileHandler;
import com.gamedesign.notouching.util.Assets;

public class LossState extends LevelState {

    private static final String LOSS_STRING = "HAI PERSO!";
    public static final String TOTAL_POINTS = "TOTALE PUNTI PARTITA: ";
    public static final String HIGHSCORE = "NUOVO HIGHSCORE!";

    public LossState() {
    }

    @Override
    public void updateLevel(float deltaTime) {
        commonUpdates();
        drawRetryButton();
        drawLevelNumber();
        level.game.getGraphics().drawText(LOSS_STRING, 679, 122);
        if (SaveFileHandler.getSave().highScore == level.totalPoints) {
            level.game.getGraphics().drawText(HIGHSCORE, 679, 850, Color.RED);
        }
        level.game.getGraphics().drawText(TOTAL_POINTS + level.totalPoints, 679, 450, Color.YELLOW);


    }

    @Override
    public void nextState() {

    }

    private void drawRetryButton() {
        level.game.getGraphics().drawPixmap(Assets.retryButton, X_COORD_BUTTON, Y_COORD_BUTTON);
    }

}
