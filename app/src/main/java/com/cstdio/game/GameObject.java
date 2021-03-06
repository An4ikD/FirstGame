package com.cstdio.game;


import android.graphics.Rect;

public abstract class GameObject {
    protected int x, y, dx, dy; // position and change vectors
    protected int width, height; // size of bitmap

    public void setX(int x) {
        this.x = x;
    }
    public void setY(int y) {
        this.y = y;
    }
    public int getX() {
        return x;
    }
    public int getY() {
        return y;
    }
    public int getHeight() {
        return height;
    }
    public int getWidth() {
        return width;
    }
    public Rect getRectangle() {
        return new Rect(x, y, x + width, y + height);
    }
}
