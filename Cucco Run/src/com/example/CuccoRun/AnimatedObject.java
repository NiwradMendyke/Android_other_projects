package com.example.CuccoRun;

import android.graphics.*;

/**
 * This is the abstract superclass that represents all the objects drawn on the screen.
 */
public abstract class AnimatedObject {

    //public int width = 1280; // the width of the screen
    //public int height = 650; // the heigh of the screen
    public boolean onScreen;

    protected Bitmap bitmap;      //the animation sequence
    protected Rect sourceRect;    //the rectangle to be drawn from animation bitmap
    protected Rect destRect;      //the rectangle to be drawn on the screen
    protected int frameNr;        //number of frames in the animation
    protected long frameTicker;   //the time since last frame update
    public int framePeriod;    //milliseconds between each frame
    protected int currentFrame;   //current frame

    protected int spriteWidth;    //width of the sprite
    protected int spriteHeight;   //height of the sprite

    protected int x;              //x coordinate of the object (top left)
    protected int y;              //y coordinate of the object (top left)

    static protected boolean isRunning = false; // the static boolean that determines whether animatedObject moves

    /**
     *
     * The initializer for AnimatedObject.
     * Sets some of the variables the the passed parameters.
     *
     * @param bitmap the path of the sprite image
     * @param x the center-left coordinate for the starting position of the object
     * @param y the center-top coordinate for the starting position of the object
     * @param fps the speed of the object when moving on the screen
     * @param frameCount the number of frames on the sprite sheet (if exists)
     */
    public AnimatedObject (Bitmap bitmap, int x, int y, int fps, int frameCount) {

        this.bitmap = bitmap;

        this.x = x;
        this.y = y;

        frameNr = frameCount;
        spriteWidth = bitmap.getWidth() / frameCount;
        spriteHeight = bitmap.getHeight();

        framePeriod = 1000 / fps;
        frameTicker = 01;
    }

    public abstract void update(long gameTime);

    /**
     * Advances the sourceRect for sprite sheets.
     * Then draws the object on the screen based on the bitmap, the source and the destination.
     *
     * @param canvas
     */
    public void draw(Canvas canvas) {

        // define the rectangle to cut out sprite
        this.sourceRect.left = currentFrame * spriteWidth;
        this.sourceRect.right = this.sourceRect.left + spriteWidth;

        // where to draw the sprite

        canvas.drawBitmap(bitmap, sourceRect, destRect, null);
    }

    public int getLeft() {
        return destRect.left;
    }

    public int getRight() {
        return destRect.right;
    }

    public int getWidth() { return spriteWidth; }

    public int getHeight() { return spriteHeight; }
}
