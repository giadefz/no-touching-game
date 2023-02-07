package com.gamedesign.notouching;

import com.gamedesign.notouching.component.BoxDrawable;
import com.gamedesign.notouching.component.Component;
import com.gamedesign.notouching.component.ComponentType;
import com.gamedesign.notouching.component.GameObject;
import com.gamedesign.notouching.framework.Game;
import com.gamedesign.notouching.util.Assets;
import com.gamedesign.notouching.util.GameObjects;
import com.google.fpl.liquidfun.Joint;
import com.google.fpl.liquidfun.RevoluteJoint;
import com.google.fpl.liquidfun.RevoluteJointDef;

public class Car {

    public GameObject chassis;
    public GameObject frontWheel;
    public GameObject backWheel;
    public RevoluteJoint frontWheelJoint;
    public RevoluteJoint backWheelJoint;


    public Car(Game game) {
        this.chassis = Assets.gameObjectsJSON.getGameObject(GameObjects.CHASSIS, game);
        this.backWheel = Assets.gameObjectsJSON.getGameObject(GameObjects.WHEEL, game);
        this.frontWheel = Assets.gameObjectsJSON.getGameObject(GameObjects.WHEEL, game);
        BoxDrawable chassisDrawable = chassis.getComponent(ComponentType.Drawable);

        RevoluteJointDef firstWheelJointDef = new RevoluteJointDef();
        firstWheelJointDef.setBodyA(chassis.getBody());
        firstWheelJointDef.setBodyB(frontWheel.getBody());
        firstWheelJointDef.setLocalAnchorA(- chassisDrawable.width / 4,  chassisDrawable.height + 0.05f);
        firstWheelJointDef.setLocalAnchorB(0, 0);
        firstWheelJointDef.setEnableMotor(true);
        firstWheelJointDef.setCollideConnected(true);
        firstWheelJointDef.setMaxMotorTorque(50f);
        frontWheelJoint = game.getWorld().createJoint(firstWheelJointDef);

        RevoluteJointDef backWheelJointDef = new RevoluteJointDef();
        backWheelJointDef.setBodyA(chassis.getBody());
        backWheelJointDef.setBodyB(backWheel.getBody());
        backWheelJointDef.setLocalAnchorA(chassisDrawable.width / 4,  chassisDrawable.height + 0.05f);
        backWheelJointDef.setLocalAnchorB(0, 0);
        backWheelJointDef.setCollideConnected(true);
        backWheelJointDef.setEnableMotor(true);
        backWheelJointDef.setMaxMotorTorque(50f);
        backWheelJoint = game.getWorld().createJoint(backWheelJointDef);
    }

    public void move(float motorSpeed){
        frontWheelJoint.setMotorSpeed(motorSpeed);
        backWheelJoint.setMotorSpeed(motorSpeed);
    }

}
