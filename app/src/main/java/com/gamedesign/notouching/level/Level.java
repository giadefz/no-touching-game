package com.gamedesign.notouching.level;

import static com.gamedesign.notouching.level.states.LevelStates.START;
import static com.gamedesign.notouching.screen.GameScreen.FIRST_PIER_X_COORDINATE;
import static com.gamedesign.notouching.screen.GameScreen.PIER_Y_COORDINATE;

import android.graphics.Color;

import com.gamedesign.notouching.component.ComponentType;
import com.gamedesign.notouching.component.Drawable;
import com.gamedesign.notouching.component.Exploding;
import com.gamedesign.notouching.component.GameObject;
import com.gamedesign.notouching.component.PixmapDrawable;
import com.gamedesign.notouching.component.Position;
import com.gamedesign.notouching.framework.Game;
import com.gamedesign.notouching.framework.Input;
import com.gamedesign.notouching.framework.Pixmap;
import com.gamedesign.notouching.level.difficultysettings.LevelDifficultySettings;
import com.gamedesign.notouching.level.difficultysettings.LevelDifficultySettingsFactory;
import com.gamedesign.notouching.level.states.LevelState;
import com.gamedesign.notouching.util.Assets;
import com.gamedesign.notouching.util.GameObjectPool;
import com.gamedesign.notouching.util.GameObjects;
import com.gamedesign.notouching.util.RopePool;
import com.gamedesign.notouching.util.TileBuilder;
import com.gamedesign.notouching.world.WorldHandler;
import com.google.fpl.liquidfun.Joint;
import com.google.fpl.liquidfun.RopeJointDef;
import com.google.fpl.liquidfun.Vec2;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Level {
    private static final int STARTING_TILE_POSITION_X = 5;
    private static final int DISTANCE_TO_COVER = 42;

    private static final float STARTING_TILE_POSITION_Y = 18;
    private static final float PIER_DISTANCE = 55;


    public static final float PIER_HALF_HEIGHT = 12.775f;
    public static final int ROPE_COLOR = Color.argb(200, 101, 67, 33);
    public static final int NEW_ROPE_COLOR = Color.BLACK;
    public static final int ROPE_COST = 1000;
    public int MAX_TILE_LENGTH;
    public int MIN_TILE_LENGTH;
    public int TILES_NUMBER;
    public int PIER_INDEX;
    public final List<GameObject> gameObjects;
    public final List<Joint> ropesBetweenTiles;
    public final List<Rope> addedRopes;
    public Joint firstRopeFromPier;
    public Joint secondRopeFromPier;
    public final Game game;
    public static Car car = new Car();
    public Vec2 newRopeCoordinates = new Vec2();
    public Vec2 startingPointCoordinates = new Vec2();
    private final Vec2 temp = new Vec2();
    private final Vec2 temp2 = new Vec2();
    public LevelState state;
    public Random random;
    public float timeBombStopped;
    public Pixmap backGround;
    public long seed;
    public int ropeBudget;
    public int totalPoints;
    public int levelNumber;
    public float timeUntilBombIgnition;
    public LevelDifficultySettings difficultySettings;

    public Level(Game game, long seed, int totalPoints, int levelNumber) {
        this.gameObjects = new ArrayList<>();
        this.ropesBetweenTiles = new ArrayList<>();
        this.addedRopes = new ArrayList<>();
        this.game = game;
        this.seed = seed;
        this.levelNumber = levelNumber;
        setUpLevel(totalPoints);
    }

    public void setUpLevel(int totalPoints) {
        this.totalPoints = totalPoints;
        this.random = new Random(seed);
        this.backGround = Assets.getBackgroundByIndex(random.nextInt(3));
        this.newRopeCoordinates.setX(0); this.newRopeCoordinates.setY(0);
        this.startingPointCoordinates.setX(FIRST_PIER_X_COORDINATE); this.startingPointCoordinates.setY(PIER_Y_COORDINATE);
        this.levelNumber++;
        this.difficultySettings = LevelDifficultySettingsFactory.getDifficultySettings(this);
        this.ropeBudget = difficultySettings.getRopeBudget();
        this.MAX_TILE_LENGTH = this.difficultySettings.getMaxTileLength();
        this.MIN_TILE_LENGTH = this.difficultySettings.getMinTileLength();
        this.timeUntilBombIgnition = this.difficultySettings.getTimeUntilIgnition();
        START.initializeState(this);
        this.state = START;
        setUpTiles();
        setUpPiers();
        difficultySettings.initCar(); //deve essere chiamato dopo che viene inizializzato nel livello TILES_NUMBER
        this.addCar();
    }

    public synchronized GameObject addGameObject(GameObject obj) {
        gameObjects.add(obj);
        return obj;
    }

    public void addCar() {
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

    public void updateLevel(float deltaTime) {
        state.updateLevel(deltaTime);
    }

    private void setUpTiles(){
        int distanceToCover = DISTANCE_TO_COVER;
        int randWidth = MAX_TILE_LENGTH != MIN_TILE_LENGTH ? random.nextInt(MAX_TILE_LENGTH - MIN_TILE_LENGTH) + MIN_TILE_LENGTH : MAX_TILE_LENGTH;
        distanceToCover -= randWidth;
        String tilePixmapNameByIndex = Assets.getTilePixmapNameByIndex(random.nextInt(3));
        GameObject firstTile = TileBuilder.buildTile(randWidth, game, tilePixmapNameByIndex);
        firstTile.setPosition(STARTING_TILE_POSITION_X + (randWidth / 2f), STARTING_TILE_POSITION_Y);
        firstTile = this.addGameObject(firstTile);
        int i = 1;
        while (distanceToCover > 0){
            randWidth = MAX_TILE_LENGTH != MIN_TILE_LENGTH ? random.nextInt(MAX_TILE_LENGTH - MIN_TILE_LENGTH) + MIN_TILE_LENGTH : MAX_TILE_LENGTH;
            PixmapDrawable firstTileComponent = firstTile.getComponent(ComponentType.Drawable);
            if(randWidth > distanceToCover){
                randWidth = distanceToCover;
            }
            distanceToCover -= randWidth;
            if(distanceToCover < MIN_TILE_LENGTH){
                randWidth += distanceToCover;
                distanceToCover = 0;
            }
            GameObject tile = TileBuilder.buildTile(randWidth, game, tilePixmapNameByIndex);
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
        Pixmap pierPixmapByIndex = Assets.getPierPixmapByIndex(random.nextInt(2));
        GameObject firstPier = this.addGameObject(Assets.gameObjectsJSON.getGameObject(GameObjects.PIER, game));
        PixmapDrawable firstPierComponent = firstPier.getComponent(ComponentType.Drawable);
        firstPierComponent.pixmap = pierPixmapByIndex;
        GameObject secondPier = Assets.gameObjectsJSON.getGameObject(GameObjects.PIER, game);
        PixmapDrawable secondPierComponent = secondPier.getComponent(ComponentType.Drawable);
        secondPierComponent.pixmap = pierPixmapByIndex;
        GameObject firstTile = this.getGameObject(0);
        PixmapDrawable firstTileDrawable = firstTile.getComponent(ComponentType.Drawable);
        GameObject lastTile = this.getGameObject(TILES_NUMBER - 1);
        PixmapDrawable lastTileDrawable = lastTile.getComponent(ComponentType.Drawable);
        Position firstPierPosition = firstPier.getComponent(ComponentType.Position);
        secondPier.setPosition(firstPierPosition.x + PIER_DISTANCE, firstPierPosition.y);
        secondPier = this.addGameObject(secondPier);
        Joint firstJoint = setRopeBetweenPierAndTile(firstPier, firstTile, - firstTileDrawable.width / 2, 0.3f);
        Joint secondJoint = setRopeBetweenPierAndTile(secondPier, lastTile, lastTileDrawable.width / 2, 0.3f);
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
        jointDef.setMaxLength(diff + 0.03f);
        jointDef.setCollideConnected(true);
        return WorldHandler.createJoint(jointDef);
    }

    public synchronized void moveCar(){
        if(car != null){
            car.move();
        }
    }

    public synchronized void addNewRope(Rope newRope) {
        this.addedRopes.add(newRope);
        this.ropeBudget -= ROPE_COST;
    }

    public void handleExplosion(GameObject go, Exploding component) {
        this.ropesBetweenTiles.remove(component.target);
        this.newRopeCoordinates.setY(0);
        this.newRopeCoordinates.setX(0);
    }

    public void destroy(){
        WorldHandler.delete();
        this.gameObjects.clear();
        this.ropesBetweenTiles.clear();
        this.addedRopes.clear();
        car.freeObjects();
        RopePool.freeRopes(this.addedRopes);
        GameObjectPool.freeGameObjects(this.gameObjects);
    }

}
