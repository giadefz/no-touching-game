package com.gamedesign.notouching.parse;

import android.content.Context;

import com.gamedesign.notouching.component.ComponentType;
import com.google.gson.Gson;

import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
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
        public String component;
        public List<Argument> args;
    }

    public static class Argument {
        public String name;
        public String value;
    }


    public static GameObjectsJSON readGameObjectsJSON(Context context)  {
        if(instance == null){
            Gson gson = new Gson();
            try (Reader reader = new InputStreamReader(context.getAssets().open("gameobjects.json"))) {
                return gson.fromJson(reader, GameObjectsJSON.class);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return instance;
    }


}
