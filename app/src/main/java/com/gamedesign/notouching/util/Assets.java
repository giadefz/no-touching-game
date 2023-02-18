package com.gamedesign.notouching.util;

import com.gamedesign.notouching.framework.Music;
import com.gamedesign.notouching.framework.Pixmap;
import com.gamedesign.notouching.framework.Sound;
import com.gamedesign.notouching.framework.impl.AndroidPixmap;
import com.gamedesign.notouching.parse.GameObjectsJSON;

import java.lang.reflect.Field;

public class Assets {

    public static GameObjectsJSON gameObjectsJSON;
    public static Pixmap tile;
    public static Pixmap tile2;
    public static Pixmap tile3;
    public static Sound click;
    public static Sound explosion;
    public static Music ost;
    public static Music engine;
    public static Sound[] tileHit = new Sound[4];
    public static Pixmap background;
    public static Pixmap buttons;
    public static Pixmap mainMenuPlayButton;
    public static Pixmap retryButton;
    public static Pixmap nextLevelButton;
    public static Pixmap stopButton;
    public static Pixmap pauseButton;
    public static Pixmap playButton;
    public static Pixmap bomb;
    public static Pixmap lvl1background;
    public static Pixmap pier;
    public static Pixmap pier2;
    public static Pixmap wheel;
    public static Pixmap terroristChassis;
    public static Pixmap chassis;
    public static Pixmap stopMusicButton;
    public static Pixmap startMusicButton;
    public static Pixmap lvl2background;
    public static Pixmap lvl3background;
    public static Pixmap tutorial1;
    public static Pixmap tutorial2;
    public static Pixmap tutorial3;
    public static Pixmap tutorial4;
    public static Pixmap quitButton;

    public static Pixmap getPixmapByName(String pixmapName) {
        try {
            Field field = Assets.class.getField(pixmapName);
            return (Pixmap) field.get(new AndroidPixmap());
        } catch (NoSuchFieldException e) {
            throw new IllegalArgumentException("No pixmap by name: " + pixmapName);
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }

    public static String getTilePixmapNameByIndex(int index){
        switch (index){
            case 0: return "tile";
            case 1: return "tile2";
            case 2: return "tile3";
            default: throw new IllegalArgumentException("No tile pixmap by index: " + index);
        }
    }

    public static Pixmap getPierPixmapByIndex(int index){
        switch (index){
            case 0: return Assets.pier;
            case 1: return Assets.pier2;
            default: throw new IllegalArgumentException("No pier pixmap by index: " + index);
        }
    }

    public static Pixmap getBackgroundByIndex(int index){
        switch (index){
            case 0: return Assets.lvl1background;
            case 1: return Assets.lvl2background;
            case 2: return Assets.lvl3background;
            default: throw new IllegalArgumentException("No background pixmap by index: " + index);
        }
    }

}
