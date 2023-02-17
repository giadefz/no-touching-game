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
    public float friction;
    public int srcRight;
    public int srcBottom;
    public String pixmapName;
    private final Rect src = new Rect();
    public Pixmap pixmap;
    protected Vec2 vec = new Vec2();


    public PixmapDrawable() {
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
        graphics.drawPixmap(pixmap, x * ScreenInfo.SCALING_FACTOR, y * ScreenInfo.SCALING_FACTOR, widthScaled, heightScaled, src, angle);
    }

    @Override
    public void postConstructOperations() {
        Body body = owner.getBody();
        PolygonShape box = new PolygonShape();
        box.setAsBox(width / 2, height / 2);
        FixtureDef fixturedef = new FixtureDef();
        fixturedef.setShape(box);
        fixturedef.setDensity(density);
        body.createFixture(fixturedef);
        src.set(0, 0, srcRight, srcBottom);
        pixmap = Assets.getPixmapByName(pixmapName);
    }

}
