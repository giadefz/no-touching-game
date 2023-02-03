package com.gamedesign.notouching;

import android.graphics.Color;

import com.gamedesign.notouching.component.ComponentType;
import com.gamedesign.notouching.component.Drawable;
import com.gamedesign.notouching.component.GameObject;
import com.gamedesign.notouching.framework.Graphics;
import com.google.fpl.liquidfun.Body;
import com.google.fpl.liquidfun.Joint;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class Level {

    private final List<GameObject> gameObjects;
    private final List<Joint> ropesBetweenTiles;
    private Joint firstRopeFromPier;
    private Joint secondRopeFromPier;
    private final Graphics graphics;

    public Level(Graphics graphics) {
        this.gameObjects = new ArrayList<>();
        this.ropesBetweenTiles = new ArrayList<>();
        this.graphics = graphics;
    }

    public synchronized GameObject addGameObject(GameObject obj) {
        gameObjects.add(obj);
        return obj;
    }

    public GameObject getGameObject(int index) {
        return gameObjects.get(index);
    }

    public synchronized void addRopeBetweenTiles(Joint rope) {
        ropesBetweenTiles.add(rope);
    }

    public synchronized void setFirstRopeFromPier(Joint firstRopeFromPier) {
        this.firstRopeFromPier = firstRopeFromPier;
    }

    public synchronized void setSecondRopeFromPier(Joint secondRopeFromPier) {
        this.secondRopeFromPier = secondRopeFromPier;
    }

    public synchronized void render() {
        drawGameObjects();
        drawRopes();
        drawFirstRopeFromPier();
        drawSecondRopeFromPier();
    }

    private void drawGameObjects() {
        gameObjects.stream()
                .map(gameObject -> gameObject.<Drawable>getComponent(ComponentType.Drawable))
                .filter(Objects::nonNull)
                .forEach(Drawable::drawThis);
    }

    private void drawRopes() {
        ropesBetweenTiles.forEach(j -> {
            Body bodyA = j.getBodyA();
            Body bodyB = j.getBodyB();
            graphics.drawLine((bodyA.getPositionX() + 3) * 32, bodyA.getPositionY() * 32,
                    (bodyB.getPositionX() - 3) * 32, bodyB.getPositionY() * 32, Color.argb(200, 255, 248, 220));
        });
    }

    private void drawFirstRopeFromPier() {
        Body bodyA = firstRopeFromPier.getBodyA();
        Body bodyB = firstRopeFromPier.getBodyB();
        graphics.drawLine((bodyA.getPositionX()) * 32, (bodyA.getPositionY() - 12.775f) * 32,
                (bodyB.getPositionX() - 3) * 32, (bodyB.getPositionY()) * 32,
                Color.argb(200, 255, 248, 220));
    }

    private void drawSecondRopeFromPier() {
        Body bodyA = secondRopeFromPier.getBodyA();
        Body bodyB = secondRopeFromPier.getBodyB();
        graphics.drawLine((bodyA.getPositionX()) * 32, (bodyA.getPositionY() - 12.775f) * 32,
                (bodyB.getPositionX() + 3) * 32, (bodyB.getPositionY()) * 32,
                Color.argb(200, 255, 248, 220));
    }

}
