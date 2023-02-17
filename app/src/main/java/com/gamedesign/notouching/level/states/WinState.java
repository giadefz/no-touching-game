package com.gamedesign.notouching.level.states;

import static com.gamedesign.notouching.util.ScreenInfo.X_COORD_BUTTON;
import static com.gamedesign.notouching.util.ScreenInfo.Y_COORD_BUTTON;

import android.graphics.Color;

import com.gamedesign.notouching.level.Level;
import com.gamedesign.notouching.level.save.SaveFile;
import com.gamedesign.notouching.level.save.SaveFileHandler;
import com.gamedesign.notouching.util.Assets;

public class WinState extends LevelState {

    public static final String WON_STRING = "HAI VINTO!!";
    public static final String SAVED = "LA PARTITA È STATA SALVATA";
    public static final String POINTS = "PUNTI LIVELLO: ";
    public int points;
    public boolean highScore;

    public WinState() {
    }

    @Override
    public void initializeState(Level level) {
        super.initializeState(level);
        this.points = calculatePoints();
        SaveFile save = SaveFileHandler.save(level.game, level.levelNumber, level.totalPoints + points, level.seed);
        if(save.highScore == level.totalPoints + points){
            highScore = true;
        }
    }

    @Override
    public void updateLevel(float deltaTime) {
        commonUpdates();
        drawLevelNumber();
        level.game.getGraphics().drawText(WON_STRING, 679, 122);
        drawNextLevelButton();
        level.game.getGraphics().drawText(POINTS + points, 679, 250);
        level.game.getGraphics().drawText(SAVED, 579, 350);
        int totalPoints = points + level.totalPoints;
        level.game.getGraphics().drawText("PUNTI TOTALI: " + totalPoints, 679, 750, Color.YELLOW);
        if(highScore) {
            level.game.getGraphics().drawText("NUOVO HIGHSCORE!", 679, 850, Color.RED);
        }
    }

    private void drawNextLevelButton() {
        level.game.getGraphics().drawPixmap(Assets.nextLevelButton, X_COORD_BUTTON, Y_COORD_BUTTON);
    }

    private int calculatePoints(){
        int timeContribution = (int) level.timeBombStopped + 1;
        return ((level.ropeBudget + 1000) * timeContribution / 1000) * level.difficultySettings.getPointsMultiplier();
    }

    @Override
    public void nextState() {

    }
}
