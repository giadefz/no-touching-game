package com.gamedesign.notouching.level;

import static com.gamedesign.notouching.util.ScreenInfo.SCALING_FACTOR;

import com.gamedesign.notouching.component.BoxDrawable;
import com.gamedesign.notouching.component.CircleDrawable;
import com.gamedesign.notouching.component.Component;
import com.gamedesign.notouching.component.ComponentType;
import com.gamedesign.notouching.component.Exploding;
import com.gamedesign.notouching.component.GameObject;
import com.gamedesign.notouching.component.PixmapDrawable;
import com.gamedesign.notouching.framework.Game;
import com.gamedesign.notouching.util.Assets;
import com.gamedesign.notouching.util.GameObjects;
import com.gamedesign.notouching.world.WorldHandler;
import com.google.fpl.liquidfun.Joint;
import com.google.fpl.liquidfun.PrismaticJoint;
import com.google.fpl.liquidfun.RevoluteJoint;
import com.google.fpl.liquidfun.RevoluteJointDef;
import com.google.fpl.liquidfun.Vec2;

public class Car {

    public static final float WHEEL_TO_CHASSIS_DISTANCE = 0.55f;
    public GameObject chassis;
    public GameObject frontWheel;
    public GameObject backWheel;
    public RevoluteJoint frontWheelJoint;
    public RevoluteJoint backWheelJoint;
    public PrismaticJoint frontAxlePrismaticJoint;
    public PrismaticJoint rearAxlePrismaticJoint;
    public Game game;
    private float targetCoordinate;
    private boolean stopped;
    private final Level level;
    private final float motorSpeed;
    private float decreasingMotorSpeed;
    private final Joint bombTarget;
    private final Vec2 vec2 = new Vec2();
    private final Vec2 vec = new Vec2();


    public Car(Game game, float targetCoordinate, Level level, float motorSpeed, Joint bombTarget) {
        this.targetCoordinate = targetCoordinate;
        this.level = level;
        this.game = game;
        this.motorSpeed = motorSpeed;
        this.chassis = Assets.gameObjectsJSON.getGameObject(GameObjects.CHASSIS, game);
        this.backWheel = Assets.gameObjectsJSON.getGameObject(GameObjects.WHEEL, game);
        this.frontWheel = Assets.gameObjectsJSON.getGameObject(GameObjects.WHEEL, game);
        this.bombTarget = bombTarget;
        PixmapDrawable chassisDrawable = chassis.getComponent(ComponentType.Drawable);

        RevoluteJointDef firstWheelJointDef = new RevoluteJointDef();
        firstWheelJointDef.setBodyA(chassis.getBody());
        firstWheelJointDef.setBodyB(frontWheel.getBody());
        firstWheelJointDef.setLocalAnchorA((chassisDrawable.width / 4 + WHEEL_TO_CHASSIS_DISTANCE),  chassisDrawable.height + WHEEL_TO_CHASSIS_DISTANCE);
        firstWheelJointDef.setLocalAnchorB(0, 0);
        firstWheelJointDef.setCollideConnected(true);
        firstWheelJointDef.setEnableMotor(true);
        firstWheelJointDef.setMaxMotorTorque(100f);
        frontWheelJoint = WorldHandler.createJoint(firstWheelJointDef);

        RevoluteJointDef backWheelJointDef = new RevoluteJointDef();
        backWheelJointDef.setBodyA(chassis.getBody());
        backWheelJointDef.setBodyB(backWheel.getBody());
        backWheelJointDef.setLocalAnchorA(-(chassisDrawable.width / 4 + WHEEL_TO_CHASSIS_DISTANCE),  chassisDrawable.height + WHEEL_TO_CHASSIS_DISTANCE);
        backWheelJointDef.setLocalAnchorB(0, 0);
        backWheelJointDef.setCollideConnected(true);
        backWheelJointDef.setEnableMotor(true);
        backWheelJointDef.setMaxMotorTorque(100f);
        backWheelJoint = WorldHandler.createJoint(backWheelJointDef);
    }

    public void move(){
        if(!stopped) {
            CircleDrawable frontWheelComponent = frontWheel.getComponent(ComponentType.Drawable);
            GameObject lastTile = level.gameObjects.get(level.TILES_NUMBER - 1);
            PixmapDrawable lastTileComponent = lastTile.getComponent(ComponentType.Drawable);

            if ((chassis.getBody().getWorldCenter().getX() * SCALING_FACTOR - targetCoordinate) < - 50) {
                frontWheelJoint.setMotorSpeed(motorSpeed);
                backWheelJoint.setMotorSpeed(motorSpeed);
            } else if ((chassis.getBody().getWorldCenter().getX() * SCALING_FACTOR - targetCoordinate) > 50) {
                frontWheelJoint.setMotorSpeed(-motorSpeed);
                backWheelJoint.setMotorSpeed(-motorSpeed);
            } else {
                frontWheelJoint.setMotorSpeed(motorSpeed);
                backWheelJoint.setMotorSpeed(0);
                vec2.setY(0); vec2.setX(0);
                stopped = true;
                ejectBomb();
            }
            vec2.setY(0); vec2.setX(frontWheelComponent.radius);
            vec.setY(0); vec.setX(lastTileComponent.width / 2);
            if(frontWheel.getBody().getWorldPoint(vec2).getX() - lastTile.getBody().getWorldPoint(vec).getX() < 0.2 && frontWheel.getBody().getWorldPoint(vec2).getX() - lastTile.getBody().getWorldPoint(vec).getX() > 0 ){
                this.destroy();
                stopped = true;
            }
        }

    }

    private void ejectBomb() {
        GameObject bomb = Assets.gameObjectsJSON.getGameObject(GameObjects.BOMB, game);
        BoxDrawable bombDrawable = bomb.getComponent(ComponentType.Drawable);
        Exploding component = bomb.getComponent(ComponentType.Exploding);
        component.setTarget(bombTarget);
        BoxDrawable chassisDrawable = chassis.getComponent(ComponentType.Drawable);
        vec2.setX(- (chassisDrawable.width / 2 + bombDrawable.width));
        vec2.setY(0);
        Vec2 chassisCoordinates = chassis.getBody().getWorldPoint(vec2);
        bomb.setPosition(chassisCoordinates.getX(), chassisCoordinates.getY() + 4f);
        level.addGameObject(bomb);
        this.targetCoordinate = 3000f;
        stopped = false;
    }

    public void destroy() {
        WorldHandler.destroyBody(chassis.getBody());
        WorldHandler.destroyBody(frontWheel.getBody());
        WorldHandler.destroyBody(backWheel.getBody());
        level.gameObjects.remove(chassis);
        level.gameObjects.remove(frontWheel);
        level.gameObjects.remove(backWheel);
        level.state.nextState();
    }

    public boolean isLost(){
        return chassis.getBody().getPosition().getY() >= 40;
    }

}
