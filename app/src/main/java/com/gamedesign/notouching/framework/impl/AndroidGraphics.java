package com.gamedesign.notouching.framework.impl;

import java.io.IOException;
import java.io.InputStream;

import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.BitmapFactory;
import android.graphics.BitmapFactory.Options;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.graphics.Rect;
import android.graphics.RectF;

import com.gamedesign.notouching.framework.Graphics;
import com.gamedesign.notouching.framework.Pixmap;

public class AndroidGraphics implements Graphics {
    AssetManager assets;
    Bitmap frameBuffer;
    public Canvas canvas;
    Paint paint;
    Rect srcRect = new Rect();
    RectF srcRectF = new RectF();
    Rect dstRect = new Rect();
    RectF dstRectF = new RectF();

    public AndroidGraphics(AssetManager assets, Bitmap frameBuffer) {
        this.assets = assets;
        this.frameBuffer = frameBuffer;
        this.canvas = new Canvas(frameBuffer);
        this.paint = new Paint();
    }

    @Override
    public Pixmap newPixmap(String fileName, PixmapFormat format) {
        Config config = null;
        if (format == PixmapFormat.RGB565)
            config = Config.RGB_565;
        else if (format == PixmapFormat.ARGB4444)
            config = Config.ARGB_4444;
        else
            config = Config.ARGB_8888;

        Options options = new Options();
        options.inPreferredConfig = config;

        InputStream in = null;
        Bitmap bitmap = null;
        try {
            in = assets.open(fileName);
            bitmap = BitmapFactory.decodeStream(in);
            if (bitmap == null)
                throw new RuntimeException("Couldn't load bitmap from asset '"
                        + fileName + "'");
        } catch (IOException e) {
            throw new RuntimeException("Couldn't load bitmap from asset '"
                    + fileName + "'");
        } finally {
            if (in != null) {
                try {
                    in.close();
                } catch (IOException e) {
                }
            }
        }

        if (bitmap.getConfig() == Config.RGB_565)
            format = PixmapFormat.RGB565;
        else if (bitmap.getConfig() == Config.ARGB_4444)
            format = PixmapFormat.ARGB4444;
        else
            format = PixmapFormat.ARGB8888;

        return new AndroidPixmap(bitmap, format);
    }

    @Override
    public void clear(int color) {
        canvas.drawRGB((color & 0xff0000) >> 16, (color & 0xff00) >> 8,
                (color & 0xff));
    }

    @Override
    public void drawPixel(int x, int y, int color) {
        paint.setColor(color);
        canvas.drawPoint(x, y, paint);
    }

    @Override
    public void drawLine(int x, int y, int x2, int y2, int color) {
        paint.setColor(color);
        paint.setStrokeWidth(8f);
        canvas.drawLine(x, y, x2, y2, paint);
    }

    @Override
    public void drawLine(float x, float y, float x2, float y2, int color) {
        paint.setColor(color);
        paint.setStrokeWidth(8f);
        canvas.drawLine(x, y, x2, y2, paint);
    }

    @Override
    public void drawRect(int x, int y, int width, int height, int color) {
        paint.setColor(color);
        paint.setStyle(Style.FILL);
        canvas.drawRect(x, y, x + width - 1, y + width - 1, paint);
    }

    @Override
    public void drawRect(int x, int y, int width, int height, int color, float angle) {
        canvas.save();
        canvas.rotate((float) Math.toDegrees(angle), x, y);
        drawRect(x, y, width, height, color);
        canvas.restore();
    }

    @Override
    public void drawRect(float x, float y, float width, float height, int color, float angle) {
        canvas.save();
        canvas.rotate((float) Math.toDegrees(angle), x + (width/2), y + (height/2));
        paint.setColor(color);
        paint.setStyle(Style.FILL);
        canvas.drawRect(x, y, x + width, y + height, paint);
        canvas.restore();
    }

    @Override
    public void drawPixmap(Pixmap pixmap, int x, int y, int srcX, int srcY,
            int srcWidth, int srcHeight) {
        srcRect.left = srcX;
        srcRect.top = srcY;
        srcRect.right = srcX + srcWidth - 1;
        srcRect.bottom = srcY + srcHeight - 1;

        dstRect.left = x;
        dstRect.top = y;
        dstRect.right = x + srcWidth - 1;
        dstRect.bottom = y + srcHeight - 1;

        canvas.drawBitmap(((AndroidPixmap) pixmap).bitmap, srcRect, dstRect,
                null);
    }

    @Override
    public void drawPixmap(Pixmap pixmap, float x, float y, int srcX, int srcY, int srcWidth, int srcHeight) {
        srcRect.left = srcX;
        srcRect.top = srcY;
        srcRect.right = srcX + srcWidth - 1;
        srcRect.bottom = srcY + srcHeight - 1;

        dstRectF.left = x;
        dstRectF.top = y;
        dstRectF.right = x + srcWidth - 1;
        dstRectF.bottom = y + srcHeight - 1;

        canvas.drawBitmap(((AndroidPixmap) pixmap).bitmap, srcRect, dstRectF,
                null);
    }

    @Override
    public void drawPixmap(Pixmap pixmap, float x, float y, float widthScaled, float heightScaled, Rect src, float angle) {
        canvas.save();
        canvas.rotate((float) Math.toDegrees(angle), x, y);
        dstRectF.left = x - (widthScaled / 2);
        dstRectF.bottom = y + (heightScaled / 2);
        dstRectF.right = x + (widthScaled / 2);
        dstRectF.top = y - (heightScaled / 2);
        canvas.drawBitmap(((AndroidPixmap) pixmap).bitmap, src, dstRectF, null);
        canvas.restore();
    }

    @Override
    public void drawPixmap(Pixmap pixmap, int x, int y) {
        canvas.drawBitmap(((AndroidPixmap)pixmap).bitmap, x, y, null);
    }

    @Override
    public void drawCircle(float radius, float x, float y, int color) {
        paint.setColor(color);
        canvas.drawCircle(x, y, radius, paint);
    }

    @Override
    public void drawText(String text, float x, float y) {
        paint.setColor(Color.BLACK);
        paint.setStyle(Style.FILL);
        paint.setTextSize(48f);
        canvas.drawText(text, x, y, paint);
    }

    @Override
    public void drawText(String text, float x, float y, int color) {
        paint.setColor(color);
        paint.setStyle(Style.FILL);
        paint.setTextSize(48f);
        canvas.drawText(text, x, y, paint);
    }

    @Override
    public int getWidth() {
        return frameBuffer.getWidth();
    }

    @Override
    public int getHeight() {
        return frameBuffer.getHeight();
    }
}
