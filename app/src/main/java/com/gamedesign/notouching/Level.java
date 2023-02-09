package com.gamedesign.notouching;

import static com.gamedesign.notouching.util.ScreenInfo.SCALING_FACTOR;

import android.graphics.Color;

import com.gamedesign.notouching.component.ComponentType;
import com.gamedesign.notouching.component.Drawable;
import com.gamedesign.notouching.component.GameObject;
import com.gamedesign.notouching.component.PixmapDrawable;
import com.gamedesign.notouching.component.Position;
import com.gamedesign.notouching.framework.Game;
import com.gamedesign.notouching.framework.Input;
import com.gamedesign.notouching.util.Assets;
import com.gamedesign.notouching.util.GameObjects;
import com.gamedesign.notouching.world.WorldHandler;
import com.google.fpl.liquidfun.Body;
import com.google.fpl.liquidfun.Joint;
import com.google.fpl.liquidfun.RopeJointDef;
import com.google.fpl.liquidfun.Vec2;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Random;

public class Level {

    public static final float DISTANCE_BETWEEN_TILES = 3;
    public static final float PIER_HALF_HEIGHT = 12.775f;
    private static final int ROPE_COLOR = Color.argb(200, 255, 248, 220);
    private static final float MOTOR_SPEED = 50f;
    private static final int TILES_NUMBER = 7;
    private static final float STARTING_TILE_POSITION_X = 8;
    private static final float STARTING_TILE_POSITION_Y = 19;
    private static final float TILE_ROPE_LENGTH = 1.2f;
    private static final float PIER_DISTANCE = 55;
    public static final float PIER_TILE_ROPE_LENGTH = 3f;
    private final List<GameObject> gameObjects;
    private final List<Joint> ropesBetweenTiles;
    private final List<Rope> addedRopes;
    public Joint firstRopeFromPier;
    public Joint secondRopeFromPier;
    private final Game game;
    private Car car;
    public Vec2 newRopeCoordinates;
    public Vec2 startingPointCoordinates;
    public final Vec2 temp = new Vec2();

    public Level(Game game) {
        this.gameObjects = new ArrayList<>();
        this.ropesBetweenTiles = new ArrayList<>();
        this.addedRopes = new ArrayList<>();
        this.game = game;
        this.newRopeCoordinates = new Vec2(0, 0);
        this.startingPointCoordinates = new Vec2(0, 0);
        setUpTiles();
        setUpPiers();
        Random random = new Random();
        float xCoordinatesOfLeftTileEdge = (getXCoordinatesOfLeftTileEdge(random.nextInt(5) + 1) * SCALING_FACTOR) + 38f;
        this.addCar(new Car(game, xCoordinatesOfLeftTileEdge, this, 5f));
    }

    public synchronized GameObject addGameObject(GameObject obj) {
        gameObjects.add(obj);
        return obj;
    }

    public synchronized void addCar(Car car) {
        this.car = car;
        this.addGameObject(car.chassis);
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

    private void setUpTiles(){
        GameObject firstTile = this.addGameObject(Assets.gameObjectsJSON.getGameObject(GameObjects.TILE, game));
        for (int i = 1; i < TILES_NUMBER; i++) {
            GameObject tile = Assets.gameObjectsJSON.getGameObject(GameObjects.TILE, game);
            PixmapDrawable component = tile.getComponent(ComponentType.Drawable);
            tile.setPosition(STARTING_TILE_POSITION_X + i * (component.width + 1), STARTING_TILE_POSITION_Y);
            tile = this.addGameObject(tile);
            this.addRopeBetweenTiles(this.insertRopeBetweenTiles(firstTile, tile, component));
            firstTile = tile;
        }
    }

    private void setUpPiers() {
        GameObject firstPier = this.addGameObject(Assets.gameObjectsJSON.getGameObject(GameObjects.PIER, game));
        GameObject secondPier = Assets.gameObjectsJSON.getGameObject(GameObjects.PIER, game);
        GameObject firstTile = this.getGameObject(0);
        PixmapDrawable firstTileDrawable = firstTile.getComponent(ComponentType.Drawable);
        GameObject lastTile = this.getGameObject(TILES_NUMBER - 1);
        Position firstPierPosition = firstPier.getComponent(ComponentType.Position);
        secondPier.setPosition(firstPierPosition.x + PIER_DISTANCE, firstPierPosition.y);
        secondPier = this.addGameObject(secondPier);
        Joint firstJoint = setRopeBetweenPierAndTile(firstPier, firstTile, - firstTileDrawable.width / 2, firstTileDrawable.height / 2);
        Joint secondJoint = setRopeBetweenPierAndTile(secondPier, lastTile, firstTileDrawable.width / 2, firstTileDrawable.height / 2);
        this.setFirstRopeFromPier(firstJoint);
        this.setSecondRopeFromPier(secondJoint);
    }

    private Joint setRopeBetweenPierAndTile(GameObject pier, GameObject tile, float xCoordinateOnTile, float yCoordinateOnTile) {
        RopeJointDef pierRope = new RopeJointDef();
        pierRope.setBodyA(pier.getBody());
        pierRope.setBodyB(tile.getBody());
        pierRope.setLocalAnchorA(0, 0);
        pierRope.setLocalAnchorB(xCoordinateOnTile, yCoordinateOnTile);
        pierRope.setCollideConnected(true);
        pierRope.setMaxLength(PIER_TILE_ROPE_LENGTH);
        return WorldHandler.createJoint(pierRope);
    }

    private Joint insertRopeBetweenTiles(GameObject firstTile, GameObject tile, PixmapDrawable component) {
        RopeJointDef jointDef = new RopeJointDef();
        jointDef.setBodyA(firstTile.getBody());
        jointDef.setBodyB(tile.getBody());
        jointDef.setLocalAnchorA(component.width, component.height / 2);
        jointDef.setLocalAnchorB(0, component.height / 2);
        jointDef.setMaxLength(TILE_ROPE_LENGTH);
        jointDef.setCollideConnected(true);
        return WorldHandler.createJoint(jointDef);
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
            game.getGraphics().drawLine((bodyA.getPositionX()) * SCALING_FACTOR, (bodyA.getPositionY() - PIER_HALF_HEIGHT) * SCALING_FACTOR,
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

        game.getGraphics().drawLine(((aposX + (DISTANCE_BETWEEN_TILES * (float) Math.cos(angleA)))) * SCALING_FACTOR , ((aposY + (DISTANCE_BETWEEN_TILES * (float) Math.sin(angleA)))) * SCALING_FACTOR,
                ((bposX - (DISTANCE_BETWEEN_TILES * (float) Math.cos(angleB)))) * SCALING_FACTOR, ((bposY - (DISTANCE_BETWEEN_TILES * (float) Math.sin(angleB)))) * SCALING_FACTOR,
                ROPE_COLOR);
    }

    private void drawFirstRopeFromPier() { //todo: refactor
        Body bodyA = firstRopeFromPier.getBodyA();
        Body bodyB = firstRopeFromPier.getBodyB();
        game.getGraphics().drawLine((bodyA.getPositionX()) * SCALING_FACTOR, (bodyA.getPositionY() - PIER_HALF_HEIGHT) * SCALING_FACTOR,
                (bodyB.getPositionX() - (DISTANCE_BETWEEN_TILES * (float) Math.cos(bodyB.getAngle()))) * SCALING_FACTOR, (bodyB.getPositionY() - (DISTANCE_BETWEEN_TILES * (float) Math.sin(bodyB.getAngle()))) * SCALING_FACTOR,
                ROPE_COLOR);
    }

    private void drawSecondRopeFromPier() {
        Body bodyA = secondRopeFromPier.getBodyA();
        Body bodyB = secondRopeFromPier.getBodyB();
        game.getGraphics().drawLine((bodyA.getPositionX()) * SCALING_FACTOR, (bodyA.getPositionY() - PIER_HALF_HEIGHT) * SCALING_FACTOR,
                (bodyB.getPositionX() + (DISTANCE_BETWEEN_TILES * (float) Math.cos(bodyB.getAngle()))) * SCALING_FACTOR, (bodyB.getPositionY() + (DISTANCE_BETWEEN_TILES * (float) Math.sin(bodyB.getAngle()))) * SCALING_FACTOR,
                ROPE_COLOR);
    }

    private void drawNewRope(){
        if(newRopeCoordinates.getX() != 0)
            game.getGraphics().drawLine(startingPointCoordinates.getX(), startingPointCoordinates.getY(),
                    newRopeCoordinates.getX(), newRopeCoordinates.getY(), ROPE_COLOR);
    }

    private float getXCoordinatesOfLeftTileEdge(int tileNumber){
        GameObject tile = this.gameObjects.get(tileNumber);
        PixmapDrawable component = tile.getComponent(ComponentType.Drawable);
        temp.setX(-component.width / 2);
        temp.setY(0);
        return tile.getBody().getWorldPoint(temp).getX();
    }

    public synchronized void moveCar(){
        car.move();
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
