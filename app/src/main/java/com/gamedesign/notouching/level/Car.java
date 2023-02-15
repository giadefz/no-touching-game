package com.gamedesign.notouching.level;

import static com.gamedesign.notouching.util.ScreenInfo.SCALING_FACTOR;

import com.gamedesign.notouching.component.CirclePixmapDrawable;
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
    public float[] targetCoordinates;
    private boolean stopped;
    private final Level level;
    private final float motorSpeed;
    private final Joint[] bombTargets;
    private final Vec2 vec2 = new Vec2();
    private int bombEjectedIndex = 0;
    private boolean isPlaying = false;


    public Car(Game game, float[] targetCoordinates, Level level, float motorSpeed, Joint... bombTargets) {
        this.targetCoordinates = targetCoordinates;
        this.level = level;
        this.game = game;
        this.motorSpeed = motorSpeed;
        this.chassis = Assets.gameObjectsJSON.getGameObject(GameObjects.CHASSIS, game);
        this.backWheel = Assets.gameObjectsJSON.getGameObject(GameObjects.WHEEL, game);
        this.frontWheel = Assets.gameObjectsJSON.getGameObject(GameObjects.WHEEL, game);
        this.bombTargets = bombTargets;
        PixmapDrawable chassisDrawable = chassis.getComponent(ComponentType.Drawable);

        RevoluteJointDef firstWheelJointDef = new RevoluteJointDef();
        firstWheelJointDef.setBodyA(chassis.getBody());
        firstWheelJointDef.setBodyB(frontWheel.getBody());
        firstWheelJointDef.setLocalAnchorA((chassisDrawable.width / 4 + WHEEL_TO_CHASSIS_DISTANCE), chassisDrawable.height + WHEEL_TO_CHASSIS_DISTANCE);
        firstWheelJointDef.setLocalAnchorB(0, 0);
        firstWheelJointDef.setCollideConnected(true);
        firstWheelJointDef.setEnableMotor(true);
        firstWheelJointDef.setMaxMotorTorque(100f);
        frontWheelJoint = WorldHandler.createJoint(firstWheelJointDef);

        RevoluteJointDef backWheelJointDef = new RevoluteJointDef();
        backWheelJointDef.setBodyA(chassis.getBody());
        backWheelJointDef.setBodyB(backWheel.getBody());
        backWheelJointDef.setLocalAnchorA(-(chassisDrawable.width / 4 + WHEEL_TO_CHASSIS_DISTANCE), chassisDrawable.height + WHEEL_TO_CHASSIS_DISTANCE);
        backWheelJointDef.setLocalAnchorB(0, 0);
        backWheelJointDef.setCollideConnected(true);
        backWheelJointDef.setEnableMotor(true);
        backWheelJointDef.setMaxMotorTorque(100f);
        backWheelJoint = WorldHandler.createJoint(backWheelJointDef);
    }

    public void move() {
        if(!isPlaying){
            Assets.engine.setLooping(true);
            Assets.engine.setVolume(0.5f);
            Assets.engine.play();
            isPlaying = true;
        }
        float targetCoordinate;
        if (this.targetCoordinates.length == bombEjectedIndex) {
            targetCoordinate = 3000f;
        } else {
            targetCoordinate = this.targetCoordinates[bombEjectedIndex];
        }
        CirclePixmapDrawable frontWheelComponent = frontWheel.getComponent(ComponentType.Drawable);
        GameObject secondPier = level.gameObjects.get(level.PIER_INDEX + 1);
        PixmapDrawable secondPierComponent = secondPier.getComponent(ComponentType.Drawable);

        if ((chassis.getBody().getWorldCenter().getX() * SCALING_FACTOR - targetCoordinate) < -50) {
            frontWheelJoint.setMotorSpeed(motorSpeed);
            backWheelJoint.setMotorSpeed(motorSpeed);
        } else if ((chassis.getBody().getWorldCenter().getX() * SCALING_FACTOR - targetCoordinate) > 50) {
            frontWheelJoint.setMotorSpeed(-motorSpeed);
            backWheelJoint.setMotorSpeed(-motorSpeed);
        } else {
            frontWheelJoint.setMotorSpeed(motorSpeed);
            backWheelJoint.setMotorSpeed(0);
            vec2.setY(0);
            vec2.setX(0);
            stopped = true;
            ejectBomb(bombEjectedIndex);
        }


    }

    private void ejectBomb(int bombIndex) {
        GameObject bomb = Assets.gameObjectsJSON.getGameObject(GameObjects.BOMB, game);
        PixmapDrawable bombDrawable = bomb.getComponent(ComponentType.Drawable);
        Exploding component = bomb.getComponent(ComponentType.Exploding);
        component.timeUntilIgnition = level.timeUntilBombIgnition;
        component.setTarget(bombTargets[bombIndex]);
        PixmapDrawable chassisDrawable = chassis.getComponent(ComponentType.Drawable);
        vec2.setX(-(chassisDrawable.width / 2 + bombDrawable.width));
        vec2.setY(0);
        Vec2 chassisCoordinates = chassis.getBody().getWorldPoint(vec2);
        bomb.setPosition(chassisCoordinates.getX(), chassisCoordinates.getY() + 4f);
        level.addGameObject(bomb);
        stopped = false;
        bombEjectedIndex++;
    }

    public void destroy() {
        if (bombEjectedIndex != this.targetCoordinates.length) {
            ejectBomb(bombEjectedIndex);
        }
        chassis.getBody().setActive(false);
        frontWheel.getBody().setActive(false);
        backWheel.getBody().setActive(false);
        level.gameObjects.remove(chassis);
        level.gameObjects.remove(frontWheel);
        level.gameObjects.remove(backWheel);
        level.state.nextState();
        Assets.engine.stop();
        stopped = true;
    }

    public boolean isLost() {
        return chassis.getBody().getPosition().getY() >= 40;
    }

}
