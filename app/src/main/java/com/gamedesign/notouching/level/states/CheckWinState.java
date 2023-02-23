package com.gamedesign.notouching.level.states;

import static com.gamedesign.notouching.level.states.LevelStates.LOSS;
import static com.gamedesign.notouching.level.states.LevelStates.WIN;
import static com.gamedesign.notouching.util.ScreenInfo.X_COORD_BUTTON;
import static com.gamedesign.notouching.util.ScreenInfo.Y_COORD_BUTTON;

import com.gamedesign.notouching.level.Car;
import com.gamedesign.notouching.level.Level;
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
        this.timeUntilLoss = 20f;
    }

    @Override
    public void updateLevel(float deltaTime) {
        timeUntilLoss -= deltaTime;
        if(!carSpawned){
            float[] target = {};
            Level.car.initCar(level.game, target, level, 10f, Assets.chassis);
            level.addCar();
            carSpawned = true;
        }
        commonUpdates();
        drawRetryButton();
        drawLevelNumber();
        level.moveCar();
        if(Level.car.isLost() || timeUntilLoss <= 0){
            LOSS.initializeState(level);
            level.state = LOSS;
        }
    }

    @Override
    public void nextState() {
        WIN.initializeState(level);
        level.state = WIN;
    }

    private void drawRetryButton(){
        level.game.getGraphics().drawPixmap(Assets.retryButton, X_COORD_BUTTON, Y_COORD_BUTTON);
    }

}
