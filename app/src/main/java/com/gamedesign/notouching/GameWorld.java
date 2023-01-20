package com.gamedesign.notouching;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;

import com.gamedesign.notouching.component.ComponentType;
import com.gamedesign.notouching.component.Drawable;
import com.gamedesign.notouching.component.GameObject;
import com.gamedesign.notouching.component.Position;
import com.gamedesign.notouching.util.Box;
import com.gamedesign.notouching.util.ScreenInfo;
import com.google.fpl.liquidfun.World;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Objects;

public class GameWorld {

    final static int bufferWidth = 1080, bufferHeight = 1920;    // actual pixels
    Bitmap buffer;
    private final Canvas canvas;
    private Paint particlePaint;
    private final List<GameObject> gameObjects;

    public World world;
    Box physicalSize;
    Box screenSize;
    public Box currentView;
    private static final int MAXPARTICLECOUNT = 1000;
    private static final float PARTICLE_RADIUS = 0.3f;

    // Parameters for world simulation
    private static final float TIME_STEP = 1 / 50f; // 50 fps
    private static final int VELOCITY_ITERATIONS = 8;
    private static final int POSITION_ITERATIONS = 3;
    private static final int PARTICLE_ITERATIONS = 3;

    final Activity activity; // just for loading bitmaps in game objects

    public GameWorld(Box physicalSize, Box screenSize, Activity activity) {
        this.activity = activity;
        this.buffer = Bitmap.createBitmap(bufferWidth, bufferHeight, Bitmap.Config.ARGB_8888);
        this.world = new World(0, 0);  // gravity vector
        this.canvas = new Canvas(buffer);
        this.gameObjects = new LinkedList<>();
        this.currentView = physicalSize;
        ScreenInfo.injectInfo(physicalSize, bufferHeight, bufferWidth);
    }

    public synchronized GameObject addGameObject(GameObject obj) {
        gameObjects.add(obj);
        return obj;
    }

    public synchronized void update(float elapsedTime)
    {
        // advance the physics simulation
        world.step(elapsedTime, VELOCITY_ITERATIONS, POSITION_ITERATIONS, PARTICLE_ITERATIONS);
    }

    public synchronized void render()
    {
        // clear the screen (with black)
        canvas.drawARGB(255, 0, 0, 0);
        drawGameObjects();
    }

    private void drawGameObjects() {
        gameObjects.stream()
                .map(gameObject -> gameObject.<Drawable>getComponent(ComponentType.Drawable))
                .filter(Objects::nonNull)
                .forEach(drawable -> drawable.drawThis(canvas));
    }

    @Override
    protected void finalize()
    {
        world.delete();
    }

}
