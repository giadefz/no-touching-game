package com.gamedesign.notouching.level;

import static com.gamedesign.notouching.level.LevelStates.TICK_TOCK_STATE;

public class StartLevelState extends LevelState {

    public StartLevelState() {
    }

    @Override
    public void updateLevel(float deltaTime) {
        commonUpdates();
    }

    @Override
    public void nextState() {
        TICK_TOCK_STATE.initializeState(level);
        level.state = TICK_TOCK_STATE;
    }
}
