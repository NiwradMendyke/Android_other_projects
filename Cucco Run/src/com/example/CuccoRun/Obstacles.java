package com.example.CuccoRun;

import android.graphics.Bitmap;

/**
 * Created by darwinmendyke on 7/7/14.
 */
public abstract class Obstacles extends AnimatedObject {

    public Obstacles (Bitmap bitmap, int x, int y, int fps, int frameCount) {
        super(bitmap, x, y, fps, frameCount);
    }


    //public void update(long gametime) { }
}
