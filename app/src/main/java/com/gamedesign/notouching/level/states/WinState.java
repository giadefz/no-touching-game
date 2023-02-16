package com.gamedesign.notouching.level.states;

import static com.gamedesign.notouching.util.ScreenInfo.X_COORD_BUTTON;
import static com.gamedesign.notouching.util.ScreenInfo.Y_COORD_BUTTON;

import com.gamedesign.notouching.level.Level;
import com.gamedesign.notouching.level.save.SaveFileHandler;
import com.gamedesign.notouching.util.Assets;

public class WinState extends LevelState {

    public static final String WON_STRING = "HAI VINTO!!";
    public static final String SAVED = "LA PARTITA È STATA SALVATA";
    public int points;

    public WinState() {
    }

    @Override
    public void initializeState(Level level) {
        super.initializeState(level);
        this.points = calculatePoints();
        SaveFileHandler.save(level.game, level.levelNumber, points, level.seed);
    }

    @Override
    public void updateLevel(float deltaTime) {
        commonUpdates();
        drawLevelNumber();
        level.game.getGraphics().drawText(WON_STRING, 679, 122);
        drawNextLevelButton();
        level.game.getGraphics().drawText("PUNTI LIVELLO: " + points, 679, 250);
        level.game.getGraphics().drawText(SAVED, 479, 450);
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
