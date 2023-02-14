package com.gamedesign.notouching.level;

import static com.gamedesign.notouching.level.LevelStates.LOSS_STATE;
import static com.gamedesign.notouching.level.LevelStates.WIN_STATE;
import static com.gamedesign.notouching.util.ScreenInfo.X_COORD_BUTTON;
import static com.gamedesign.notouching.util.ScreenInfo.Y_COORD_BUTTON;

import com.gamedesign.notouching.util.Assets;

public class CheckWinState extends LevelState {

    private boolean carSpawned;
    private float timeUntilLoss;

    public CheckWinState() {
    }

    @Override
    public void initializeState(Level level) {
        super.initializeState(level);
        this.carSpawned = false;
        this.timeUntilLoss = 10f;
    }

    @Override
    public void updateLevel(float deltaTime) {
        timeUntilLoss -= deltaTime;
        if(!carSpawned){
            float[] target = {};
            level.addCar(new Car(level.game, target, level, 10f));
            carSpawned = true;
        }
        commonUpdates();
        drawRetryButton();
        if(level.car.isLost() || timeUntilLoss <= 0){
            LOSS_STATE.initializeState(level);
            level.state = LOSS_STATE;
        }
    }

    @Override
    public void nextState() {
        WIN_STATE.initializeState(level);
        level.state = WIN_STATE;
    }

    private void drawRetryButton(){
        level.game.getGraphics().drawPixmap(Assets.retryButton, X_COORD_BUTTON, Y_COORD_BUTTON);
    }

}
