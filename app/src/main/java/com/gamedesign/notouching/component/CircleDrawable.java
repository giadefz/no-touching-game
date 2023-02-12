package com.gamedesign.notouching.component;

import android.graphics.Color;

import com.gamedesign.notouching.framework.Graphics;
import com.gamedesign.notouching.framework.Input;
import com.gamedesign.notouching.util.ScreenInfo;
import com.google.fpl.liquidfun.Body;
import com.google.fpl.liquidfun.CircleShape;
import com.google.fpl.liquidfun.FixtureDef;
import com.google.fpl.liquidfun.PolygonShape;
import com.google.fpl.liquidfun.Vec2;

public class CircleDrawable extends Drawable {

    public int alpha;
    public int red;
    public int green;
    public int blue;
    public float radius;
    public float density;
    public float restitution;
    public float friction;
    private int color;
    protected Vec2 vec = new Vec2();

    @Override
    public boolean isBodyWithinBounds(Input.TouchEvent event) {
        Body body = owner.getBody();
        vec.setX(event.x / ScreenInfo.SCALING_FACTOR);
        vec.setY(event.y / ScreenInfo.SCALING_FACTOR);
        Vec2 localPoint = body.getLocalPoint(vec);
        return ((localPoint.getY() >= - this.radius && localPoint.getY() <= this.radius) &&
                (localPoint.getX() >= - this.radius && localPoint.getX() <= this.radius));
    }

    @Override
    public void draw(float x, float y, float angle) {
        Graphics graphics = owner.game.getGraphics();
        Body body = owner.getBody();
        float posY = body.getPositionY() * ScreenInfo.SCALING_FACTOR;
        float posX = body.getPositionX() * ScreenInfo.SCALING_FACTOR;
        graphics.drawCircle(radius * ScreenInfo.SCALING_FACTOR, posX, posY, color);
    }

    @Override
    public void postConstructOperations() {
        Body body = owner.getBody();
        this.color = Color.argb(alpha, red, green, blue);
        CircleShape circle = new CircleShape();
        circle.setRadius(radius);
        FixtureDef fixturedef = new FixtureDef();
        fixturedef.setShape(circle);
        fixturedef.setFriction(friction);
        fixturedef.setRestitution(restitution);
        fixturedef.setDensity(density);
        body.createFixture(fixturedef);
    }

}
