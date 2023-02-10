package com.gamedesign.notouching.util;

import com.gamedesign.notouching.framework.Pixmap;
import com.gamedesign.notouching.framework.Sound;
import com.gamedesign.notouching.parse.GameObjectsJSON;

public class Assets {

    public static GameObjectsJSON gameObjectsJSON;
    public static Pixmap icon;
    public static Pixmap tile;
    public static Sound click;
    public static Sound explosion;
    public static Pixmap background;
    public static Pixmap buttons;
    public static Pixmap mainMenu;

    public static Pixmap getPixmapByName(String pixmapName){
        switch (pixmapName) {
            case "tile": return tile;
            case "icon": return icon;
            default: throw new IllegalArgumentException("No pixmap by name: " + pixmapName);
        }
    }
}
