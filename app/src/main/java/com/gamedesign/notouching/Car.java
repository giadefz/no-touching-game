package com.gamedesign.notouching;

import com.gamedesign.notouching.component.BoxDrawable;
import com.gamedesign.notouching.component.Component;
import com.gamedesign.notouching.component.ComponentType;
import com.gamedesign.notouching.component.GameObject;
import com.gamedesign.notouching.framework.Game;
import com.gamedesign.notouching.util.Assets;
import com.gamedesign.notouching.util.GameObjects;
import com.gamedesign.notouching.world.WorldHandler;
import com.google.fpl.liquidfun.Joint;
import com.google.fpl.liquidfun.PrismaticJoint;
import com.google.fpl.liquidfun.PrismaticJointDef;
import com.google.fpl.liquidfun.RevoluteJoint;
import com.google.fpl.liquidfun.RevoluteJointDef;
import com.google.fpl.liquidfun.World;

public class Car {

    public GameObject chassis;
    public GameObject frontWheel;
    public GameObject backWheel;
    public RevoluteJoint frontWheelJoint;
    public RevoluteJoint backWheelJoint;
    public GameObject frontAxle;
    public GameObject rearAxle;
    public Joint frontAxlePrismaticJoint;
    public Joint rearAxlePrismaticJoint;


    public Car(Game game) {
        this.chassis = Assets.gameObjectsJSON.getGameObject(GameObjects.CHASSIS, game);
        this.backWheel = Assets.gameObjectsJSON.getGameObject(GameObjects.WHEEL, game);
        this.frontWheel = Assets.gameObjectsJSON.getGameObject(GameObjects.WHEEL, game);
        this.frontAxle = Assets.gameObjectsJSON.getGameObject(GameObjects.AXLE, game);
        this.rearAxle = Assets.gameObjectsJSON.getGameObject(GameObjects.AXLE, game);
        BoxDrawable chassisDrawable = chassis.getComponent(ComponentType.Drawable);
        BoxDrawable frontAxleDrawable = frontAxle.getComponent(ComponentType.Drawable);
        BoxDrawable rearAxleDrawable = rearAxle.getComponent(ComponentType.Drawable);

        // ************************ PRISMATIC JOINTS ************************ //
        //  definition
        PrismaticJointDef axlePrismaticJointDef = new PrismaticJointDef();
        axlePrismaticJointDef.setLowerTranslation(-1f);
        axlePrismaticJointDef.setUpperTranslation(0.3f);
        axlePrismaticJointDef.setLocalAxisA(0,-1);
        axlePrismaticJointDef.setEnableLimit(true);
        axlePrismaticJointDef.setEnableMotor(true);
        axlePrismaticJointDef.setCollideConnected(false);
        axlePrismaticJointDef.setMaxMotorForce(0.5f);
        axlePrismaticJointDef.setMotorSpeed(1f);
        // front axle
        axlePrismaticJointDef.setBodyA(chassis.getBody());
        axlePrismaticJointDef.setBodyB(frontWheel.getBody());
        axlePrismaticJointDef.setLocalAnchorA( chassisDrawable.width / 4,  chassisDrawable.height + 0.55f);
        axlePrismaticJointDef.setLocalAnchorB(0, 0);
        frontAxlePrismaticJoint = WorldHandler.createJoint(axlePrismaticJointDef);
        // rear axle
        axlePrismaticJointDef.setBodyA(chassis.getBody());
        axlePrismaticJointDef.setBodyB(backWheel.getBody());
        axlePrismaticJointDef.setLocalAnchorA(- chassisDrawable.width / 4,  chassisDrawable.height + 0.55f);
        axlePrismaticJointDef.setLocalAnchorB(0, 0);
        rearAxlePrismaticJoint = WorldHandler.createJoint(axlePrismaticJointDef);

/*        RevoluteJointDef firstWheelJointDef = new RevoluteJointDef();
        firstWheelJointDef.setBodyA(chassis.getBody());
        firstWheelJointDef.setBodyB(frontWheel.getBody());
        firstWheelJointDef.setLocalAnchorA(chassisDrawable.width / 4,  chassisDrawable.height + 0.55f);
        firstWheelJointDef.setLocalAnchorB(0, 0);
        firstWheelJointDef.setEnableMotor(true);
        firstWheelJointDef.setCollideConnected(true);
        firstWheelJointDef.setMaxMotorTorque(50f);
        frontWheelJoint = game.getWorld().createJoint(firstWheelJointDef);

        RevoluteJointDef backWheelJointDef = new RevoluteJointDef();
        backWheelJointDef.setBodyA(chassis.getBody());
        backWheelJointDef.setBodyB(backWheel.getBody());
        backWheelJointDef.setLocalAnchorA(-chassisDrawable.width / 4,  chassisDrawable.height + 0.55f);
        backWheelJointDef.setLocalAnchorB(0, 0);
        backWheelJointDef.setCollideConnected(true);
        backWheelJointDef.setEnableMotor(true);
        backWheelJointDef.setMaxMotorTorque(50f);
        backWheelJoint = game.getWorld().createJoint(backWheelJointDef);*/
    }

    public void move(float motorSpeed){
        frontWheelJoint.setMotorSpeed(motorSpeed);
        backWheelJoint.setMotorSpeed(motorSpeed);
    }

}
