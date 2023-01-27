package com.gamedesign.notouching.component;

import android.graphics.Rect;

import com.gamedesign.notouching.framework.Graphics;
import com.gamedesign.notouching.framework.Pixmap;
import com.gamedesign.notouching.util.Assets;
import com.google.fpl.liquidfun.Body;
import com.google.fpl.liquidfun.FixtureDef;
import com.google.fpl.liquidfun.PolygonShape;

public class PixmapDrawable extends Drawable {
    public float width;
    public float height;
    public float density;
    private final Rect src = new Rect();
    private Pixmap pixmap;

    public PixmapDrawable() {
    }

    @Override
    public void draw(float x, float y, float angle) {
        Graphics graphics = owner.game.getGraphics();

        graphics.drawPixmap(pixmap, x, y, src.left, src.top, src.right, src.bottom);
    }

    @Override
    public void postConstructOperations() {
        Body body = owner.getBody();

        PolygonShape box = new PolygonShape();
        box.setAsBox(width / 2, height / 2);
        FixtureDef fixturedef = new FixtureDef();
        fixturedef.setShape(box);
        fixturedef.setFriction(0.1f);       // default 0.2
        fixturedef.setRestitution(0.4f);    // default 0
        fixturedef.setDensity(density);     // default 0
        body.createFixture(fixturedef);
        src.set(0, 0, 76, 76);
        pixmap = Assets.icon;
    }
}
