package com.gamedesign.notouching.level;

public class BaseLevelState extends LevelState {

    public BaseLevelState(Level level) {
        super(level);
    }

    @Override
    public void updateLevel(float deltaTime) {
        drawGameObjects();
        drawRopes();
        drawFirstRopeFromPier();
        drawSecondRopeFromPier();
        drawNewRope();
    }

    @Override
    public void nextState() {
        level.state = new TicktockState(level);
    }
}
