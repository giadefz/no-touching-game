package com.gamedesign.notouching.touch;

import static com.gamedesign.notouching.util.ScreenInfo.HALF_SCREEN;
import static com.gamedesign.notouching.util.ScreenInfo.Y_DISTANCE_FROM_FINGER;

import android.util.Log;

import com.gamedesign.notouching.level.Level;
import com.gamedesign.notouching.level.Rope;
import com.gamedesign.notouching.component.ComponentType;
import com.gamedesign.notouching.component.Drawable;
import com.gamedesign.notouching.component.GameObject;
import com.gamedesign.notouching.framework.Input;
import com.gamedesign.notouching.framework.TouchConsumer;
import com.gamedesign.notouching.util.ScreenInfo;
import com.gamedesign.notouching.world.WorldHandler;
import com.google.fpl.liquidfun.Joint;
import com.google.fpl.liquidfun.RopeJointDef;
import com.google.fpl.liquidfun.Vec2;

public class LevelTouchConsumer extends TouchConsumer {

    public int pierIndex;
    public Level level;
    private final float secondPierXCoordinate;
    private final float firstPierXCoordinate;
    private final float pierYCoordinate;
    private final float pierHalfHeight;

    public LevelTouchConsumer(Level level, float secondPierXCoordinate, float firstPierXCoordinate, float pierYCoordinate, float pierHalfHeight, int pierIndex) {
        super(event -> event.y = event.y + Y_DISTANCE_FROM_FINGER);
        this.level = level;
        this.pierIndex = pierIndex;
        this.secondPierXCoordinate = secondPierXCoordinate;
        this.firstPierXCoordinate = firstPierXCoordinate;
        this.pierYCoordinate = pierYCoordinate;
        this.pierHalfHeight = pierHalfHeight;
    }

    @Override
    protected void handleTouchDown(Input.TouchEvent event) {
        if (event.x > HALF_SCREEN)
            level.startingPointCoordinates.setX(secondPierXCoordinate);
        else
            level.startingPointCoordinates.setX(firstPierXCoordinate);
        level.startingPointCoordinates.setY(pierYCoordinate);
        level.newRopeCoordinates.setX(event.x);
        level.newRopeCoordinates.setY(event.y);
    }

    @Override
    protected void handleTouchUp(Input.TouchEvent event) {
        level.newRopeCoordinates.setX(0);
        level.newRopeCoordinates.setY(0);
        GameObject gameObjectWithinBound = level.getGameObjectWithinBound(event);
        if (gameObjectWithinBound != null) {
            Log.println(Log.ASSERT, "TOUCH", gameObjectWithinBound.toString());
            Rope newRope = createNewRope(event, gameObjectWithinBound);
            level.addNewRope(newRope);
        }
    }

    @Override
    protected void handleTouchDragged(Input.TouchEvent event) {
        level.newRopeCoordinates.setX(event.x);
        level.newRopeCoordinates.setY(event.y);
    }

    private Rope createNewRope(Input.TouchEvent event, GameObject destinationTile) {
        RopeJointDef jointDef = new RopeJointDef();
        if (level.startingPointCoordinates.getX() > HALF_SCREEN)
            jointDef.setBodyA(level.getGameObject(pierIndex + 1).getBody());
        else
            jointDef.setBodyA(level.getGameObject(pierIndex).getBody());
        jointDef.setBodyB(destinationTile.getBody());
        jointDef.setLocalAnchorA(0, pierHalfHeight);
        Drawable drawable = destinationTile.getComponent(ComponentType.Drawable);
        Vec2 localCoordinatesFromWorldCoordinates = drawable.getLocalCoordinatesFromWorldCoordinates(event.x, event.y);
        jointDef.setLocalAnchorB(localCoordinatesFromWorldCoordinates.getX(), localCoordinatesFromWorldCoordinates.getY());
        float xDiff = event.x - level.startingPointCoordinates.getX();
        float yDiff = event.y - level.startingPointCoordinates.getY();
        jointDef.setMaxLength((((float) Math.sqrt(xDiff * xDiff + yDiff * yDiff)) / ScreenInfo.SCALING_FACTOR) + 0.2f);
        jointDef.setCollideConnected(true);
        Joint joint = WorldHandler.createJoint(jointDef);
        return new Rope(joint, localCoordinatesFromWorldCoordinates.getX(), localCoordinatesFromWorldCoordinates.getY());
    }

}
