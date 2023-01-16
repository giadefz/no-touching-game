package com.gamedesign.notouching.util;

import com.gamedesign.notouching.component.Component;
import com.gamedesign.notouching.component.ComponentType;
import com.google.gson.Gson;

import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.List;

public class GameObjectsJSON {

    private static GameObjectsJSON instance;
    public List<GameObjectJSON> gameObjects;

    private GameObjectsJSON() {

    }

    public static class GameObjectJSON {
        public String gameObjectName;
        public List<ComponentJSON> components;
    }

    public static class ComponentJSON {
        public ComponentType component;
        public List<String> args;
    }


    public static GameObjectsJSON readGameObjectsJSON()  {
        if(instance == null){
            Gson gson = new Gson();
            try (Reader reader = new FileReader("src/main/assets/gameobjects.json")) {
                return gson.fromJson(reader, GameObjectsJSON.class);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return instance;
    }


}
