package com.gamedesign.notouching.component;

import com.gamedesign.notouching.framework.Game;
import com.gamedesign.notouching.world.WorldHandler;
import com.google.fpl.liquidfun.Body;
import com.google.fpl.liquidfun.BodyDef;
import com.google.fpl.liquidfun.Vec2;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public abstract class Entity {

    private final Map<ComponentType, Component> components = new HashMap<>();
    private Body body;
    public Game game;


    public Entity() {
    }

    public Entity(Game game) {
        this.game = game;
    }

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
        Body body = WorldHandler.createBody(bodyDef);
        this.body = body;
        body.setUserData(this);
    }

    public void setPosition(float x, float y){
        Vec2 position = body.getPosition();
        position.setX(x);
        position.setY(y);
        body.setTransform(position, body.getAngle());
    }
    public void setPosition(float x, float y, float angle){
        Vec2 position = body.getPosition();
        position.setX(x);
        position.setY(y);
        body.setTransform(position, angle);
    }

    public Body getBody() {
        return body;
    }
}
