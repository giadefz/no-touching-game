package com.gamedesign.notouching;

import android.app.Activity;

import com.gamedesign.notouching.framework.Screen;
import com.gamedesign.notouching.framework.impl.AndroidGame;
import com.gamedesign.notouching.screen.LoadingScreen;


public class BridgeGame extends AndroidGame {


    @Override
    public Screen getStartScreen() {
        return new LoadingScreen(this);
    }
}
