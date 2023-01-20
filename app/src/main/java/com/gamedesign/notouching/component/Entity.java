package com.gamedesign.notouching.component;

import com.gamedesign.notouching.GameWorld;
import com.google.fpl.liquidfun.Body;
import com.google.fpl.liquidfun.BodyDef;
import com.google.fpl.liquidfun.PolygonShape;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

public abstract class Entity {

    private final Map<ComponentType, Component> components = new HashMap<>();
    private Body body;
    public GameWorld gw;

    public void addComponent(Component component){
        component.owner = this;
        components.put(component.type(), component);
    }

    public <T extends Component> T getComponent(ComponentType type){
        return (T) components.get(type);
    }

    public void addComponents(Set<Component> components) {
        components.forEach(this::addComponent);
    }

    public void setBody(BodyDef bodyDef) {
        Body body = gw.world.createBody(bodyDef);
        this.body = body;
        body.setUserData(this);
    }

    public Body getBody() {
        return body;
    }

    public void setShape(PolygonShape shape){

    }
}
