package com.gamedesign.notouching.parse;

import com.gamedesign.notouching.component.Component;
import com.gamedesign.notouching.component.GameObject;
import com.gamedesign.notouching.framework.Game;
import com.gamedesign.notouching.util.GameObjectPool;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class GameObjectsJSON {

    public List<GameObjectJSON> gameObjects;


    public GameObjectsJSON() {
    }

    public static class GameObjectJSON {
        public String gameObjectName;
        public List<ComponentJSON> components;
    }

    public static class ComponentJSON {
        public String component;
        public List<Argument> args;
    }

    public static class Argument {
        public String name;
        public String value;
    }

    public GameObject getGameObject(String gameObjectName, Game game) {
        GameObject gameObject = GameObjectPool.newGameObject();
        gameObject.name = gameObjectName;
        gameObject.game = game;
        Set<Component> components = this.gameObjects.stream()
                .filter(g -> g.gameObjectName.equals(gameObjectName))
                .findFirst()
                .map(g -> g.components)
                .orElseThrow(() -> new IllegalArgumentException("No game object found for name: " + gameObjectName))
                .stream()
                .map(c -> Component.fromComponentJSON(c, gameObject))
                .collect(Collectors.toSet());
        gameObject.addComponents(components);
        return gameObject;
    }

}
