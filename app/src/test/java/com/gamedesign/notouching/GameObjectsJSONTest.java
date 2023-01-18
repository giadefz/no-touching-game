package com.gamedesign.notouching;

import com.gamedesign.notouching.component.Component;
import com.gamedesign.notouching.component.ComponentType;
import com.gamedesign.notouching.component.GameObject;
import com.gamedesign.notouching.component.Position;
import com.gamedesign.notouching.util.GameObjectBuilder;
import com.gamedesign.notouching.parse.GameObjectsJSON;

import org.junit.Test;

import java.io.IOException;
import java.util.List;

/**
 *
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
    public void getGameObject() {
        GameObject terrorist = GameObjectBuilder.getGameObject("PESCIO");
        Position component = terrorist.<Position>getComponent(ComponentType.Position);
    }
}