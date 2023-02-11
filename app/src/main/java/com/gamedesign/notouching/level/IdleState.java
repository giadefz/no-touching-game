package com.gamedesign.notouching.level;

public class IdleState extends LevelState {

    private float timeUntilNextState;
    private final LevelState nextState;

    public IdleState(Level level, float timeUntilNextState, LevelState nextState) {
        super(level);
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
        level.state = nextState;
    }
}
