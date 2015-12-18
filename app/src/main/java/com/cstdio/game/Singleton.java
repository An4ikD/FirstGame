package com.cstdio.game;


public class Singleton {
    private static Singleton myInstance = new Singleton();
    private int screenHeight, screenWidth;
    private int playerWidth, playerHeight;
    private int blockWidth, blockHeight;

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

    public void setPlayerSize(int playerWidth, int playerHeight) {
        this.playerHeight = playerHeight;
        this.playerWidth = playerWidth;
    }

    public int getPlayerWidth() {
        return playerWidth;
    }
    public int getPlayerHeight() {
        return playerHeight;
    }

    public void setBlockSize(int blockHeight, int blockWidth) {
        this.blockHeight = blockHeight;
        this.blockWidth = blockWidth;
    }

    public int getBlockWidth() {
        return blockWidth;
    }
    public int getBlockHeight() {
        return blockHeight;
    }
}
