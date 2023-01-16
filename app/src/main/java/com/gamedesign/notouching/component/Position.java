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

    @Override
    public void fillFieldsFromArgs(List<String> args) {
        Field[] fields = this.getClass().getDeclaredFields();
        for(int i = 0; i < fields.length; i++){
            Field field = fields[i];
            try {
                field.setInt(this, Integer.parseInt(args.get(i)));
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        }
    }

}
