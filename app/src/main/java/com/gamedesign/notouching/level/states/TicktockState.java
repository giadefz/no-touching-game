package com.gamedesign.notouching.level.states;

import static com.gamedesign.notouching.level.states.LevelStates.CHECK_WIN;
import static com.gamedesign.notouching.level.states.LevelStates.IDLE;
import static com.gamedesign.notouching.util.ScreenInfo.X_COORD_BUTTON;
import static com.gamedesign.notouching.util.ScreenInfo.Y_COORD_BUTTON;

import com.gamedesign.notouching.component.ComponentType;
import com.gamedesign.notouching.component.Exploding;
import com.gamedesign.notouching.component.GameObject;
import com.gamedesign.notouching.level.Level;
import com.gamedesign.notouching.level.save.SaveFileHandler;
import com.gamedesign.notouching.util.Assets;

import java.util.Arrays;
import java.util.Locale;

public class TicktockState extends LevelState {

    private int explodedBombIndex = 0;
    private int totalBombs = 0;
    private GameObject[] toRemove = new GameObject[2];

    public TicktockState() {
    }

    @Override
    public void initializeState(Level level) {
        super.initializeState(level);
        totalBombs = level.car.targetCoordinates.length;
        explodedBombIndex = 0;
        SaveFileHandler.resetSave(level.game);
        Arrays.fill(toRemove, null);
    }

    @Override
    public void updateLevel(float deltaTime) {
        commonUpdates();
        drawPauseButton();
        drawBudget();
        drawLevelNumber();
        ticktockBomb(deltaTime);
        drawStopButton();
    }

    @Override
    public void nextState() {
        IDLE.initializeState(level, CHECK_WIN, 2.5f);
        level.state = IDLE;
    }


    private void drawBudget() {
        level.game.getGraphics().drawText("Budget: " + String.valueOf(level.ropeBudget), 50, 40);
    }

    private void ticktockBomb(float deltaTime) {
        for (GameObject go : level.gameObjects) {
            Exploding bomb = go.getComponent(ComponentType.Exploding);
            if (bomb != null) {
                bomb.shortenFuse(deltaTime);
                if (bomb.isExploded()) {
                    toRemove[explodedBombIndex] = go;
                    level.handleExplosion(go, bomb);
                    explodedBombIndex++;
                } else {
                    drawTimer(bomb.timeUntilIgnition);
                }
            }
        }
        if (toRemove.length > 0) {
            Arrays.stream(toRemove).forEach(go -> level.gameObjects.remove(go));
        }
        if(explodedBombIndex == totalBombs){
            nextState();
        }
    }

    private void drawTimer(float timeUntilIgnition) {
        String timeString = String.format(Locale.getDefault(), "%.2f", timeUntilIgnition);
        level.game.getGraphics().drawText(timeString, 850, 100);
    }

    private void drawStopButton() {
        level.game.getGraphics().drawPixmap(Assets.stopButton, X_COORD_BUTTON, Y_COORD_BUTTON);
    }


}
