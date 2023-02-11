package com.gamedesign.notouching.level;

import static com.gamedesign.notouching.util.ScreenInfo.SCALING_FACTOR;

import android.graphics.Color;

import com.gamedesign.notouching.component.ComponentType;
import com.gamedesign.notouching.component.Drawable;
import com.gamedesign.notouching.component.Exploding;
import com.gamedesign.notouching.component.GameObject;
import com.gamedesign.notouching.component.PixmapDrawable;
import com.gamedesign.notouching.component.Position;
import com.gamedesign.notouching.framework.Game;
import com.gamedesign.notouching.framework.Input;
import com.gamedesign.notouching.util.Assets;
import com.gamedesign.notouching.util.GameObjects;
import com.gamedesign.notouching.world.WorldHandler;
import com.google.fpl.liquidfun.Joint;
import com.google.fpl.liquidfun.RopeJointDef;
import com.google.fpl.liquidfun.Vec2;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Level {

    public static final float PIER_HALF_HEIGHT = 12.775f;
    public static final int ROPE_COLOR = Color.argb(200, 255, 248, 220);
    public static final int DISTANCE_BETWEEN_TILES = 1;
    public static final int TILES_NUMBER = 7;
    public static final float STARTING_TILE_POSITION_X = 8;
    public static final float STARTING_TILE_POSITION_Y = 19;
    public static final float TILE_ROPE_LENGTH = 1.2f;
    public static final float PIER_DISTANCE = 55;
    public static final float PIER_TILE_ROPE_LENGTH = 3f;


    public final List<GameObject> gameObjects;
    public final List<Joint> ropesBetweenTiles;
    public final List<Rope> addedRopes;
    public Joint firstRopeFromPier;
    public Joint secondRopeFromPier;
    public final Game game;
    private Car car;
    public Vec2 newRopeCoordinates;
    public Vec2 startingPointCoordinates;
    public final Vec2 temp = new Vec2();
    public LevelState state;
    public float timeBombStopped;

    public Level(Game game) {
        this.gameObjects = new ArrayList<>();
        this.ropesBetweenTiles = new ArrayList<>();
        this.addedRopes = new ArrayList<>();

        this.game = game;

        this.newRopeCoordinates = new Vec2(0, 0);
        this.startingPointCoordinates = new Vec2(0, 0);

        this.state = new StartLevelState(this);

        setUpTiles();
        setUpPiers();

        Random random = new Random();
        int index = random.nextInt(5) + 1;
        float xCoordinatesOfTileLeftEdge = (getXCoordinatesOfTileLeftEdge(index) * SCALING_FACTOR);

        this.addCar(new Car(game, xCoordinatesOfTileLeftEdge, this, 5f, ropesBetweenTiles.get(index - 1)));
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

    public synchronized void updateLevel(float deltaTime) {
        state.updateLevel(deltaTime);
    }

    private void setUpTiles(){
        GameObject firstTile = this.addGameObject(Assets.gameObjectsJSON.getGameObject(GameObjects.TILE, game));
        for (int i = 1; i < TILES_NUMBER; i++) {
            GameObject tile = Assets.gameObjectsJSON.getGameObject(GameObjects.TILE, game);
            PixmapDrawable component = tile.getComponent(ComponentType.Drawable);
            tile.setPosition(STARTING_TILE_POSITION_X + i * (component.width + DISTANCE_BETWEEN_TILES), STARTING_TILE_POSITION_Y);
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

    private float getXCoordinatesOfTileLeftEdge(int tileNumber){
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

    public void destroyCar() {
        this.gameObjects.remove(car.chassis);
        this.gameObjects.remove(car.backWheel);
        this.gameObjects.remove(car.frontWheel);
        car.destroy();
        state.nextState();
    }

    public void handleExplosion(GameObject go, Exploding component) {
        this.ropesBetweenTiles.remove(component.target);
        this.gameObjects.remove(go);
        this.newRopeCoordinates.setY(0);
        this.newRopeCoordinates.setX(0);
        state = new IdleState(this, 2.5f, new CheckWinState(this));
    }

    public void destroy(){
        WorldHandler.delete();
    }

}
