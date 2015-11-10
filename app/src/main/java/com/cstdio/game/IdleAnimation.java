package com.cstdio.game;

import android.graphics.Bitmap;

/**
 * Created by anuar on 11/10/15.
 */
public class IdleAnimation extends MyAnimation {
    public IdleAnimation(Bitmap[] frames, int delay) {
        this.frames = frames;
        this.delay = delay;
        currentFrame = 0;
        startTime = System.nanoTime();
    }

    @Override
    public void update() {
        long elapsed = (System.nanoTime() - startTime) / 10000000;

        if(elapsed > delay) {
            currentFrame++;
            startTime = System.nanoTime();
        }
        if(currentFrame >= frames.length) {
            currentFrame = 0;
            playedOnce = true;
        }
    }
}
