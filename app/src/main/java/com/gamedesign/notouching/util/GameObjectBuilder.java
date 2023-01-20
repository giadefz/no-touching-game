package com.gamedesign.notouching.util;

import android.content.Context;

import com.gamedesign.notouching.component.Component;
import com.gamedesign.notouching.component.GameObject;
import com.gamedesign.notouching.parse.GameObjectsJSON;

import java.util.Set;
import java.util.stream.Collectors;

public class GameObjectBuilder {


    public static GameObject getGameObject(String gameObjectName, Context context) {
        GameObjectsJSON gameObjectsJSON = GameObjectsJSON.readGameObjectsJSON(context);
        GameObject gameObject = new GameObject();
        Set<Component> components = gameObjectsJSON.gameObjects.stream()
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
