package com.cstdio.game;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.util.Log;

public class Player extends GameObject implements SensorEventListener {
    private Singleton singleton = Singleton.getMyInstance();;
    private int score;
    private double dya, dxa;
    private boolean up;
    private boolean playing;
    private long startTime;
    private Resources resources;
    private int currentAction; // 0 - idle, 1 - run, 2 - jump
    private int direction = 1;
    private int jumpDirection;
    private int posx, posy;
    private boolean last  = false;

    private Sensor accel;
    private SensorManager sensorManager;

    private MyAnimation[] myAnimations = new MyAnimation[3];

    private int runFrames = 6, jumpFrames = 2, idleFrames = 2;

    private Bitmap[] runBitmap = new Bitmap[runFrames];
    private Bitmap[] jumpBitmap = new Bitmap[jumpFrames];
    private Bitmap[] idleBitmap = new Bitmap[idleFrames];

    public void initBitmaps() {
        Bitmap b;
        // 479 x 797
        b = BitmapFactory.decodeResource(resources, R.drawable.run1);
        runBitmap[0] = Bitmap.createScaledBitmap(b, width, height, true);
        b = BitmapFactory.decodeResource(resources, R.drawable.run2);
        runBitmap[1] = Bitmap.createScaledBitmap(b, width, height, true);
        b = BitmapFactory.decodeResource(resources, R.drawable.run3);
        runBitmap[2] = Bitmap.createScaledBitmap(b, width, height, true);
        b = BitmapFactory.decodeResource(resources, R.drawable.run4);
        runBitmap[3] = Bitmap.createScaledBitmap(b, width, height, true);
        b = BitmapFactory.decodeResource(resources, R.drawable.run5);
        runBitmap[4] = Bitmap.createScaledBitmap(b, width, height, true);
        b = BitmapFactory.decodeResource(resources, R.drawable.run6);
        runBitmap[5] = Bitmap.createScaledBitmap(b, width, height, true);

        // 490 x 845, 559 x 764
        b = BitmapFactory.decodeResource(resources, R.drawable.jumpup);
        jumpBitmap[0] = Bitmap.createScaledBitmap(b, width, height, true);
        b = BitmapFactory.decodeResource(resources, R.drawable.jumpfall);
        jumpBitmap[1] = Bitmap.createScaledBitmap(b, width, height, true);

        // 499 x 750
        b = BitmapFactory.decodeResource(resources, R.drawable.idle1);
        idleBitmap[0] = Bitmap.createScaledBitmap(b, width, height, true);
        b = BitmapFactory.decodeResource(resources, R.drawable.idle2);
        idleBitmap[1] = Bitmap.createScaledBitmap(b, width, height, true);
    }

    public void initAnimations() {
        myAnimations[0] = new IdleAnimation(idleBitmap, 20);
        myAnimations[1] = new RunAnimation(runBitmap, 7);
        myAnimations[2] = new JumpAnimation(jumpBitmap, 0);
    }

    public Player(Resources resources, Context context) {
        sensorManager = (SensorManager) context.getSystemService(Context.SENSOR_SERVICE);
        accel = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        sensorManager.registerListener(this, accel, SensorManager.SENSOR_DELAY_UI);

        this.resources = resources;
        currentAction = 1;
        // initial position of a player
        posx = posy = 0;
        x = 100;
        y = 500;
        dy = 0;
        score = 0;

        height = singleton.getScreenHeight() / 8;
        width = singleton.getScreenWidth() / 8;

        singleton.setPlayerSize(width, height);

        initBitmaps();
        initAnimations();

        startTime = System.nanoTime();
    }

    public void setAction(int actionID) {
        currentAction = actionID;

        if(actionID == 2) {
            jumpDirection = -1;
            dya = -10.0;
        }
    }

    public int getAction() {
        return currentAction;
    }

    public void update() {
        long elapsed = (System.nanoTime() - startTime) / 1000000;
        if(elapsed > 100) {
            score++;
            startTime = System.nanoTime();
        }

        if (currentAction == 2) {
            if(jumpDirection < 0) {
                myAnimations[currentAction].setFrame(0);
            }
            else {
                myAnimations[currentAction].setFrame(1);
            }

            dy = (int)dya;

            if(dya >= 0)
                jumpDirection = 1;
            // y += dy * 2;
            posy += dy * 2;

            dy = 0;
            if(dya < 10.0)
                dya += 1.0;

            if(last) {
                setAction(0);
                last = false;
            }
        }

        dx = (int) dxa;

        if(x + dx * 2 >= 100 && x + dx * 2 <= singleton.getScreenWidth() / 2 - 100) {
            x += dx * 2;
        }
        else {
            posx += dx * 2;
        }
        dx = 0;

        myAnimations[currentAction].update();
    }

    public int getPosx() {
        return posx;
    }

    public int getPosy() {
        return posy;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public int getDirection() {
        return direction;
    }

    public void draw(Canvas canvas) {
        canvas.drawBitmap(myAnimations[currentAction].getImage(direction), x, y, null);
    }

    public int getPlayerWidth() {
        return width;
    }

    public int getScore() {
        return score;
    }

    public boolean getPlaying() {
        return playing;
    }

    public void setPlaying(boolean b) {
        playing = b;
    }

    public void resetDYA() {
        dya = 0;
    }

    public double getDYA() {
        return dya;
    }

    public void setDYA(double dya) {
        this.dya = dya / 2.0;
    }

    public void setLast(boolean b) {
        last = b;
    }

    public void resetScore() {
        score = 0;
    }


    @Override
    public void onSensorChanged(SensorEvent event) {
        // moving player left or right

        if(event.values[0] < -1.5) {
            direction = 1;

            if(dxa > 0 && dxa < 5)
                dxa += 1.3;
            else if(dxa <= 0)
                dxa = 1.3;

            if(currentAction != 2) {
                currentAction = 1;
            }
        }
        else if(event.values[0] > 1.5) {
            direction = -1;

            if(dxa < 0 && dxa > -5)
                dxa += -1.3;
            else if(dxa >= 0)
                dxa = -1.3;

            if(currentAction != 2) {
                currentAction = 1;
            }
        }
        else {
            dxa = 0;
            if(currentAction != 2) {
                currentAction = 0;
            }
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }
}
