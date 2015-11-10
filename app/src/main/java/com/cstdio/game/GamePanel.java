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

public class GamePanel extends SurfaceView implements SurfaceHolder.Callback, GestureDetector.OnGestureListener {
    private Singleton singleton = Singleton.getMyInstance();;
    public static final int WIDTH = 400;
    public static final int HEIGHT = 800;
    private MainThread thread;
    private Background background;
    private Player player;
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
        singleton.setScreenSize(getHeight(), getWidth());
        background = new Background(BitmapFactory.decodeResource(getResources(), R.drawable.background));
        player = new Player(getResources(), getContext());

        // now we can start the game loop
        if(!thread.isRunning()) {
            thread.setRunning(true);
            thread.start();
        }
        player.setPlaying(true);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        this.gestureDetector.onTouchEvent(event);
        super.onTouchEvent(event);

        return true;
    }

    public void update() {
        if(player.getPlaying()) {
            player.update();
            background.update();
        }
    }

    @Override
    public void draw(Canvas canvas) {
        super.draw(canvas);

        // 768 x 1184
        final float scaleFactorX = (float) singleton.getScreenWidth() / WIDTH;
        final float scaleFactorY = (float) singleton.getScreenHeight() / HEIGHT;

        if(canvas != null) {
            final int savedState = canvas.save();
            canvas.scale(scaleFactorX, scaleFactorY);
            background.draw(canvas);
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
