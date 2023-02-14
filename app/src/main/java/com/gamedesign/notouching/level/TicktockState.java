package com.gamedesign.notouching.level;

import static com.gamedesign.notouching.level.LevelStates.CHECK_WIN_STATE;
import static com.gamedesign.notouching.level.LevelStates.IDLE_STATE;
import static com.gamedesign.notouching.util.ScreenInfo.X_COORD_BUTTON;
import static com.gamedesign.notouching.util.ScreenInfo.Y_COORD_BUTTON;

import com.gamedesign.notouching.component.ComponentType;
import com.gamedesign.notouching.component.Exploding;
import com.gamedesign.notouching.component.GameObject;
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
        Arrays.fill(toRemove, null);
    }

    @Override
    public void updateLevel(float deltaTime) {
        commonUpdates();
        drawBudget();
        drawLevelNumber();
        ticktockBomb(deltaTime);
        drawStopButton();
    }

    @Override
    public void nextState() {
        IDLE_STATE.initializeState(level, CHECK_WIN_STATE, 2.5f);
        level.state = IDLE_STATE;
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
