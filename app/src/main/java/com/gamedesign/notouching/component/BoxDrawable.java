package com.gamedesign.notouching.component;

import android.graphics.Color;

import com.gamedesign.notouching.framework.Graphics;
import com.gamedesign.notouching.framework.Input;
import com.gamedesign.notouching.util.ScreenInfo;
import com.google.fpl.liquidfun.Body;
import com.google.fpl.liquidfun.FixtureDef;
import com.google.fpl.liquidfun.PolygonShape;
import com.google.fpl.liquidfun.Vec2;

public class BoxDrawable extends Drawable {

    public int alpha;
    public int red;
    public int green;
    public int blue;
    public float width;
    public float height;
    public float density;
    private int color;
    protected Vec2 vec = new Vec2();

    public BoxDrawable() {
    }

    @Override
    public boolean isBodyWithinBounds(Input.TouchEvent event) {
        Body body = owner.getBody();
        vec.setX(event.x / ScreenInfo.SCALING_FACTOR);
        vec.setY(event.y / ScreenInfo.SCALING_FACTOR);
        Vec2 localPoint = body.getLocalPoint(vec);
        return ((localPoint.getY() >= - this.height / 2 && localPoint.getY() <= this.height / 2) &&
                (localPoint.getX() >= - this.width / 2 && localPoint.getX() <= this.width /2));
    }

    @Override
    public void draw(float x, float y, float angle) {
        Graphics graphics = owner.game.getGraphics();
        float widthScaled = width * ScreenInfo.SCALING_FACTOR;
        float heightScaled = height * ScreenInfo.SCALING_FACTOR;
        float xScaled = x * ScreenInfo.SCALING_FACTOR;
        float yScaled = y * ScreenInfo.SCALING_FACTOR;
        graphics.drawRect(xScaled - (widthScaled / 2), yScaled - (heightScaled / 2), widthScaled, heightScaled, color, angle);
    }

    @Override
    public void postConstructOperations() {
        Body body = owner.getBody();
        this.color = Color.argb(alpha, red, green, blue);
        PolygonShape box = new PolygonShape();
        box.setAsBox(width / 2, height / 2);
        FixtureDef fixturedef = new FixtureDef();
        fixturedef.setShape(box);
        fixturedef.setFriction(1f);       // default 0.2
        fixturedef.setRestitution(0f);    // default 0
        fixturedef.setDensity(density);     // default 0
        body.createFixture(fixturedef);
    }
}