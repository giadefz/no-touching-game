package com.gamedesign.notouching.component;

import android.graphics.Rect;
import android.graphics.RectF;

import com.gamedesign.notouching.framework.Graphics;
import com.gamedesign.notouching.framework.Pixmap;
import com.gamedesign.notouching.util.Assets;
import com.gamedesign.notouching.util.ScreenInfo;
import com.google.fpl.liquidfun.Body;
import com.google.fpl.liquidfun.FixtureDef;
import com.google.fpl.liquidfun.PolygonShape;

public class PixmapDrawable extends Drawable {
    public float width;
    public float height;
    public float density;
    public int srcRight;
    public int srcBottom;
    public String pixmapName;
    private final Rect src = new Rect();
    private Pixmap pixmap;

    public PixmapDrawable() {
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
        fixturedef.setDensity(density);     // default 0
        body.createFixture(fixturedef);
        src.set(0, 0, srcRight, srcBottom);
        pixmap = Assets.getPixmapByName(pixmapName);
    }
}
