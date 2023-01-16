package com.gamedesign.notouching.component;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

public abstract class Entity {

    private final Map<ComponentType, Component> components = new HashMap<>();

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

}
