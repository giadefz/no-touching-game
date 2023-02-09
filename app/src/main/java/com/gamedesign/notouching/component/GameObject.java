package com.gamedesign.notouching.component;

import com.gamedesign.notouching.framework.Game;

public class GameObject extends Entity {

    public final String name;

    public GameObject(String name, Game game) {
        super(game);
        this.name = name;
    }
}
