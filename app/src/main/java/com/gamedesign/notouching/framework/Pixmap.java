package com.gamedesign.notouching.framework;

import com.gamedesign.notouching.framework.Graphics.PixmapFormat;

public interface Pixmap {
    int getWidth();

    int getHeight();

    PixmapFormat getFormat();

    void dispose();
}
