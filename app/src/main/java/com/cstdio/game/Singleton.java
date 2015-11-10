package com.cstdio.game;


public class Singleton {
    private static Singleton myInstance = new Singleton();
    private int screenHeight, screenWidth;

    private Singleton() {}
    public static synchronized Singleton getMyInstance() {
        return myInstance;
    }

    public void setScreenSize(int screenHeight, int screenWidth) {
        this.screenHeight = screenHeight;
        this.screenWidth = screenWidth;
    }

    public int getScreenHeight() {
        return screenHeight;
    }

    public int getScreenWidth() {
        return screenWidth;
    }
}
