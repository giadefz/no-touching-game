package com.gamedesign.notouching.component;

import android.graphics.Rect;
import android.graphics.RectF;

import com.gamedesign.notouching.framework.Graphics;
import com.gamedesign.notouching.framework.Input;
import com.gamedesign.notouching.framework.Pixmap;
import com.gamedesign.notouching.util.Assets;
import com.gamedesign.notouching.util.ScreenInfo;
import com.google.fpl.liquidfun.Body;
import com.google.fpl.liquidfun.FixtureDef;
import com.google.fpl.liquidfun.PolygonShape;
import com.google.fpl.liquidfun.Vec2;

public class PixmapDrawable extends Drawable {
    public float width;
    public float height;
    public float density;
    public int srcRight;
    public int srcBottom;
    public String pixmapName;
    private final Rect src = new Rect();
    private Pixmap pixmap;
    protected Vec2 upperLeftCorner;


    public PixmapDrawable() {
    }

    @Override
    public boolean isBodyWithinBounds(Input.TouchEvent event) {
        Body body = owner.getBody();
        Vec2 worldPoint = body.getWorldPoint(upperLeftCorner);
        return inBounds(event,
                worldPoint.getX() * ScreenInfo.SCALING_FACTOR,
                worldPoint.getY() * ScreenInfo.SCALING_FACTOR,
                ((width * (float) Math.cos(body.getAngle())) - height * (float) Math.sin(body.getAngle())) * ScreenInfo.SCALING_FACTOR,
                ((width * (float) Math.sin(body.getAngle())) + height * (float) Math.cos(body.getAngle())) * ScreenInfo.SCALING_FACTOR);
    }

    @Override
    public void draw(float x, float y, float angle) {
        Graphics graphics = owner.game.getGraphics();
        float widthScaled = width * ScreenInfo.SCALING_FACTOR;
        float heightScaled = height * ScreenInfo.SCALING_FACTOR;
        graphics.drawPixmap(pixmap, x * ScreenInfo.SCALING_FACTOR, y * ScreenInfo.SCALING_FACTOR, widthScaled, heightScaled, src, angle);
    }

    @Override
    public void postConstructOperations() {
        Body body = owner.getBody();
        this.upperLeftCorner = new Vec2(- width / 2, - height / 2);
        PolygonShape box = new PolygonShape();
        box.setAsBox(width / 2, height / 2);
        FixtureDef fixturedef = new FixtureDef();
        fixturedef.setShape(box);
        fixturedef.setDensity(density);     // default 0
        body.createFixture(fixturedef);
        src.set(0, 0, srcRight, srcBottom);
        pixmap = Assets.getPixmapByName(pixmapName);
    }

}
