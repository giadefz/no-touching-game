package com.gamedesign.notouching.component;

import lombok.Getter;

@Getter
public abstract class Component {
    protected Entity owner;
    public abstract ComponentType type();

}
