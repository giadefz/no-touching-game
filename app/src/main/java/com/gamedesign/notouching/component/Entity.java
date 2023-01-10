package com.gamedesign.notouching.component;

import java.util.Map;

public abstract class Entity {

    private Map<ComponentType, Component> components;

    public void addComponent(Component component){
        component.owner = this;
        components.put(component.type(), component);
    }

    public Component getComponent(ComponentType type){
        return components.get(type);
    }

}
