package com.gamedesign.notouching;

import com.gamedesign.notouching.component.Component;
import com.gamedesign.notouching.component.ComponentType;
import com.gamedesign.notouching.component.GameObject;
import com.gamedesign.notouching.util.GameObjectBuilders;
import com.gamedesign.notouching.util.GameObjectsJSON;

import org.junit.Test;

import java.io.IOException;
import java.util.List;
import java.util.Set;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class GameObjectsJSONTest {

    @Test
    public void getGameObjectsJSON() throws IOException {
        GameObjectsJSON gameObjectsJSON = GameObjectsJSON.readGameObjectsJSON();
        List<GameObjectsJSON.GameObjectJSON> gameObjects = gameObjectsJSON.gameObjects;
    }

    @Test
    public void getComponentsForGameObject() {
        GameObject terrorist = GameObjectBuilders.getGameObject("TERRORIST");
        Component component = terrorist.getComponent(ComponentType.Position);
    }
}