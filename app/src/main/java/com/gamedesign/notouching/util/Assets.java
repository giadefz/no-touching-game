package com.gamedesign.notouching.util;

import com.gamedesign.notouching.framework.Music;
import com.gamedesign.notouching.framework.Pixmap;
import com.gamedesign.notouching.framework.Sound;
import com.gamedesign.notouching.framework.impl.AndroidPixmap;
import com.gamedesign.notouching.parse.GameObjectsJSON;

import java.lang.reflect.Field;
import java.util.Random;

import lombok.SneakyThrows;

public class Assets {

    public static GameObjectsJSON gameObjectsJSON;
    public static Pixmap icon;
    public static Pixmap tile;
    public static Sound click;
    public static Sound explosion;
    public static Music engine;
    public static Sound[] tileHit = new Sound[4];
    public static Pixmap background;
    public static Pixmap buttons;
    public static Pixmap mainMenu;
    public static Pixmap retryButton;
    public static Pixmap stopButton;
    public static Pixmap pauseButton;
    public static Pixmap playButton;
    public static Pixmap bomb;
    public static Pixmap lvl1background;
    public static Pixmap pier;
    public static Pixmap pier2;
    public static Pixmap wheel;
    public static Pixmap chassis;
    public static Pixmap nextLevel;

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
}
