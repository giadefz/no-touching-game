package com.gamedesign.notouching.level.states;

import static com.gamedesign.notouching.util.ScreenInfo.X_COORD_BUTTON;
import static com.gamedesign.notouching.util.ScreenInfo.X_COORD_START_BUTTON;
import static com.gamedesign.notouching.util.ScreenInfo.Y_COORD_BUTTON;
import static com.gamedesign.notouching.util.ScreenInfo.Y_COORD_START_BUTTON;

import com.gamedesign.notouching.level.Level;
import com.gamedesign.notouching.util.Assets;

public class PauseState extends LevelState {

    private LevelState previousState;
    public boolean musicOn = true;

    public void initializeState(Level level, LevelState previousState) {
        super.initializeState(level);
        this.previousState = previousState;
    }

    @Override
    public void updateLevel(float deltaTime) {
        commonUpdates();
        drawPlayButton();
        if(musicOn)
            drawStopMusicButton();
        else
            drawStartMusicButton();
    }

    private void drawStopMusicButton() {
        level.game.getGraphics().drawPixmap(Assets.stopMusicButton, X_COORD_BUTTON, Y_COORD_BUTTON);
    }

    private void drawStartMusicButton() {
        level.game.getGraphics().drawPixmap(Assets.startMusicButton, X_COORD_BUTTON, Y_COORD_BUTTON);
    }

    @Override
    public void nextState() {
        level.state = previousState;
    }

    private void drawPlayButton(){
        level.game.getGraphics().drawPixmap(Assets.playButton, X_COORD_START_BUTTON, Y_COORD_START_BUTTON);
    }

}
