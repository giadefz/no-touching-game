package com.gamedesign.notouching.component;

import android.graphics.Canvas;

import com.gamedesign.notouching.GameWorld;
import com.gamedesign.notouching.framework.Graphics;
import com.gamedesign.notouching.util.Box;
import com.gamedesign.notouching.util.ScreenInfo;
import com.google.fpl.liquidfun.Body;

public abstract class Drawable extends Component {

    @Override
    public ComponentType type() {
        return ComponentType.Drawable;
    }

    public void drawThis() {
        Body body = owner.getBody();
        Graphics graphics = owner.game.getGraphics();
        if (body != null) {
            // Physical position of the center
            float x = body.getPositionX(), y = body.getPositionY(), angle = body.getAngle();
            // Cropping
            float screen_x = ScreenInfo.getInstance().toPixelsX(x), screen_y = ScreenInfo.getInstance().toPixelsY(y);
            this.draw(screen_x, screen_y, angle);

        } else {
            this.draw(0, 0, 0);
        }
    }

    public abstract void draw(float x, float y, float angle);

}
