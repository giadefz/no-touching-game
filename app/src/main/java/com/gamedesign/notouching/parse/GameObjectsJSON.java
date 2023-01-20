package com.gamedesign.notouching.parse;

import android.content.Context;

import com.gamedesign.notouching.component.Component;
import com.gamedesign.notouching.component.ComponentType;
import com.gamedesign.notouching.component.GameObject;
import com.gamedesign.notouching.framework.Game;
import com.google.gson.Gson;

import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
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
        GameObject gameObject = new GameObject(game);
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
