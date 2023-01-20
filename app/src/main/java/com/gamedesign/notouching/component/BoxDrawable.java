package com.gamedesign.notouching.component;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;

import com.gamedesign.notouching.util.ScreenInfo;
import com.google.fpl.liquidfun.Body;
import com.google.fpl.liquidfun.BodyDef;
import com.google.fpl.liquidfun.BodyType;
import com.google.fpl.liquidfun.FixtureDef;
import com.google.fpl.liquidfun.PolygonShape;

import java.util.List;

public class BoxDrawable extends Drawable {

    public int alpha;
    public int red;
    public int green;
    public int blue;
    public float width;
    public float height;
    public float density;
    private float screen_semi_height;
    private float screen_semi_width;
    private Paint paint;

    public BoxDrawable() {
    }

    @Override
    public void draw(Canvas canvas, float x, float y, float angle) {
        canvas.save();
        canvas.rotate((float) Math.toDegrees(angle), x, y);
        canvas.drawRect(x - screen_semi_width, y - screen_semi_height,
                x + screen_semi_width, y + screen_semi_height, paint);
        canvas.restore();
    }

    @Override
    public void postConstructOperations() {
        Body body = owner.getBody();
        this.screen_semi_width = ScreenInfo.getInstance().toPixelsXLength(width) / 2;
        this.screen_semi_height = ScreenInfo.getInstance().toPixelsYLength(height) / 2;
        Paint paint = new Paint();
        int argb = Color.argb(alpha, red, green, blue);
        paint.setColor(argb);
        paint.setStyle(Paint.Style.FILL_AND_STROKE);
        PolygonShape box = new PolygonShape();
        box.setAsBox(width / 2, height / 2);
        FixtureDef fixturedef = new FixtureDef();
        fixturedef.setShape(box);
        fixturedef.setFriction(0.1f);       // default 0.2
        fixturedef.setRestitution(0.4f);    // default 0
        fixturedef.setDensity(density);     // default 0
        body.createFixture(fixturedef);
    }
}
