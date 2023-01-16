package com.gamedesign.notouching.component;

import android.graphics.Canvas;

public abstract class Drawable extends Component {

    @Override
    public ComponentType type() {
        return ComponentType.Drawable;
    }

    public abstract void draw(Canvas canvas);

}
