package com.gamedesign.notouching.level;

import static com.gamedesign.notouching.util.ScreenInfo.X_COORD_BUTTON;
import static com.gamedesign.notouching.util.ScreenInfo.Y_COORD_BUTTON;

import com.gamedesign.notouching.util.Assets;

public class WinState extends LevelState {

    public static final String WON_STRING = "HAI VINTO!!!";
    public int points;

    public WinState(Level level) {
        super(level);
        points = calculatePoints();
    }

    @Override
    public void updateLevel(float deltaTime) {
        commonUpdates();
        level.game.getGraphics().drawText(WON_STRING, 679, 122);
        level.game.getGraphics().drawPixmap(Assets.nextLevel, X_COORD_BUTTON, Y_COORD_BUTTON);
        level.game.getGraphics().drawText("PUNTI LIVELLO: " + points, 679, 250);
    }

    private int calculatePoints(){
        int timeContribution = (int) level.timeBombStopped + 1;
        return level.ropeBudget * timeContribution / 1000;
    }

    @Override
    public void nextState() {

    }
}
