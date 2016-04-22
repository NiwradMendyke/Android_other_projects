package com.example.CuccoRun;

import android.graphics.Bitmap;
import android.graphics.Rect;
import android.graphics.Canvas;

/**
 * Created by darwinmendyke on 7/8/14.
 */
public class DyingLink extends AnimatedObject {

    public DyingLink (Bitmap bitmap, int x, int y, int fps, int frameCount) {

        super(bitmap, x, y, fps, frameCount);

        onScreen = true;

        destRect = new Rect(x - spriteWidth * 5 / 2, y, x + spriteWidth * 5 / 2, y + spriteHeight * 5);
        sourceRect = new Rect(0, 0, spriteWidth, spriteHeight);

        onScreen = true;

        //currentFrame = 1;
    }

    @Override
    public void update(long gameTime) { }

    @Override
    public void draw(Canvas canvas) {

        canvas.drawBitmap(bitmap, sourceRect, destRect, null);
    }
}
