package com.cstdio.game;

import android.graphics.Bitmap;


public class JumpAnimation extends MyAnimation {


    public JumpAnimation(Bitmap[] frames, int delay) {
        this.frames = frames;
        this.delay = delay;
        currentFrame = 0;
        startTime = System.nanoTime();
    }

    @Override
    public void update() {

    }

}
