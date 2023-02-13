package com.gamedesign.notouching.util;

import com.gamedesign.notouching.component.GameObject;
import com.gamedesign.notouching.component.PixmapDrawable;
import com.gamedesign.notouching.framework.Game;
import com.gamedesign.notouching.parse.GameObjectsJSON;

public class TileBuilder {

    public static GameObject buildTile(float width, Game game) {
        GameObject gameObject = Assets.gameObjectsJSON.getGameObject(GameObjects.TILE, game);
        PixmapDrawable drawable = new PixmapDrawable();
        drawable.owner = gameObject;
        drawable.width = width;
        drawable.height = 2;
        drawable.density = 7;
        drawable.srcRight = 288;
        drawable.srcBottom = 59;
        drawable.pixmapName = "tile";
        drawable.postConstructOperations();
        gameObject.addComponent(drawable);
        return gameObject;
    }

}
