package com.example.CuccoRun;

import android.graphics.*;
import android.util.Log;

public class BackGround extends AnimatedObject {

    private int actualHeight = bitmap.getHeight();
    private int actualWidth = bitmap.getWidth();

    public boolean isFaded = false;

    public double ratio;

    private int change;

    public BackGround(Bitmap bitmap, int x, int y, int fps, int frameCount, int w, int h) {

        super(bitmap, x, y, fps, frameCount);

        int screenHeight = h;
        int screenWidth = w;

        onScreen = true;

        ratio = (double)screenWidth / screenHeight;

        spriteHeight = (int)(spriteHeight * .5);
        spriteWidth = (int) (ratio * spriteHeight);

        //sets display rectangle equal to screen size
        destRect = new Rect(x, y, x + screenWidth, y + screenHeight);
        sourceRect = new Rect(0, actualHeight - spriteHeight, spriteWidth, actualHeight);

        onScreen = true;

        change = (actualWidth - spriteWidth) / 720;
    }

    @Override
    public void update(long gameTime) {
        if ((isRunning) && (gameTime > frameTicker + framePeriod)) {

            sourceRect.set(sourceRect.left + change, sourceRect.top, sourceRect.right + change, sourceRect.bottom);

            //Log.d("a", "the value of sourceRect.right = " + sourceRect.right);

            if (sourceRect.right >= ((actualWidth * .5) + (spriteWidth - change)))
                sourceRect.set(0, actualHeight - spriteHeight, spriteWidth, actualHeight);

            //sourceRect.setLocation()
        }

    }

    @Override
    public void draw(Canvas canvas) {

        Paint p = new Paint();

        if (isFaded) {
            p.setAlpha(40);

        }

        // where to draw the sprite
        canvas.drawBitmap(bitmap, sourceRect, destRect, p);
    }
}
