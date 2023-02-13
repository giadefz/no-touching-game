package com.gamedesign.notouching.component;

import android.graphics.Color;
import android.graphics.Rect;

import com.gamedesign.notouching.framework.Graphics;
import com.gamedesign.notouching.framework.Input;
import com.gamedesign.notouching.framework.Pixmap;
import com.gamedesign.notouching.util.Assets;
import com.gamedesign.notouching.util.ScreenInfo;
import com.google.fpl.liquidfun.Body;
import com.google.fpl.liquidfun.CircleShape;
import com.google.fpl.liquidfun.FixtureDef;
import com.google.fpl.liquidfun.PolygonShape;
import com.google.fpl.liquidfun.Vec2;

public class CirclePixmapDrawable extends Drawable {
    public float radius;
    public float density;
    public float restitution;
    public float friction;

    public int srcRight;
    public int srcBottom;
    public String pixmapName;
    private final Rect src = new Rect();
    private Pixmap pixmap;
    protected Vec2 vec = new Vec2();


    public CirclePixmapDrawable() {
    }

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
        float widthScaled = (radius * 2) * ScreenInfo.SCALING_FACTOR;
        float heightScaled = (radius * 2) * ScreenInfo.SCALING_FACTOR;
        graphics.drawPixmap(pixmap, x * ScreenInfo.SCALING_FACTOR, y * ScreenInfo.SCALING_FACTOR, widthScaled, heightScaled, src, angle);
    }

    @Override
    public void postConstructOperations() {
        Body body = owner.getBody();
        CircleShape circle = new CircleShape();
        circle.setRadius(radius);
        FixtureDef fixturedef = new FixtureDef();
        fixturedef.setShape(circle);
        fixturedef.setFriction(friction);
        fixturedef.setRestitution(restitution);
        fixturedef.setDensity(density);
        body.createFixture(fixturedef);
        src.set(0, 0, srcRight, srcBottom);
        pixmap = Assets.getPixmapByName(pixmapName);
    }

}
