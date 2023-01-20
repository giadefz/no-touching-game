package com.gamedesign.notouching.component;

import android.graphics.Canvas;

import com.gamedesign.notouching.GameWorld;
import com.gamedesign.notouching.util.Box;
import com.gamedesign.notouching.util.ScreenInfo;
import com.google.fpl.liquidfun.Body;

public abstract class Drawable extends Component {

    @Override
    public ComponentType type() {
        return ComponentType.Drawable;
    }

    public void drawThis(Canvas canvas) {
        Body body = owner.getBody();
        GameWorld gw = owner.gw;
        if (body != null) {
            // Physical position of the center
            float x = body.getPositionX(), y = body.getPositionY(), angle = body.getAngle();
            // Cropping
            Box view = gw.currentView;
            if (x > view.xmin && x < view.xmax &&
                    y > view.ymin && y < view.ymax) {
                float screen_x = ScreenInfo.getInstance().toPixelsX(x), screen_y = ScreenInfo.getInstance().toPixelsY(y);
                this.draw(canvas, screen_x, screen_y, angle);
            }
        } else {
            this.draw(canvas, 0, 0, 0);
        }
    }

    public abstract void draw(Canvas canvas, float x, float y, float angle);

}
