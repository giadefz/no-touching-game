package com.gamedesign.notouching.framework;

import android.graphics.Color;
import android.graphics.Rect;

public interface Graphics {

    enum PixmapFormat {
        ARGB8888, ARGB4444, RGB565
    }

    Pixmap newPixmap(String fileName, PixmapFormat format);

    void clear(int color);

    void drawPixel(int x, int y, int color);

    void drawLine(int x, int y, int x2, int y2, int color);
    void drawLine(float x, float y, float x2, float y2, int color);

    void drawRect(int x, int y, int width, int height, int color);
    void drawRect(int x, int y, int width, int height, int color, float angle);
    void drawRect(float x, float y, float width, float height, int color, float angle);


    void drawPixmap(Pixmap pixmap, int x, int y, int srcX, int srcY,
            int srcWidth, int srcHeight);

    void drawPixmap(Pixmap pixmap, float x, float y, int srcX, int srcY,
            int srcWidth, int srcHeight);

    void drawPixmap(Pixmap pixmap, float x, float y, float widthScaled, float heightScaled, Rect src, float angle);

    void drawPixmap(Pixmap pixmap, int x, int y);

    void drawCircle(float radius, float x, float y, int color);

    void drawText(String text, float x, float y);
    void drawText(String text, float x, float y, int color);

    int getWidth();

    int getHeight();
}
