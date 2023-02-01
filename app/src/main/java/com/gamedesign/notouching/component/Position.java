package com.gamedesign.notouching.component;

import com.google.fpl.liquidfun.Body;
import com.google.fpl.liquidfun.BodyDef;
import com.google.fpl.liquidfun.BodyType;

import java.lang.reflect.Field;
import java.util.List;

import lombok.NoArgsConstructor;

@NoArgsConstructor
public class Position extends Component {

    protected float x;
    protected float y;
    protected int angle;
    protected boolean dynamic;

    @Override
    public ComponentType type() {
        return ComponentType.Position;
    }

    @Override
    public void postConstructOperations() {
        BodyDef bdef = new BodyDef();
        bdef.setPosition(x, y);
        if(dynamic){
            bdef.setType(BodyType.dynamicBody);
        }else{
            bdef.setType(BodyType.staticBody);
        }
        owner.setBody(bdef);
    }
}
