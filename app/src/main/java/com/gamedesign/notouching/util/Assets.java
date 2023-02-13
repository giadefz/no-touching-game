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
    public static Pixmap retryButton;
    public static Pixmap stopButton;
    public static Pixmap bomb;
    public static Pixmap lvl1background;
    public static Pixmap pier;
    public static Pixmap wheel;
    public static Pixmap chassis;

    public static Pixmap getPixmapByName(String pixmapName){
        switch (pixmapName) {
            case "tile": return tile;
            case "bomb": return bomb;
            case "pier": return pier;
            case "icon": return icon;
            case "wheel": return wheel;
            case "chassis": return chassis;


            default: throw new IllegalArgumentException("No pixmap by name: " + pixmapName);
        }
    }
}
