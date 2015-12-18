package com.cstdio.game;

import android.content.Context;
import android.gesture.Gesture;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;

import java.util.Random;
import java.util.Vector;

public class GamePanel extends SurfaceView implements SurfaceHolder.Callback, GestureDetector.OnGestureListener {
    private Singleton singleton = Singleton.getMyInstance();
    public static final int WIDTH = 400;
    public static final int HEIGHT = 800;
    private MainThread thread;
    private Background background;
    private Player player;
    private Vector< Block > blocks = new Vector <>();
    private GestureDetector gestureDetector = new GestureDetector(getContext(), this);

    public GamePanel(Context context) {
        super(context);

        // add the callback to the surfaceHolder to intercept events
        getHolder().addCallback(this);

        thread = new MainThread(getHolder(), this);

        // make gamePanel focusable so it can handle events
        setFocusable(true);
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {

    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        boolean retry = true;

        System.out.println("DESTROYING!!!");

        while(retry) {
            try {
                thread.setRunning(false);
                thread.join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            retry = false;
        }
    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        singleton.setScreenSize(getHeight() / 2, getWidth() / 2);
        background = new Background(BitmapFactory.decodeResource(getResources(), R.drawable.background));
        player = new Player(getResources(), getContext());

        System.out.println(getHeight() + " " + getWidth());

        Random random = new Random();

        int blockX, blockY;
        blockX = singleton.getPlayerWidth() * 2;
        blockY = 600 / (520 / blockX);

        singleton.setBlockSize(blockY, blockX);

        blocks.add(new Block(getResources(),
                BitmapFactory.decodeResource(getResources(), R.drawable.triangle), 100, 500 + player.height));

        for(int i = -singleton.getScreenWidth(); i < singleton.getScreenWidth(); i += random.nextInt(singleton.getScreenWidth() / 2)) {
            //System.out.println("ALLOWED x: " + i);
            int y = -random.nextInt(singleton.getScreenHeight());

            while(y < singleton.getScreenHeight()) {
                if(!intersect(i, y, blockX, blockY)) {
                    blocks.add(new Block(getResources(),
                            BitmapFactory.decodeResource(getResources(), R.drawable.triangle), i, y));

                    //System.out.println("Drawing: " + i + " " + y);
                }

                y += blockY / 2 + random.nextInt(singleton.getScreenHeight() / 2);
            }
        }

        // now we can start the game loop
        if(!thread.isRunning()) {
            thread.setRunning(true);
            thread.start();
        }

        System.out.println("Block: " + singleton.getBlockWidth() + " " + singleton.getBlockHeight());
        System.out.println("Player: " + singleton.getPlayerWidth() + " " + singleton.getPlayerHeight());

        player.setPlaying(true);
    }

    public boolean intersect(int x, int y, int width, int height) {
        for(int i = 0; i < blocks.size(); i++) {
            Block block = blocks.elementAt(i);

            if(insideRectangle(x, y, block.x, block.y, block.x + width, block.y + height))
                return true;
            if(insideRectangle(x + width, y, block.x, block.y, block.x + width, block.y + height))
                return true;
            if(insideRectangle(x, y + height, block.x, block.y, block.x + width, block.y + height))
                return true;
            if(insideRectangle(x + width, y + height, block.x, block.y, block.x + width, block.y + height))
                return true;
        }

        return false;
    }

    public boolean insideRectangle(int x, int y, int fx, int fy, int sx, int sy) {
        return (fx <= x && x <= sx && fy <= y && y <= sy);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        this.gestureDetector.onTouchEvent(event);
        super.onTouchEvent(event);

        return true;
    }

    public void update() {
        if(player.getPlaying()) {
            background.update();
            for(int i = 0; i < blocks.size(); i++) {
                blocks.elementAt(i).update();
            }
            player.update();

            if(player.getAction() == 2 && player.getDirection() == 1) {
                onBlock(player.getPosx(), player.getPosy(), player.getDYA());
            }
        }
    }

    public boolean onBlock(int posx, int posy, double dya) {
        int dy = (int)dya;

        for(int i = 0; i < blocks.size(); i++) {
            int actX, actY;
            actX = blocks.elementAt(i).x - posx;
            actY = blocks.elementAt(i).y - posy;
            actX *= 2;

            int pX, pY;
            pX = player.x;
            pY = player.y;
            pX *= 2;

            //System.out.println("qwerty: " + );

            System.out.println("Player: " + pX + " " + " " + (pY + singleton.getPlayerHeight()));
            System.out.println("Block: " + actX + " " + actY);
            System.out.println("DYA: " + dya);

            if(actX <= pX + singleton.getPlayerWidth() / 2 &&
                    pX + singleton.getPlayerWidth() / 2 <= actX + singleton.getBlockWidth()) {
                if(pY + singleton.getPlayerHeight() < actY &&
                        actY <= pY + singleton.getPlayerHeight() + dy * 2) {
                    System.out.println("LANDING");
                    player.setDYA((double)(actY - (pY + singleton.getPlayerHeight())));
                    player.setLast(true);
                }
            }
        }

        return false;
    }

    @Override
    public void draw(Canvas canvas) {
        super.draw(canvas);

        // 768 x 1184
        final float scaleFactorX = (float) getWidth() / WIDTH;
        final float scaleFactorY = (float) getHeight() / HEIGHT;

        if(canvas != null) {
            final int savedState = canvas.save();
            canvas.scale(scaleFactorX, scaleFactorY);
            background.draw(canvas);
            for(int i = 0; i < blocks.size(); i++) {
                blocks.elementAt(i).draw(canvas, player.getPosx(), player.getPosy());
            }
            player.draw(canvas);

            canvas.restoreToCount(savedState);
        }
    }

    @Override
    public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
        System.out.println("onScroll gesture detected!!!");
        return false;
    }

    @Override
    public boolean onSingleTapUp(MotionEvent e) {
        System.out.println("onSingleTapUp gesture detected!!!");

        //jump
        if(player.getAction() != 2)
            player.setAction(2);

        return false;
    }

    @Override
    public boolean onDown(MotionEvent e) {
        System.out.println("onDown gesture detected!!!");
        return false;
    }

    @Override
    public void onShowPress(MotionEvent e) {
        System.out.println("onShowPress gesture detected!!!");
    }

    @Override
    public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
        System.out.println("onFling gesture detected!!!");

        // change edge of a triangle
        return false;
    }

    @Override
    public void onLongPress(MotionEvent e) {
        System.out.println("onLongPress  gesture detected!!!");
    }
}
