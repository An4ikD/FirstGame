package com.cstdio.game;

import android.graphics.Bitmap;
import android.graphics.Canvas;

/**
 * Created by anuar on 11/9/15.
 */
public class Background {
    private Bitmap image;
    private int x, y, dx;

    public Background(Bitmap res) {
        image = res;
    }

    public void update() {
    }

    public void draw(Canvas canvas) {
        canvas.drawBitmap(image, x, y, null);
    }

    public void setVector(int dx) {
        this.dx = dx;
    }
}
