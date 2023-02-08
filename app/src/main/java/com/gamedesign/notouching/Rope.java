package com.gamedesign.notouching;

import com.google.fpl.liquidfun.Joint;
import com.google.fpl.liquidfun.Vec2;

public class Rope {

    public Joint joint;
    public float localCoordinatesX;
    public float localCoordinatesY;

    public Rope(Joint joint, float localCoordinatesX, float localCoordinatesY) {
        this.joint = joint;
        this.localCoordinatesX = localCoordinatesX;
        this.localCoordinatesY = localCoordinatesY;
    }
}
