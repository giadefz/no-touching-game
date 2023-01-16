package com.gamedesign.notouching;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import androidx.annotation.Nullable;

import com.gamedesign.notouching.util.Box;

public class MainActivity extends Activity {

    public static String TAG;
    private AndroidFastRenderView renderView;

    private static final float XMIN = -4, XMAX = 16, YMIN = -22, YMAX = 22;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        System.loadLibrary("liquidfun");
        System.loadLibrary("liquidfun_jni");

        TAG = getString(R.string.app_name);

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

        DisplayMetrics metrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(metrics);

        Box physicalSize = new Box(XMIN, YMIN, XMAX, YMAX),
                screenSize   = new Box(0, 0, metrics.widthPixels, metrics.heightPixels);

        GameWorld gw = new GameWorld(physicalSize, screenSize, this);

        renderView = new AndroidFastRenderView(this, gw);
        setContentView(renderView);


    }

    @Override
    public void onPause() {
        super.onPause();
        Log.i("Main thread", "pause");
        renderView.pause(); // stops the main loop
    }

    @Override
    public void onStop() {
        super.onStop();
        Log.i("Main thread", "stop");
    }

    @Override
    public void onResume() {
        super.onResume();
        Log.i("Main thread", "resume");

        renderView.resume(); // starts game loop in a separate thread
    }
}
