package com.gamedesign.notouching.level;

import static com.gamedesign.notouching.util.ScreenInfo.SCALING_FACTOR;

import android.graphics.Color;
import android.util.SparseArray;

import com.gamedesign.notouching.R;
import com.gamedesign.notouching.component.Component;
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
import com.gamedesign.notouching.util.ScreenInfo;
import com.gamedesign.notouching.util.TileBuilder;
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
    public static final int STARTING_TILE_POSITION_X = 5;
    public static final int DISTANCE_TO_COVER = 42;
    public static final int MAX_TILE_LENGTH = 13;
    public static final int MIN_TILE_LENGTH = 3;
    public int TILES_NUMBER;
    public int PIER_INDEX;
    public static final float STARTING_TILE_POSITION_Y = 18;
    public static final float PIER_DISTANCE = 55;


    public final List<GameObject> gameObjects;
    public final List<Joint> ropesBetweenTiles;
    public final List<Rope> addedRopes;
    public Joint firstRopeFromPier;
    public Joint secondRopeFromPier;
    public final Game game;
    Car car;
    public Vec2 newRopeCoordinates;
    public Vec2 startingPointCoordinates;
    public final Vec2 temp = new Vec2();
    public final Vec2 temp2 = new Vec2();
    public LevelState state;
    public Random random;
    public float timeBombStopped;

    public Level(Game game) {
        this.gameObjects = new ArrayList<>();
        this.ropesBetweenTiles = new ArrayList<>();
        this.addedRopes = new ArrayList<>();

        this.game = game;

        this.newRopeCoordinates = new Vec2(0, 0);
        this.startingPointCoordinates = new Vec2(0, 0);

        this.state = new StartLevelState(this);
        this.random = new Random();

        setUpTiles();
        setUpPiers();

        int index = this.random.nextInt(TILES_NUMBER) + 1;
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
                .filter(gameObject -> gameObject.name.equals(GameObjects.TILE))
                .filter(gameObject  -> gameObject.<Drawable>getComponent(ComponentType.Drawable)
                        .isBodyWithinBounds(event))
                .findFirst()
                .orElse(null);
    }

    public synchronized void updateLevel(float deltaTime) {
        state.updateLevel(deltaTime);
    }

    private void setUpTiles(){
        int distanceToCover = DISTANCE_TO_COVER;
        int randWidth = random.nextInt(MAX_TILE_LENGTH - MIN_TILE_LENGTH) + MIN_TILE_LENGTH; //tiles between 3 and 10 width
//        int randWidth = 6; //tiles between 3 and 10 width
        distanceToCover -= randWidth;
        GameObject firstTile = TileBuilder.buildTile(randWidth, game);
        firstTile.setPosition(STARTING_TILE_POSITION_X + (randWidth / 2f), STARTING_TILE_POSITION_Y);
        firstTile = this.addGameObject(firstTile);
        int i = 1;
        while (distanceToCover > 0){
            randWidth = random.nextInt(7) + MIN_TILE_LENGTH;
            PixmapDrawable firstTileComponent = firstTile.getComponent(ComponentType.Drawable);
            if(randWidth > distanceToCover){
                randWidth = distanceToCover;
            }
            distanceToCover -= randWidth;
            if(distanceToCover < MIN_TILE_LENGTH){
                randWidth += distanceToCover;
                distanceToCover = 0;
            }
            GameObject tile = TileBuilder.buildTile(randWidth, game);
            PixmapDrawable tileComponent = tile.getComponent(ComponentType.Drawable);
            temp.setX(firstTileComponent.width / 2);
            temp.setY(0);
            tile.setPosition(firstTile.getBody().getWorldPoint(temp).getX() + (tileComponent.width / 2) + 1, STARTING_TILE_POSITION_Y);
            tile = this.addGameObject(tile);
            this.addRopeBetweenTiles(this.insertRopeBetweenTiles(firstTile, tile, firstTileComponent));
            firstTile = tile;
            i++;
        }
        TILES_NUMBER = i;
        PIER_INDEX = i;
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
        Joint firstJoint = setRopeBetweenPierAndTile(firstPier, firstTile, - firstTileDrawable.width / 2, 0);
        Joint secondJoint = setRopeBetweenPierAndTile(secondPier, lastTile, firstTileDrawable.width / 2, 0);
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
        temp.setX(xCoordinateOnTile);
        temp.setY(yCoordinateOnTile);
        float xDiff = pier.getBody().getPositionX() - tile.getBody().getWorldPoint(temp).getX();
        float yDiff = pier.getBody().getPositionY() - tile.getBody().getWorldPoint(temp).getY();
        float diff = (float) Math.sqrt(xDiff * xDiff - yDiff * yDiff);
        pierRope.setMaxLength(diff);
        return WorldHandler.createJoint(pierRope);
    }

    private Joint insertRopeBetweenTiles(GameObject firstTile, GameObject tile, PixmapDrawable firstTileComponent) {
        RopeJointDef jointDef = new RopeJointDef();
        PixmapDrawable tileComponent = tile.getComponent(ComponentType.Drawable);
        jointDef.setBodyA(firstTile.getBody());
        jointDef.setBodyB(tile.getBody());
        jointDef.setLocalAnchorA(firstTileComponent.width / 2, 0);
        jointDef.setLocalAnchorB(- tileComponent.width / 2, 0);
        temp.setX(firstTileComponent.width / 2);
        temp.setY(0);

        temp2.setX(-tileComponent.width / 2);
        temp2.setY(0);
        float xDiff = firstTile.getBody().getWorldPoint(temp).getX() - tile.getBody().getWorldPoint(temp2).getX();
        float yDiff = firstTile.getBody().getWorldPoint(temp).getY() - tile.getBody().getWorldPoint(temp2).getY();
        float diff = (float) Math.sqrt(xDiff * xDiff - yDiff * yDiff);
        jointDef.setMaxLength(diff);
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