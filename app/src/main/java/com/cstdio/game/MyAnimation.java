package com.cstdio.game;


import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.util.DisplayMetrics;

public class MyAnimation {

    protected Bitmap[] frames;
    protected int currentFrame;
    protected long startTime;
    protected long delay;
    protected boolean playedOnce;

    public void setFrames(Bitmap[] frames) {
        this.frames = frames;
        currentFrame = 0;
        startTime = System.nanoTime();
    }

    public void update() {

    }

    public void setDelay(long delay) {
        this.delay = delay;
    }

    public void setFrame(int i) {
        currentFrame = i;
    }

    public Bitmap flip(Bitmap bitmap) {
        Matrix matrix = new Matrix();
        matrix.preScale(-1, 1);
        Bitmap res = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, false);
        res.setDensity(DisplayMetrics.DENSITY_DEFAULT);

        return res;
    }

    public Bitmap getImage(int direction) {
        if(direction == -1)
            return flip(frames[currentFrame]);
        return frames[currentFrame];
    }

    public int getFrame() {
        return currentFrame;
    }

    public boolean isPlayedOnce() {
        return playedOnce;
    }
}
