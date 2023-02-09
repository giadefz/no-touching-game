package com.gamedesign.notouching;

import static com.gamedesign.notouching.util.ScreenInfo.SCALING_FACTOR;

import com.gamedesign.notouching.component.BoxDrawable;
import com.gamedesign.notouching.component.ComponentType;
import com.gamedesign.notouching.component.GameObject;
import com.gamedesign.notouching.framework.Game;
import com.gamedesign.notouching.util.Assets;
import com.gamedesign.notouching.util.GameObjects;
import com.gamedesign.notouching.world.WorldHandler;
import com.google.fpl.liquidfun.PrismaticJoint;
import com.google.fpl.liquidfun.PrismaticJointDef;
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
    private Level level;
    private float motorSpeed;


    public Car(Game game, float targetCoordinate, Level level, float motorSpeed) {
        this.targetCoordinate = targetCoordinate;
        this.level = level;
        this.game = game;
        this.motorSpeed = motorSpeed;
        this.chassis = Assets.gameObjectsJSON.getGameObject(GameObjects.CHASSIS, game);
        this.backWheel = Assets.gameObjectsJSON.getGameObject(GameObjects.WHEEL, game);
        this.frontWheel = Assets.gameObjectsJSON.getGameObject(GameObjects.WHEEL, game);
        BoxDrawable chassisDrawable = chassis.getComponent(ComponentType.Drawable);

        RevoluteJointDef firstWheelJointDef = new RevoluteJointDef();
        firstWheelJointDef.setBodyA(chassis.getBody());
        firstWheelJointDef.setBodyB(frontWheel.getBody());
        firstWheelJointDef.setLocalAnchorA((chassisDrawable.width / 4 + WHEEL_TO_CHASSIS_DISTANCE),  chassisDrawable.height + WHEEL_TO_CHASSIS_DISTANCE);
        firstWheelJointDef.setLocalAnchorB(0, 0);
        firstWheelJointDef.setCollideConnected(true);
        firstWheelJointDef.setEnableMotor(true);
        firstWheelJointDef.setMaxMotorTorque(30f);
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
            if ((chassis.getBody().getWorldCenter().getX() * SCALING_FACTOR - targetCoordinate) < - 50) {
                frontWheelJoint.setMotorSpeed(motorSpeed);
                backWheelJoint.setMotorSpeed(motorSpeed);
            } else if ((chassis.getBody().getWorldCenter().getX() * SCALING_FACTOR - targetCoordinate) > 50) {
                frontWheelJoint.setMotorSpeed(-motorSpeed);
                backWheelJoint.setMotorSpeed(-motorSpeed);
            } else {
                frontWheelJoint.setMotorSpeed(0);
                backWheelJoint.setMotorSpeed(0);
                chassis.getBody().setLinearVelocity(new Vec2(0, 0));
                stopped = true;
                ejectBomb();
            }
        }

    }

    private void ejectBomb() {
        GameObject bomb = Assets.gameObjectsJSON.getGameObject(GameObjects.BOMB, game);
        Vec2 chassisCoordinates = chassis.getBody().getWorldCenter();
        bomb.setPosition(chassisCoordinates.getX(), chassisCoordinates.getY() + 4f);
        level.addGameObject(bomb);
        this.targetCoordinate = 3000f;
        stopped = false;
        this.motorSpeed = 10f;
    }

}

//    PrismaticJointDef axlePrismaticJointDef = new PrismaticJointDef();
//        axlePrismaticJointDef.setLowerTranslation(-1f);
//                axlePrismaticJointDef.setUpperTranslation(0.3f);
//                axlePrismaticJointDef.setLocalAxisA(0,-1);
//                axlePrismaticJointDef.setEnableLimit(true);
//                axlePrismaticJointDef.setEnableMotor(true);
//                axlePrismaticJointDef.setCollideConnected(false);
//                axlePrismaticJointDef.setMaxMotorForce(0.5f);
//                axlePrismaticJointDef.setMotorSpeed(1f);
//                // front axle
//                axlePrismaticJointDef.setBodyA(chassis.getBody());
//                axlePrismaticJointDef.setBodyB(frontWheel.getBody());
//                axlePrismaticJointDef.setLocalAnchorA( chassisDrawable.width / 4,  chassisDrawable.height + 0.55f);
//                axlePrismaticJointDef.setLocalAnchorB(0, 0);
//                frontAxlePrismaticJoint = WorldHandler.createJoint(axlePrismaticJointDef);
//                // rear axle
//                axlePrismaticJointDef.setBodyA(chassis.getBody());
//                axlePrismaticJointDef.setBodyB(backWheel.getBody());
//                axlePrismaticJointDef.setLocalAnchorA(- chassisDrawable.width / 4,  chassisDrawable.height + 0.55f);
//                axlePrismaticJointDef.setLocalAnchorB(0, 0);
//                rearAxlePrismaticJoint = WorldHandler.createJoint(axlePrismaticJointDef);
