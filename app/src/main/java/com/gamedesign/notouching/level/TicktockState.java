package com.gamedesign.notouching.level;

import com.gamedesign.notouching.component.ComponentType;
import com.gamedesign.notouching.component.Exploding;
import com.gamedesign.notouching.component.GameObject;

public class TicktockState extends LevelState {

    public TicktockState(Level level) {
        super(level);
    }

    @Override
    public void updateLevel(float deltaTime) {
        drawGameObjects();
        drawRopes();
        drawFirstRopeFromPier();
        drawSecondRopeFromPier();
        drawNewRope();
        ticktockBomb(deltaTime);
    }

    @Override
    public void nextState() {
        level.state = new BaseLevelState(level);
    }


    private void ticktockBomb(float deltaTime) {
        for(GameObject go: level.gameObjects){
            Exploding component = go.getComponent(ComponentType.Exploding);
            if(component != null){
                component.shortenFuse(deltaTime);
                if (component.isExploded()){
                    handleExplosion(go, component);
                }
            }
        }
    }

    private void handleExplosion(GameObject go, Exploding component) {
        level.ropesBetweenTiles.remove(component.target);
        level.gameObjects.remove(go);
        level.addCar(new Car(level.game, 3000f, level, 10f, null));
        level.newRopeCoordinates.setY(0);
        level.newRopeCoordinates.setX(0);
    }


}
