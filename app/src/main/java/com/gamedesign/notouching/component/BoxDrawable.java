package com.gamedesign.notouching.component;

import android.graphics.Color;

import com.gamedesign.notouching.framework.Graphics;
import com.gamedesign.notouching.util.ScreenInfo;
import com.google.fpl.liquidfun.Body;
import com.google.fpl.liquidfun.FixtureDef;
import com.google.fpl.liquidfun.PolygonShape;

public class BoxDrawable extends Drawable {

    public int alpha;
    public int red;
    public int green;
    public int blue;
    public float width;
    public float height;
    public float density;
    private int color;

    public BoxDrawable() {
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
        fixturedef.setFriction(0.8f);       // default 0.2
        fixturedef.setRestitution(0f);    // default 0
        fixturedef.setDensity(density);     // default 0
        body.createFixture(fixturedef);
    }
}