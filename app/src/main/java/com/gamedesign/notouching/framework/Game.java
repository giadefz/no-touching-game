package com.gamedesign.notouching.framework;

import com.google.fpl.liquidfun.World;

public interface Game {
    Input getInput();

    FileIO getFileIO();

    Graphics getGraphics();

    Audio getAudio();

    void setScreen(Screen screen);

    Screen getCurrentScreen();

    Screen getStartScreen();

    World getWorld();
}