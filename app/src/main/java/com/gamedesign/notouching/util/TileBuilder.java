package com.gamedesign.notouching.util;

import com.gamedesign.notouching.component.GameObject;
import com.gamedesign.notouching.component.PixmapDrawable;
import com.gamedesign.notouching.framework.Game;
import com.gamedesign.notouching.framework.Pixmap;
import com.gamedesign.notouching.parse.GameObjectsJSON;

import java.util.Random;

public class TileBuilder {

    public static GameObject buildTile(float width, Game game, String pixmapName) {
        GameObject gameObject = Assets.gameObjectsJSON.getGameObject(GameObjects.TILE, game);
        PixmapDrawable drawable = new PixmapDrawable();
        drawable.owner = gameObject;
        drawable.width = width;
        drawable.height = 2;
        drawable.density = 7;
        drawable.srcRight = 288;
        drawable.srcBottom = 59;
        drawable.pixmapName = pixmapName;
        drawable.postConstructOperations();
        gameObject.addComponent(drawable);
        return gameObject;
    }

}
