package com.gamedesign.notouching;

import static com.gamedesign.notouching.util.ScreenInfo.SCALING_FACTOR;

import android.graphics.Color;

import com.gamedesign.notouching.component.ComponentType;
import com.gamedesign.notouching.component.Drawable;
import com.gamedesign.notouching.component.GameObject;
import com.gamedesign.notouching.framework.Graphics;
import com.gamedesign.notouching.framework.Input;
import com.google.fpl.liquidfun.Body;
import com.google.fpl.liquidfun.Joint;
import com.google.fpl.liquidfun.Vec2;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class Level {

    public static final float DISTANCE_BETWEEN_TILES = 3;
    public static final float PIER_HALF_HEIGHT = 12.775f;
    public static final int ROPE_COLOR = Color.argb(200, 255, 248, 220);
    public static final float MOTOR_SPEED = 50f;
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
        this.startingPointCoordinates = new Vec2(0, 0);
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
        ropesBetweenTiles.forEach(this::drawRopeBetweenTiles);

        addedRopes.forEach( rope ->{
            Joint joint = rope.joint;
            Body bodyA = joint.getBodyA();
            Body bodyB = joint.getBodyB();
            float bodyBAngle = bodyB.getAngle();
            graphics.drawLine((bodyA.getPositionX()) * SCALING_FACTOR, (bodyA.getPositionY() - PIER_HALF_HEIGHT) * SCALING_FACTOR,
                    (bodyB.getPositionX() + ((rope.localCoordinatesX * (float) Math.cos(bodyBAngle)) - (rope.localCoordinatesY * (float) Math.sin(bodyBAngle)))) * SCALING_FACTOR,
                    (bodyB.getPositionY() + ((rope.localCoordinatesX * (float) Math.sin(bodyBAngle)) + (rope.localCoordinatesY * (float) Math.cos(bodyBAngle)))) * SCALING_FACTOR,
                    ROPE_COLOR);
        });

    }

    private void drawRopeBetweenTiles(Joint j) {
        Body bodyA = j.getBodyA();
        Body bodyB = j.getBodyB();
        float angleA = bodyA.getAngle();
        float aposY = bodyA.getPositionY();
        float aposX = bodyA.getPositionX();
        float angleB = bodyB.getAngle();
        float bposY = bodyB.getPositionY();
        float bposX = bodyB.getPositionX();

        graphics.drawLine(((aposX + (DISTANCE_BETWEEN_TILES * (float) Math.cos(angleA)))) * SCALING_FACTOR , ((aposY + (DISTANCE_BETWEEN_TILES * (float) Math.sin(angleA)))) * SCALING_FACTOR,
                ((bposX - (DISTANCE_BETWEEN_TILES * (float) Math.cos(angleB)))) * SCALING_FACTOR, ((bposY - (DISTANCE_BETWEEN_TILES * (float) Math.sin(angleB)))) * SCALING_FACTOR,
                ROPE_COLOR);
    }

    private void drawFirstRopeFromPier() { //todo: refactor
        Body bodyA = firstRopeFromPier.getBodyA();
        Body bodyB = firstRopeFromPier.getBodyB();
        graphics.drawLine((bodyA.getPositionX()) * SCALING_FACTOR, (bodyA.getPositionY() - PIER_HALF_HEIGHT) * SCALING_FACTOR,
                (bodyB.getPositionX() - (DISTANCE_BETWEEN_TILES * (float) Math.cos(bodyB.getAngle()))) * SCALING_FACTOR, (bodyB.getPositionY() - (DISTANCE_BETWEEN_TILES * (float) Math.sin(bodyB.getAngle()))) * SCALING_FACTOR,
                ROPE_COLOR);
    }

    private void drawSecondRopeFromPier() {
        Body bodyA = secondRopeFromPier.getBodyA();
        Body bodyB = secondRopeFromPier.getBodyB();
        graphics.drawLine((bodyA.getPositionX()) * SCALING_FACTOR, (bodyA.getPositionY() - PIER_HALF_HEIGHT) * SCALING_FACTOR,
                (bodyB.getPositionX() + (DISTANCE_BETWEEN_TILES * (float) Math.cos(bodyB.getAngle()))) * SCALING_FACTOR, (bodyB.getPositionY() + (DISTANCE_BETWEEN_TILES * (float) Math.sin(bodyB.getAngle()))) * SCALING_FACTOR,
                ROPE_COLOR);
    }

    private void drawNewRope(){
        if(newRopeCoordinates.getX() != 0)
            graphics.drawLine(startingPointCoordinates.getX(), startingPointCoordinates.getY(),
                    newRopeCoordinates.getX(), newRopeCoordinates.getY(), ROPE_COLOR);
    }

    public synchronized void moveCar(){
        car.move(MOTOR_SPEED);
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
