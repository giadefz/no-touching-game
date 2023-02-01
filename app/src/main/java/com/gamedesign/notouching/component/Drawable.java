package com.gamedesign.notouching.component;

import com.gamedesign.notouching.framework.Graphics;
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
            float x = body.getPositionX(), y = body.getPositionY(), angle = body.getAngle();
            this.draw(x, y, angle);

        } else {
            this.draw(0, 0, 0);
        }
    }

    public abstract void draw(float x, float y, float angle);

}
