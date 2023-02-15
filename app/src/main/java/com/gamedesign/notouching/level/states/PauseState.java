package com.gamedesign.notouching.level.states;

import static com.gamedesign.notouching.util.ScreenInfo.X_COORD_START_BUTTON;
import static com.gamedesign.notouching.util.ScreenInfo.Y_COORD_START_BUTTON;

import com.gamedesign.notouching.level.Level;
import com.gamedesign.notouching.util.Assets;

public class PauseState extends LevelState {

    private LevelState previousState;


    public void initializeState(Level level, LevelState previousState) {
        super.initializeState(level);
        this.previousState = previousState;
    }

    @Override
    public void updateLevel(float deltaTime) {
        commonUpdates(); //todo: tasto per uscire dal gioco
        drawPlayButton();
    }

    @Override
    public void nextState() {
        level.state = previousState;
    }

    private void drawPlayButton(){
        level.game.getGraphics().drawPixmap(Assets.playButton, X_COORD_START_BUTTON, Y_COORD_START_BUTTON);
    }

}
