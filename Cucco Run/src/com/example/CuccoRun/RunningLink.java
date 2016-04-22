package com.example.CuccoRun;

import android.graphics.Bitmap;
import android.graphics.Rect;

/**
 * This class represents the character that runs across the screen and jumps over cuccos.
 */
public class RunningLink extends AnimatedObject {
    //private static final String TAG = RunningLink.class.getSimpleName();

    //determines whether Link is jumping
    static public boolean jumpingUp = false;
    static public boolean jumpingDown = false;

    private int jumpIncrement = 0; // used to determine the vertical distance traveled

    /**
     * The initializer for runningLink.
     *
     * @param bitmap the path of the sprite image
     * @param x the center-left coordinate for the starting position of the object
     * @param y the center-top coordinate for the starting position of the object
     * @param fps the speed of the object when moving on the screen
     * @param frameCount the number of frames on the sprite sheet (if exists)
     */
    public RunningLink (Bitmap bitmap, int x, int y, int fps, int frameCount) {

        super(bitmap, x, y, fps, frameCount); // passes the parameters to the parent class

        onScreen = true;

        // defines the initial source rectangle and destination rectangle for the character
        destRect = new Rect(x, y, x + spriteWidth, y + spriteHeight);
        sourceRect = new Rect(0, 0, spriteWidth, spriteHeight);

        currentFrame = 3; // sets the initial frame eon the sprite sheet
    }

    /**
     * Moves the source frame along the sprite sheet.
     *
     * @param gameTime
     */
    @Override
    public void update(long gameTime) {
        if ((isRunning) && (gameTime > frameTicker + framePeriod)) {
            frameTicker = gameTime;

            // checks to see if Link should move vertically
            if (jumpingUp || jumpingDown)
                jump();

            currentFrame--;

            if (currentFrame < 0) {
                currentFrame = 3;
            }
        }
    }

    /**
     * Method to move Link vertically when he is told to jump.
     */
    public void jump() {

        // increments the jumpincrement
        if (jumpingUp) {
            jumpIncrement++;
            if (jumpIncrement == 1)
                framePeriod /= 4;
        }
        if (jumpingDown)
            jumpIncrement--;

        // once he reaches the apex of the jump, tells the object to move down
        if (jumpIncrement >= 11) {
            jumpingUp = false;
            jumpingDown = true;

            jumpIncrement--;
        }

        // resets to initial state after finishes jumping
        if (jumpIncrement <= 0) {
            jumpingDown = false;
            framePeriod *= 4;
        }

        // the vertical change in height for the next update, dependent on the value of jumpIncrement
        int changeHeight = (int)(100 * Math.pow(.8, jumpIncrement));

        if (jumpingUp)
            destRect.set(destRect.left, destRect.top - changeHeight, destRect.right, destRect.bottom - changeHeight);

        if (jumpingDown)
            destRect.set(destRect.left, destRect.top + changeHeight, destRect.right, destRect.bottom + changeHeight);
    }
}
