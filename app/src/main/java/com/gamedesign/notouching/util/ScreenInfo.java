package com.gamedesign.notouching.util;

public class ScreenInfo {

    private static final ScreenInfo instance = new ScreenInfo();
    private Box currentView;
    private int bufferWidth, bufferHeight;

    private ScreenInfo() {

    }

    public static void injectInfo(Box currentView, int bufferHeight, int bufferWidth) {
        instance.currentView = currentView;
        instance.bufferHeight = bufferHeight;
        instance.bufferWidth = bufferWidth;
    }

    public static ScreenInfo getInstance() {
        assert instance.currentView != null;
        return instance;
    }

    public float toPixelsXLength(float x) {
        return x / currentView.width * bufferWidth;
    }

    public float toPixelsYLength(float y) {
        return y / currentView.height * bufferHeight;
    }

    public float toPixelsX(float x) { return (x-currentView.xmin)/currentView.width*bufferWidth; }
    public float toPixelsY(float y) { return (y-currentView.ymin)/currentView.height*bufferHeight; }

}
