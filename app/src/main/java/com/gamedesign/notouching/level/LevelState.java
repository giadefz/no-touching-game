package com.gamedesign.notouching.level;

import static com.gamedesign.notouching.level.Level.DISTANCE_BETWEEN_TILES;
import static com.gamedesign.notouching.level.Level.PIER_HALF_HEIGHT;
import static com.gamedesign.notouching.level.Level.ROPE_COLOR;
import static com.gamedesign.notouching.util.ScreenInfo.SCALING_FACTOR;

import com.gamedesign.notouching.component.ComponentType;
import com.gamedesign.notouching.component.Drawable;
import com.google.fpl.liquidfun.Body;
import com.google.fpl.liquidfun.Joint;

import java.util.Objects;

public abstract class LevelState {

    final Level level;

    public LevelState(Level level) {
        this.level = level;
    }

    public abstract void updateLevel(float deltaTime);

    public abstract void nextState();

    protected void commonUpdates(){
        drawGameObjects();
        drawRopes();
        drawFirstRopeFromPier();
        drawSecondRopeFromPier();
        drawNewRope();
    }

    protected void drawGameObjects() {
        level.gameObjects.stream()
                .map(gameObject -> gameObject.<Drawable>getComponent(ComponentType.Drawable))
                .filter(Objects::nonNull)
                .forEach(Drawable::drawThis);
    }

    protected void drawRopes() {
        level.ropesBetweenTiles.forEach(this::drawRopeBetweenTiles);

        level.addedRopes.forEach( rope ->{
            Joint joint = rope.joint;
            Body bodyA = joint.getBodyA();
            Body bodyB = joint.getBodyB();
            float bodyBAngle = bodyB.getAngle();
            level.game.getGraphics().drawLine((bodyA.getPositionX()) * SCALING_FACTOR, (bodyA.getPositionY() - PIER_HALF_HEIGHT) * SCALING_FACTOR,
                    (bodyB.getPositionX() + ((rope.localCoordinatesX * (float) Math.cos(bodyBAngle)) - (rope.localCoordinatesY * (float) Math.sin(bodyBAngle)))) * SCALING_FACTOR,
                    (bodyB.getPositionY() + ((rope.localCoordinatesX * (float) Math.sin(bodyBAngle)) + (rope.localCoordinatesY * (float) Math.cos(bodyBAngle)))) * SCALING_FACTOR,
                    ROPE_COLOR);
        });

    }

    protected void drawRopeBetweenTiles(Joint j) {
        Body bodyA = j.getBodyA();
        Body bodyB = j.getBodyB();
        float angleA = bodyA.getAngle();
        float aposY = bodyA.getPositionY();
        float aposX = bodyA.getPositionX();
        float angleB = bodyB.getAngle();
        float bposY = bodyB.getPositionY();
        float bposX = bodyB.getPositionX();

        level.game.getGraphics().drawLine(((aposX + (DISTANCE_BETWEEN_TILES * (float) Math.cos(angleA)))) * SCALING_FACTOR , ((aposY + (DISTANCE_BETWEEN_TILES * (float) Math.sin(angleA)))) * SCALING_FACTOR,
                ((bposX - (DISTANCE_BETWEEN_TILES * (float) Math.cos(angleB)))) * SCALING_FACTOR, ((bposY - (DISTANCE_BETWEEN_TILES * (float) Math.sin(angleB)))) * SCALING_FACTOR,
                ROPE_COLOR);
    }

    protected void drawFirstRopeFromPier() { //todo: refactor
        Body bodyA = level.firstRopeFromPier.getBodyA();
        Body bodyB = level.firstRopeFromPier.getBodyB();
        level.game.getGraphics().drawLine((bodyA.getPositionX()) * SCALING_FACTOR, (bodyA.getPositionY() - PIER_HALF_HEIGHT) * SCALING_FACTOR,
                (bodyB.getPositionX() - (DISTANCE_BETWEEN_TILES * (float) Math.cos(bodyB.getAngle()))) * SCALING_FACTOR, (bodyB.getPositionY() - (DISTANCE_BETWEEN_TILES * (float) Math.sin(bodyB.getAngle()))) * SCALING_FACTOR,
                ROPE_COLOR);
    }

    protected void drawSecondRopeFromPier() {
        Body bodyA = level.secondRopeFromPier.getBodyA();
        Body bodyB = level.secondRopeFromPier.getBodyB();
        level.game.getGraphics().drawLine((bodyA.getPositionX()) * SCALING_FACTOR, (bodyA.getPositionY() - PIER_HALF_HEIGHT) * SCALING_FACTOR,
                (bodyB.getPositionX() + (DISTANCE_BETWEEN_TILES * (float) Math.cos(bodyB.getAngle()))) * SCALING_FACTOR, (bodyB.getPositionY() + (DISTANCE_BETWEEN_TILES * (float) Math.sin(bodyB.getAngle()))) * SCALING_FACTOR,
                ROPE_COLOR);
    }

    protected void drawNewRope(){
        if(level.newRopeCoordinates.getX() != 0)
            level.game.getGraphics().drawLine(level.startingPointCoordinates.getX(), level.startingPointCoordinates.getY(),
                    level.newRopeCoordinates.getX(), level.newRopeCoordinates.getY(), ROPE_COLOR);
    }



}
