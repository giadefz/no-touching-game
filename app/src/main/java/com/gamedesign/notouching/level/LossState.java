package com.gamedesign.notouching.level;

import static com.gamedesign.notouching.util.ScreenInfo.X_COORD_RETRY_BUTTON;
import static com.gamedesign.notouching.util.ScreenInfo.Y_COORD_RETRY_BUTTON;

import com.gamedesign.notouching.util.Assets;

public class LossState extends LevelState {

    private static final String LOSS_STRING = "HAI PERDUTO!!!!!";

    public LossState(Level level) {
        super(level);
    }

    @Override
    public void updateLevel(float deltaTime) {
        commonUpdates();
        drawRetryButton();
        level.game.getGraphics().drawText(LOSS_STRING, 679, 122);
    }

    @Override
    public void nextState() {

    }

    private void drawRetryButton(){
        level.game.getGraphics().drawPixmap(Assets.retryButton, X_COORD_RETRY_BUTTON, Y_COORD_RETRY_BUTTON);
    }

}
