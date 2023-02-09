package com.gamedesign.notouching.world;

import com.google.fpl.liquidfun.Body;
import com.google.fpl.liquidfun.BodyDef;
import com.google.fpl.liquidfun.ContactListener;
import com.google.fpl.liquidfun.Joint;
import com.google.fpl.liquidfun.JointDef;
import com.google.fpl.liquidfun.World;

public class WorldHandler {

    private final World world;
    public static WorldHandler instance;
    private static final int VELOCITY_ITERATIONS = 8;
    private static final int POSITION_ITERATIONS = 3;
    private static final int PARTICLE_ITERATIONS = 3;
    private static final float step = 1/60f;

    private WorldHandler() {
        this.world = new World(0, 7);
    }

    public static Body createBody(BodyDef bodyDef){
        if(instance == null) instance = new WorldHandler();
        return instance.world.createBody(bodyDef);
    }

    public static Joint createJoint(JointDef jointDef){
        if(instance == null) instance = new WorldHandler();
        return instance.world.createJoint(jointDef);
    }

    public static void setContactListener(ContactListener contactListener){
        if(instance == null) instance = new WorldHandler();
        instance.world.setContactListener(contactListener);
    }

    public static void step(){
        if(instance == null) instance = new WorldHandler();
        instance.world.step(step, VELOCITY_ITERATIONS, POSITION_ITERATIONS, PARTICLE_ITERATIONS);
    }

    public static void delete(){
        instance.world.delete();
        instance = null;
    }

}
