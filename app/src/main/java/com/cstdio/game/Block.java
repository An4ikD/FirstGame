package com.cstdio.game;


import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Canvas;

import java.util.Random;

public class Block extends GameObject {
    private final static int WIDTH = 520;
    private final static int HEIGHT = 600;

    private Singleton singleton = Singleton.getMyInstance();
    private Bitmap bitmap;
    private Resources resources;

    public Block(Resources resources, Bitmap bitmap, int posX, int posY) {
        width = singleton.getPlayerWidth() * 2;
        height = HEIGHT / (WIDTH / width);

        this.resources = resources;
        this.bitmap = Bitmap.createScaledBitmap(bitmap, width, height, true);

        x = posX;
        y = posY;
    }

    public int getX() {
        return x;
    }
    public int getY() {
        return y;
    }

    public void draw(Canvas canvas, int dx, int dy) {
        canvas.drawBitmap(bitmap, x - dx, y - dy, null);
    }

    public void update() {

    }
}
