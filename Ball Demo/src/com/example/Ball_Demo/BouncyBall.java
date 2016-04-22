package com.example.Ball_Demo;

import android.graphics.Bitmap;
import android.graphics.Rect;
import android.graphics.Canvas;
import android.media.SoundPool;
import android.media.SoundPool.OnLoadCompleteListener;
import android.media.AudioManager;
import android.util.Log;

/**
 * Created by darwinmendyke on 7/14/14.
 */
public class BouncyBall {

    private Bitmap bitmap;      //the animation sequence
    private Rect sourceRect;    //the rectangle to be drawn from animation bitmap
    private Rect destRect;      //the rectangle to be drawn on the screen
    private int frameNr;        //number of frames in the animation
    private long frameTicker;   //the time since last frame update
    public int framePeriod;    //milliseconds between each frame

    private int spriteWidth;    //width of the sprite
    private int spriteHeight;   //height of the sprite

    private int x;              //x coordinate of the object (top left)
    private int y;              //y coordinate of the object (top left)

    private double velocityX = 0;
    private double velocityY = 0;

    private SoundPool sound;
    private static final int s1 = R.raw.ball_bounce;
    private int soundID;

    public static boolean isBouncing = false;

    public BouncyBall (Bitmap bitmap, int x, int y, int fps) {

        this.bitmap = bitmap;

        this.x = x;
        this.y = y;

        spriteWidth = bitmap.getWidth() / 3;
        spriteHeight = bitmap.getHeight() / 3;

        sourceRect = new Rect(0, 0, bitmap.getWidth(), bitmap.getHeight());
        destRect = new Rect(x - spriteWidth / 2, y - spriteHeight / 2, x + spriteWidth / 2, y + spriteHeight / 2);

        framePeriod = 1000 / fps;
        frameTicker = 01;

        sound = new SoundPool(1, AudioManager.STREAM_MUSIC, 100);
        soundID = sound.load(Panel.theContext, R.raw.ball_bounce, 1);
    }

    public void update(Long gameTime) {
        velocityX = velocityX + Accelerometer.x / 150;
        velocityY = velocityY + Accelerometer.y / 150;

        Rect newRect;

        double velocity = Math.sqrt((Math.pow(velocityX, 2) + Math.pow(velocityY, 2)));
        double accerleration = Math.sqrt((Math.pow(Accelerometer.x / 150, 2) + Math.pow(Accelerometer.y / 150, 2)));

        //Log.d("Bouncyball", "velocityX = " + velocityX + " velocityY = " + velocityY);
        //Log.d("Bouncyball", "the velocity is " + velocity);

        if (destRect.left < 0) {
            if (velocityX >= .05) { }
            else {
                velocityX = 0 - velocityX * .7;
                playSound();
            }
        }

        if (destRect.right > Panel.width) {
            if (velocityX <= -.05) { }
            else {
                velocityX = 0 - velocityX * .7;
                playSound();
            }
        }

        if (destRect.top < 0) {
            if (velocityY >= .05) {
            } else {
                velocityY = 0 - velocityY * .7;
                playSound();
            }
        }

        if (destRect.bottom > Panel.height) {
            if (velocityY <= -.05) { }
            else {
                velocityY = 0 - velocityY * .7;
                playSound();
            }
            //Log.d("Bouncyball", "the velocity is " + velocityY);
        }

        newRect = new Rect(destRect.left + (int)velocityX, destRect.top + (int)velocityY, destRect.right + (int)velocityX, destRect.bottom + (int)velocityY);

        destRect = newRect;

        friction();

    }

    public void friction() {
        if (velocityX < 0)
            velocityX += .05;

        if (velocityX > 0)
            velocityX -= .05;

        if (velocityY < 0)
            velocityY += .05;

        if (velocityY > 0)
            velocityY -= .05;

        if (Math.abs(velocityX) <= .03)
            velocityX = 0;
        if (Math.abs(velocityY) <= .03)
            velocityY = 0;
    }

    public void playSound() {
        if (sound != null) {
            //Log.d("sound issues", "playing sound");
            sound.play(soundID, 1, 1, 1, 0, 1f);
        }
    }

    public void draw(Canvas canvas) {
        canvas.drawBitmap(bitmap, sourceRect, destRect, null);
    }
}
