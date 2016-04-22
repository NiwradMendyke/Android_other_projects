package com.example.CuccoRun;

import android.graphics.Bitmap;
import android.graphics.Rect;

/**
 * The class that represents the cuccos that run across the screen and must be jumped over.
 *
 * Created by darwinmendyke on 7/3/14.
 */
public class Cucco extends Obstacles {

    Rect start; // the initial destRect for the class off screen

    /**
     * The initializer for cucco.
     *
     * @param bitmap the path of the sprite image
     * @param x the center-left coordinate for the starting position of the object
     * @param y the center-top coordinate for the starting position of the object
     * @param fps the speed of the object when moving on the screen
     * @param scale the size of the cucco relative to the regular cucco size
     */
    public Cucco (Bitmap bitmap, int x, int y, int fps, int scale) {

        super(bitmap, x, y, fps, 1);

        start = new Rect(x, y - spriteHeight / 2 * scale, x + spriteWidth / 2 * scale, y);

        destRect = start;
        sourceRect = new Rect(0, 0, spriteWidth, spriteHeight);

        onScreen = false;
    }

    private int change = spriteWidth / 10; // the value of the change in horizontal space during each update

    /**
     * The update method specific for Cucco objects.
     * Every update, moves the sprite of the object horizontally a distance equal to change until it leaves the screen.
     * Once the cucco leaves the screen, it resets the cucco back to the start.
     * And it informs GamePanel that the cucco is off the screen, allowing the game to remove the cucco from the onScreen arraylist.
     *
     * @param gameTime
     */
    @Override
    public void update(long gameTime) {
        if ((isRunning) && (gameTime > frameTicker + framePeriod)) {
            frameTicker = gameTime;

            //Log.d("a", "left edge: " + destRect.left + " and right edge: " + destRect.right);

            if (destRect.right >= 0) {
                destRect = new Rect(destRect.left - change, destRect.top, destRect.right - change, destRect.bottom);
                //Log.d("D", "moving the bird");
            }
            if (destRect.right <= 0) {
                onScreen = false;
                destRect = start;
                //Log.d("D", "setting bird back to right edge of screen");
            }
        }
    }
}
