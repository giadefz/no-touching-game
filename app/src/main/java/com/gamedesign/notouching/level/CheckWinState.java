package com.gamedesign.notouching.level;

import static com.gamedesign.notouching.util.ScreenInfo.X_COORD_BUTTON;
import static com.gamedesign.notouching.util.ScreenInfo.Y_COORD_BUTTON;

import com.gamedesign.notouching.util.Assets;

public class CheckWinState extends LevelState {

    private boolean carSpawned;
    private float timeUntilLoss = 20f;

    public CheckWinState(Level level) {
        super(level);
    }

    @Override
    public void updateLevel(float deltaTime) {
        timeUntilLoss -= deltaTime;
        if(!carSpawned){
            level.addCar(new Car(level.game, 3000f, level, 10f, null));
            carSpawned = true;
        }
        commonUpdates();
        drawRetryButton();
        if(level.car.isLost() || timeUntilLoss <= 0){
            level.state = new LossState(level);
        }
    }

    @Override
    public void nextState() {
        level.state = new WinState(level);
    }

    private void drawRetryButton(){
        level.game.getGraphics().drawPixmap(Assets.retryButton, X_COORD_BUTTON, Y_COORD_BUTTON);
    }

}
