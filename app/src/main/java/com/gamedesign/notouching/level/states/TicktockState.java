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
import com.gamedesign.notouching.util.GameObjectPool;

import java.util.Arrays;
import java.util.Locale;
import java.util.Objects;

public class TicktockState extends LevelState {

    private int explodedBombIndex = 0;
    private int totalBombs = 0;
    private final GameObject[] toRemove = new GameObject[2];
    public float timePassedSinceEntering;

    public TicktockState() {
    }

    @Override
    public void initializeState(Level level) {
        super.initializeState(level);
        timePassedSinceEntering = 0;
        totalBombs = Level.car.getTotalBombs();
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
        timePassedSinceEntering += deltaTime;
    }

    @Override
    public void nextState() {
        IDLE.initializeState(level, CHECK_WIN, 2.5f);
        level.state = IDLE;
    }


    private void drawBudget() {
        level.game.getGraphics().drawText("Budget: " + level.ropeBudget, 50, 40);
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
            for(GameObject gameObject : toRemove){
                if(gameObject != null){
                    level.gameObjects.remove(gameObject);
                    GameObjectPool.freeGameObject(gameObject);
                }
            }
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
