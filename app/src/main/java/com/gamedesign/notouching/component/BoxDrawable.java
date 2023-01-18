package com.gamedesign.notouching.component;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;

import com.gamedesign.notouching.util.ScreenInfo;

import java.util.List;

public class BoxDrawable extends Drawable {

    private int alpha;
    private int red;
    private int green;
    private int blue;
    private float width;
    private float height;
    private float density;
    private float screen_semi_height;
    private float screen_semi_width;

    public BoxDrawable(int alpha, int red, int green, int blue, float width, float height, float density) {
        this.alpha = alpha;
        this.red = red;
        this.green = green;
        this.blue = blue;
        this.width = width;
        this.height = height;
        this.density = density;
        this.screen_semi_width = ScreenInfo.getInstance().toPixelsXLength(width) / 2;
        this.screen_semi_height = ScreenInfo.getInstance().toPixelsYLength(height) / 2;
    }

    @Override
    public void draw(Canvas canvas) {
        canvas.save();
        Position pos = owner.getComponent(ComponentType.Position);
        canvas.rotate((float) Math.toDegrees(pos.angle), pos.x, pos.y);
        Paint paint = new Paint();
        int argb = Color.argb(alpha, red, green, blue);
        canvas.drawRect(pos.x - screen_semi_width, pos.y - screen_semi_height,
                pos.x + screen_semi_width, pos.y + screen_semi_height, paint);
    }
}
