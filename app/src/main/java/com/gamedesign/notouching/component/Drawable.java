package com.gamedesign.notouching.component;

import com.gamedesign.notouching.framework.Graphics;
import com.gamedesign.notouching.framework.Input;
import com.gamedesign.notouching.util.ScreenInfo;
import com.google.fpl.liquidfun.Body;
import com.google.fpl.liquidfun.Vec2;

public abstract class Drawable extends Component {

    @Override
    public ComponentType type() {
        return ComponentType.Drawable;
    }

    public void drawThis() {
        Body body = owner.getBody();
        if (body != null) {
            float x = body.getPositionX(), y = body.getPositionY(), angle = body.getAngle();
            this.draw(x, y, angle);

        } else {
            this.draw(0, 0, 0);
        }
    }

    public abstract boolean isBodyWithinBounds(Input.TouchEvent event);
    public abstract void draw(float x, float y, float angle);

    protected boolean inBounds(Input.TouchEvent event, float x, float y, float width, float height) {
        if(event.x > x && event.x < x + width &&
                event.y > y && event.y < y + height)
            return true;
        else
            return false;
    }

    public Vec2 getLocalCoordinatesFromWorldCoordinates(float x, float y){
        Body body = owner.getBody();
        return body.getLocalPoint(new Vec2(x / ScreenInfo.SCALING_FACTOR, y / ScreenInfo.SCALING_FACTOR));
    }
}
