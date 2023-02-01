package com.gamedesign.notouching;

import com.gamedesign.notouching.component.ComponentType;
import com.gamedesign.notouching.component.Drawable;
import com.gamedesign.notouching.component.GameObject;
import com.google.fpl.liquidfun.Joint;
import com.google.fpl.liquidfun.JointDef;

import java.util.LinkedList;
import java.util.List;
import java.util.Objects;

public class Level {

    private final List<GameObject> gameObjects;
    private final List<Joint> ropesBetweenTiles;

    public Level() {
        this.gameObjects = new LinkedList<>();
        this.ropesBetweenTiles = new LinkedList<>();
    }

    public synchronized GameObject addGameObject(GameObject obj) {
        gameObjects.add(obj);
        return obj;
    }

    public synchronized void addRope(Joint rope){
        ropesBetweenTiles.add(rope);
    }

    public synchronized void render()
    {
        drawGameObjects();
    }

    private void drawGameObjects() {
        gameObjects.stream()
                .map(gameObject -> gameObject.<Drawable>getComponent(ComponentType.Drawable))
                .filter(Objects::nonNull)
                .forEach(Drawable::drawThis);
    }

}
