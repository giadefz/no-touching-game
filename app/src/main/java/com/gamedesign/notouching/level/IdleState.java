package com.gamedesign.notouching.level;

public class IdleState extends LevelState {

    private float timeUntilNextState;
    private LevelState nextState;

    public IdleState() {
    }

    @Override
    public void initializeState(Level level) {
        super.initializeState(level);
    }

    public void initializeState(Level level, LevelState nextState, float timeUntilNextState) {
        super.initializeState(level);
        this.timeUntilNextState = timeUntilNextState;
        this.nextState = nextState;
    }

    @Override
    public void updateLevel(float deltaTime) {
        timeUntilNextState -= deltaTime;
        if(timeUntilNextState <= 0)
            nextState();
        commonUpdates();
    }

    @Override
    public void nextState() {
        nextState.initializeState(level);
        level.state = nextState;
    }
}
