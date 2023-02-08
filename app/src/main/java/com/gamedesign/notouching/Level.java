package com.gamedesign.notouching;

import android.graphics.Color;

import com.gamedesign.notouching.component.ComponentType;
import com.gamedesign.notouching.component.Drawable;
import com.gamedesign.notouching.component.GameObject;
import com.gamedesign.notouching.framework.Graphics;
import com.gamedesign.notouching.framework.Input;
import com.gamedesign.notouching.util.ScreenInfo;
import com.google.fpl.liquidfun.Body;
import com.google.fpl.liquidfun.Joint;
import com.google.fpl.liquidfun.RopeJoint;
import com.google.fpl.liquidfun.Vec2;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class Level {

    public static final float DISTANCE = 3;
    public static final float PIER_HALF_HEIGHT = 12.775f;
    public static final int ROPE_COLOR = Color.argb(200, 255, 248, 220);
    private final List<GameObject> gameObjects;
    private final List<Joint> ropesBetweenTiles;
    private final List<Rope> addedRopes;
    public Joint firstRopeFromPier;
    public Joint secondRopeFromPier;
    private final Graphics graphics;
    private Car car;
    public Vec2 newRopeCoordinates;
    public Vec2 startingPointCoordinates;

    public Level(Graphics graphics) {
        this.gameObjects = new ArrayList<>();
        this.ropesBetweenTiles = new ArrayList<>();
        this.addedRopes = new ArrayList<>();
        this.graphics = graphics;
        this.newRopeCoordinates = new Vec2(0, 0);
        this.startingPointCoordinates = new Vec2(0, 0); //64, 169 -- 1823,169
    }

    public synchronized GameObject addGameObject(GameObject obj) {
        gameObjects.add(obj);
        return obj;
    }

    public synchronized void addCar(Car car) {
        this.car = car;
        this.addGameObject(car.chassis);
        this.addGameObject(car.frontAxle);
        this.addGameObject(car.rearAxle);
        this.addGameObject(car.frontWheel);
        this.addGameObject(car.backWheel);
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

    public synchronized GameObject getGameObjectWithinBound(Input.TouchEvent event){
        return this.gameObjects.stream()
                .filter(gameObject  -> gameObject.<Drawable>getComponent(ComponentType.Drawable)
                        .isBodyWithinBounds(event))
                .findFirst()
                .orElse(null);
    }

    public synchronized void render() {
        drawGameObjects();
        drawRopes();
        drawFirstRopeFromPier();
        drawSecondRopeFromPier();
        drawNewRope();
    }

    private void drawGameObjects() {
        gameObjects.stream()
                .map(gameObject -> gameObject.<Drawable>getComponent(ComponentType.Drawable))
                .filter(Objects::nonNull)
                .forEach(Drawable::drawThis);
    }

    private void drawRopes() {
        ropesBetweenTiles.forEach(this::drawRope);

        addedRopes.forEach( rope ->{
            Joint joint = rope.joint;
            Body bodyA = joint.getBodyA();
            Body bodyB = joint.getBodyB();
            float bodyBAngle = bodyB.getAngle();
            graphics.drawLine((bodyA.getPositionX()) * ScreenInfo.SCALING_FACTOR, (bodyA.getPositionY() - PIER_HALF_HEIGHT) * ScreenInfo.SCALING_FACTOR,
                    (bodyB.getPositionX() + (rope.localCoordinatesX * (float) Math.cos(bodyBAngle))) * ScreenInfo.SCALING_FACTOR,
                    (bodyB.getPositionY() + (rope.localCoordinatesY * (float) Math.sin(bodyBAngle))) * ScreenInfo.SCALING_FACTOR,
                    ROPE_COLOR);
        });

    }

    private void drawRope(Joint j) {
        Body bodyA = j.getBodyA();
        Body bodyB = j.getBodyB();
        float angleA = bodyA.getAngle();
        float aposY = bodyA.getPositionY();
        float aposX = bodyA.getPositionX();
        float angleB = bodyB.getAngle();
        float bposY = bodyB.getPositionY();
        float bposX = bodyB.getPositionX();

        graphics.drawLine(((aposX + (DISTANCE * (float) Math.cos(angleA)))) * 32 , ((aposY + (DISTANCE * (float) Math.sin(angleA)))) * 32,
                ((bposX - (DISTANCE * (float) Math.cos(angleB)))) * 32, ((bposY - (DISTANCE * (float) Math.sin(angleB)))) * 32,
                ROPE_COLOR);
    }

    private void drawFirstRopeFromPier() { //todo: refactor
        Body bodyA = firstRopeFromPier.getBodyA();
        Body bodyB = firstRopeFromPier.getBodyB();
        graphics.drawLine((bodyA.getPositionX()) * ScreenInfo.SCALING_FACTOR, (bodyA.getPositionY() - PIER_HALF_HEIGHT) * ScreenInfo.SCALING_FACTOR,
                (bodyB.getPositionX() - (DISTANCE * (float) Math.cos(bodyB.getAngle()))) * ScreenInfo.SCALING_FACTOR, (bodyB.getPositionY() - (DISTANCE * (float) Math.sin(bodyB.getAngle()))) * ScreenInfo.SCALING_FACTOR,
                ROPE_COLOR);
    }

    private void drawSecondRopeFromPier() {
        Body bodyA = secondRopeFromPier.getBodyA();
        Body bodyB = secondRopeFromPier.getBodyB();
        graphics.drawLine((bodyA.getPositionX()) * ScreenInfo.SCALING_FACTOR, (bodyA.getPositionY() - PIER_HALF_HEIGHT) * ScreenInfo.SCALING_FACTOR,
                (bodyB.getPositionX() + (DISTANCE * (float) Math.cos(bodyB.getAngle()))) * ScreenInfo.SCALING_FACTOR, (bodyB.getPositionY() + (DISTANCE * (float) Math.sin(bodyB.getAngle()))) * ScreenInfo.SCALING_FACTOR,
                ROPE_COLOR);
    }

    private void drawNewRope(){
        if(newRopeCoordinates.getX() != 0)
            graphics.drawLine(startingPointCoordinates.getX(), startingPointCoordinates.getY(),
                    newRopeCoordinates.getX(), newRopeCoordinates.getY(), ROPE_COLOR);
    }

    public synchronized void moveCar(){
        car.move(50f);
    }

    public synchronized void addNewRope(Rope newRope) {
        this.addedRopes.add(newRope);
    }

    public synchronized Joint removeRope(int index){
        Joint toRemove = this.ropesBetweenTiles.get(index);
        this.ropesBetweenTiles.remove(toRemove);
        return toRemove;
    }

}
