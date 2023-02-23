package com.gamedesign.notouching.level;

import com.gamedesign.notouching.component.ChassisEngine;
import com.gamedesign.notouching.component.ComponentPools;
import com.gamedesign.notouching.component.ComponentType;
import com.gamedesign.notouching.component.GameObject;
import com.gamedesign.notouching.component.PixmapDrawable;
import com.gamedesign.notouching.framework.Game;
import com.gamedesign.notouching.framework.Pixmap;
import com.gamedesign.notouching.util.Assets;
import com.gamedesign.notouching.util.GameObjectPool;
import com.gamedesign.notouching.util.GameObjects;
import com.gamedesign.notouching.world.WorldHandler;
import com.google.fpl.liquidfun.Joint;
import com.google.fpl.liquidfun.RevoluteJoint;
import com.google.fpl.liquidfun.RevoluteJointDef;

public class Car {

    public static final float WHEEL_TO_CHASSIS_DISTANCE = 0.55f;
    public GameObject chassis;
    public GameObject frontWheel;
    public GameObject backWheel;
    public Game game;
    private Level level;
    private float motorSpeed;


    public Car() {
    }

    public void initCar(Game game, float[] targetCoordinates, Level level, float motorSpeed, Pixmap chassisPixmap, Joint... bombTargets) {
        this.level = level;
        this.game = game;
        this.motorSpeed = motorSpeed;
        this.chassis = Assets.gameObjectsJSON.getGameObject(GameObjects.CHASSIS, game);
        this.backWheel = Assets.gameObjectsJSON.getGameObject(GameObjects.WHEEL, game);
        this.frontWheel = Assets.gameObjectsJSON.getGameObject(GameObjects.WHEEL, game);
        PixmapDrawable chassisDrawable = chassis.getComponent(ComponentType.Drawable);
        chassisDrawable.pixmap = chassisPixmap;
        RevoluteJointDef firstWheelJointDef = new RevoluteJointDef();
        firstWheelJointDef.setBodyA(chassis.getBody());
        firstWheelJointDef.setBodyB(frontWheel.getBody());
        firstWheelJointDef.setLocalAnchorA((chassisDrawable.width / 4 + WHEEL_TO_CHASSIS_DISTANCE), chassisDrawable.height + WHEEL_TO_CHASSIS_DISTANCE);
        firstWheelJointDef.setLocalAnchorB(0, 0);
        firstWheelJointDef.setCollideConnected(true);
        firstWheelJointDef.setEnableMotor(true);
        firstWheelJointDef.setMaxMotorTorque(100f);
        RevoluteJoint frontWheelJoint = WorldHandler.createJoint(firstWheelJointDef);

        RevoluteJointDef backWheelJointDef = new RevoluteJointDef();
        backWheelJointDef.setBodyA(chassis.getBody());
        backWheelJointDef.setBodyB(backWheel.getBody());
        backWheelJointDef.setLocalAnchorA(-(chassisDrawable.width / 4 + WHEEL_TO_CHASSIS_DISTANCE), chassisDrawable.height + WHEEL_TO_CHASSIS_DISTANCE);
        backWheelJointDef.setLocalAnchorB(0, 0);
        backWheelJointDef.setCollideConnected(true);
        backWheelJointDef.setEnableMotor(true);
        backWheelJointDef.setMaxMotorTorque(100f);
        RevoluteJoint backWheelJoint = WorldHandler.createJoint(backWheelJointDef);
        ChassisEngine chassisEngine = (ChassisEngine) ComponentPools.getNewInstance(ChassisEngine.class);
        chassisEngine.targetCoordinates = targetCoordinates;
        chassisEngine.backWheelJoint = backWheelJoint;
        chassisEngine.frontWheelJoint = frontWheelJoint;
        chassisEngine.bombTargets = bombTargets;
        chassis.addComponent(chassisEngine);
    }

    public void move() {
        ChassisEngine engine = chassis.getComponent(ComponentType.AI);
        engine.move(motorSpeed, level);
    }

    public void destroy() {
        ChassisEngine engine = chassis.getComponent(ComponentType.AI);
        engine.isPlaying = false;
        if (engine.bombEjectedIndex != engine.targetCoordinates.length) {
            engine.ejectBomb(level);
        }
        chassis.getBody().setActive(false);
        frontWheel.getBody().setActive(false);
        backWheel.getBody().setActive(false);
        level.gameObjects.remove(chassis);
        level.gameObjects.remove(frontWheel);
        level.gameObjects.remove(backWheel);
        level.state.nextState();
        Assets.engine.stop();
    }

    public void freeObjects(){
        GameObjectPool.freeGameObject(chassis);
        GameObjectPool.freeGameObject(frontWheel);
        GameObjectPool.freeGameObject(backWheel);
    }

    public boolean isPlaying(){
        ChassisEngine component = chassis.getComponent(ComponentType.AI);
        if(component != null)
            return component.isPlaying;
        else return false;
    }

    public boolean isLost() {
        return chassis.getBody().getPosition().getY() >= 40;
    }
    
    public int getTotalBombs(){
        ChassisEngine component = chassis.getComponent(ComponentType.AI);
        if(component != null){
            return component.targetCoordinates.length;
        }
        else {
            return 0;
        }
    }

}
