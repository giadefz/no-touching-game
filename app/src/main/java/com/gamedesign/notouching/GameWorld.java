package com.gamedesign.notouching;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;

import com.badlogic.androidgames.framework.Input;
import com.google.fpl.liquidfun.World;

public class GameWorld {

    final static int bufferWidth = 1080, bufferHeight = 1920;    // actual pixels
    Bitmap buffer;
    private Canvas canvas;
    private Paint particlePaint;

    World world;
    private static final int MAXPARTICLECOUNT = 1000;
    private static final float PARTICLE_RADIUS = 0.3f;

    // Parameters for world simulation
    private static final float TIME_STEP = 1 / 50f; // 50 fps
    private static final int VELOCITY_ITERATIONS = 8;
    private static final int POSITION_ITERATIONS = 3;
    private static final int PARTICLE_ITERATIONS = 3;

    final Activity activity; // just for loading bitmaps in game objects

    public GameWorld(Activity activity) {
        this.activity = activity;
        this.buffer = Bitmap.createBitmap(bufferWidth, bufferHeight, Bitmap.Config.ARGB_8888);
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

    }

}
