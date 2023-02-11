package com.gamedesign.notouching.level;

import static com.gamedesign.notouching.util.ScreenInfo.X_COORD_RETRY_BUTTON;
import static com.gamedesign.notouching.util.ScreenInfo.Y_COORD_RETRY_BUTTON;

import com.gamedesign.notouching.util.Assets;

public class WinState extends LevelState {

    public static final String WON_STRING = "HAI VINTO!!! SFACIOM!!!";

    public WinState(Level level) {
        super(level);
    }

    @Override
    public void updateLevel(float deltaTime) {
        commonUpdates();
        level.game.getGraphics().drawText(WON_STRING, 679, 122);
        level.game.getGraphics().drawPixmap(Assets.retryButton, X_COORD_RETRY_BUTTON, Y_COORD_RETRY_BUTTON);
    }

    @Override
    public void nextState() {

    }
}
