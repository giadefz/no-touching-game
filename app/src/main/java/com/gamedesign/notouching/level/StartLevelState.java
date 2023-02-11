package com.gamedesign.notouching.level;

public class StartLevelState extends LevelState {

    public StartLevelState(Level level) {
        super(level);
    }

    @Override
    public void updateLevel(float deltaTime) {
        commonUpdates();
    }

    @Override
    public void nextState() {
        level.state = new TicktockState(level);
    }
}
