package com.gamedesign.notouching.component;

import com.gamedesign.notouching.framework.Game;

public class GameObject extends Entity {

    public String name;

    public GameObject() {
    }

    public GameObject(String name, Game game) {
        super(game);
        this.name = name;
    }
}
