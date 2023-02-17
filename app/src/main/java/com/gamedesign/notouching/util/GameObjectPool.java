package com.gamedesign.notouching.util;

import com.gamedesign.notouching.component.GameObject;
import com.gamedesign.notouching.framework.Pool;

import java.util.List;

public class GameObjectPool {

    public static Pool<GameObject> gameObjectPool = new Pool<>(GameObject::new, 50);

    private GameObjectPool() {
    }

    public static GameObject newGameObject(){
        return gameObjectPool.newObject();
    }

    public static void freeGameObject(GameObject gameObject){
        gameObjectPool.free(gameObject);
    }

    public static void freeGameObjects(List<GameObject> gameObjects){
        gameObjects.forEach(GameObjectPool::freeGameObject);
    }
}
