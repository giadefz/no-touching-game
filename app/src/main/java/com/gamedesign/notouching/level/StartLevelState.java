package com.gamedesign.notouching.level;

import static com.gamedesign.notouching.level.LevelStates.TICK_TOCK;

public class StartLevelState extends LevelState {

    public StartLevelState() {
    }

    @Override
    public void updateLevel(float deltaTime) {
        commonUpdates();
    }

    @Override
    public void nextState() {
        TICK_TOCK.initializeState(level);
        level.state = TICK_TOCK;
    }
}
