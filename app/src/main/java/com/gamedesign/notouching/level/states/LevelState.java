package com.gamedesign.notouching.level.states;


import static com.gamedesign.notouching.level.Level.NEW_ROPE_COLOR;
import static com.gamedesign.notouching.level.Level.PIER_HALF_HEIGHT;
import static com.gamedesign.notouching.level.Level.ROPE_COLOR;
import static com.gamedesign.notouching.util.ScreenInfo.SCALING_FACTOR;
import static com.gamedesign.notouching.util.ScreenInfo.X_COORD_BUTTON;
import static com.gamedesign.notouching.util.ScreenInfo.X_COORD_PAUSE_BUTTON;
import static com.gamedesign.notouching.util.ScreenInfo.Y_COORD_BUTTON;

import com.gamedesign.notouching.component.ComponentType;
import com.gamedesign.notouching.component.Drawable;
import com.gamedesign.notouching.component.GameObject;
import com.gamedesign.notouching.component.PixmapDrawable;
import com.gamedesign.notouching.level.Level;
import com.gamedesign.notouching.util.Assets;
import com.google.fpl.liquidfun.Body;
import com.google.fpl.liquidfun.Joint;

import java.util.Objects;

public abstract class LevelState {

    Level level;

    public LevelState() {
    }

    public void initializeState(Level level){
        this.level = level;
    }

    public abstract void updateLevel(float deltaTime);

    public abstract void nextState();

    protected void commonUpdates(){
        drawBackGround();
        drawGameObjects();
        drawRopes();
        drawFirstRopeFromPier();
        drawSecondRopeFromPier();
        drawNewRope();
    }

    protected void drawLevelNumber() {
        level.game.getGraphics().drawText("Livello: " + level.levelNumber, 50, 95);
    }

    protected void drawPauseButton(){
        level.game.getGraphics().drawPixmap(Assets.pauseButton, X_COORD_PAUSE_BUTTON, Y_COORD_BUTTON);
    }

    private void drawBackGround() {
        level.game.getGraphics().drawPixmap(level.backGround, 0, 0);
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
                    NEW_ROPE_COLOR);
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
        GameObject userDataA = (GameObject) bodyA.getUserData();
        PixmapDrawable drawableA = userDataA.getComponent(ComponentType.Drawable);

        GameObject userDataB = (GameObject) bodyB.getUserData();
        PixmapDrawable drawableB = userDataB.getComponent(ComponentType.Drawable);
        level.game.getGraphics().drawLine(((aposX + ((drawableA.width / 2) * (float) Math.cos(angleA)))) * SCALING_FACTOR , ((aposY + ((drawableA.width / 2) * (float) Math.sin(angleA)))) * SCALING_FACTOR,
                ((bposX - ((drawableB.width / 2) * (float) Math.cos(angleB)))) * SCALING_FACTOR, ((bposY - ((drawableB.width / 2) * (float) Math.sin(angleB)))) * SCALING_FACTOR,
                ROPE_COLOR);
    }

    protected void drawFirstRopeFromPier() { //todo: refactor
        Body bodyA = level.firstRopeFromPier.getBodyA();
        Body bodyB = level.firstRopeFromPier.getBodyB();
        GameObject userDataB = (GameObject) bodyB.getUserData();
        PixmapDrawable drawableB = userDataB.getComponent(ComponentType.Drawable);
        level.game.getGraphics().drawLine((bodyA.getPositionX()) * SCALING_FACTOR, (bodyA.getPositionY() - PIER_HALF_HEIGHT) * SCALING_FACTOR,
                (bodyB.getPositionX() - ((drawableB.width / 2) * (float) Math.cos(bodyB.getAngle()))) * SCALING_FACTOR, (bodyB.getPositionY() - ((drawableB.width / 2) * (float) Math.sin(bodyB.getAngle()))) * SCALING_FACTOR,
                ROPE_COLOR);
    }

    protected void drawSecondRopeFromPier() {
        Body bodyA = level.secondRopeFromPier.getBodyA();
        Body bodyB = level.secondRopeFromPier.getBodyB();
        GameObject userDataB = (GameObject) bodyB.getUserData();
        PixmapDrawable drawableB = userDataB.getComponent(ComponentType.Drawable);
        level.game.getGraphics().drawLine((bodyA.getPositionX()) * SCALING_FACTOR, (bodyA.getPositionY() - PIER_HALF_HEIGHT) * SCALING_FACTOR,
                (bodyB.getPositionX() + ((drawableB.width / 2) * (float) Math.cos(bodyB.getAngle()))) * SCALING_FACTOR, (bodyB.getPositionY() + ((drawableB.width / 2) * (float) Math.sin(bodyB.getAngle()))) * SCALING_FACTOR,
                ROPE_COLOR);
    }

    protected void drawNewRope(){
        if(level.newRopeCoordinates.getX() != 0)
            level.game.getGraphics().drawLine(level.startingPointCoordinates.getX(), level.startingPointCoordinates.getY(),
                    level.newRopeCoordinates.getX(), level.newRopeCoordinates.getY(), NEW_ROPE_COLOR);
    }



}
