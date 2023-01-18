package com.gamedesign.notouching.component;

import java.lang.reflect.Field;
import java.util.List;

import lombok.NoArgsConstructor;

@NoArgsConstructor
public class Position extends Component {

    protected int x;
    protected int y;
    protected int angle;

    @Override
    public ComponentType type() {
        return ComponentType.Position;
    }

}
