package com.example.Ball_Demo;

import android.content.Context;
import android.graphics.*;
import android.util.Log;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.WindowManager;
import android.view.Display;

/**
 * Created by darwinmendyke on 7/14/14.
 */
public class Panel extends SurfaceView implements SurfaceHolder.Callback {

    private static final String TAG = Panel.class.getSimpleName();

    public MainThread thread;

    public static Context theContext;

    public static int width;
    public static int height;

    private Bitmap ballImage = BitmapFactory.decodeResource(getResources(), R.drawable.ball);

    private BouncyBall ball;

    private Accelerometer accelerometer;

    // the fps to be displayed
    private String avgFps;
    public void setAvgFps(String avgFps) {
        this.avgFps = avgFps;
    }


    public Panel(Context context) {
        super(context);

        // adding the callback (this) to the surface holder to intercept events
        getHolder().addCallback(this);

        theContext = context;

        accelerometer = new Accelerometer(theContext);

        getScreenSize();

        ball = new BouncyBall(ballImage, width / 2, height / 2, 50);

        // create the game loop thread
        thread = new MainThread(getHolder(), this);

        // make the GamePanel focusable so it can handle events
        setFocusable(true);
    }

    public void getScreenSize() {
        WindowManager wm = (WindowManager) theContext.getSystemService(Context.WINDOW_SERVICE);
        Display display = wm.getDefaultDisplay();

        Point size = new Point();
        display.getSize(size);
        width = size.x;
        height = (int)(size.y * .9);
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) { }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        Log.d("D", "surface created");

        // at this point the surface is created and
        // we can safely start the game loop
        thread.setRunning(true);
        thread.start();
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        Log.d(TAG, "Surface is being destroyed");
        // tell the thread to shut down and wait for it to finish
        // this is a clean shutdown
        boolean retry = true;

        while (retry) {
            try {
                thread.join();
                retry = false;
            } catch (InterruptedException e) {
                // try again shutting down the thread
            }
        }
        //Log.d(TAG, "Thread was shut down cleanly");
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {

        switch (event.getAction() & MotionEvent.ACTION_MASK) {

            case MotionEvent.ACTION_DOWN:
                break;

            // another tap tells Link to jump
            case MotionEvent.ACTION_POINTER_DOWN:
                break;

            case MotionEvent.ACTION_UP:
                break;
            }

        return true;
    }

    public void render(Canvas canvas) {

        canvas.drawColor(Color.BLACK);

        ball.draw(canvas);

        // display fps
        displayFps(canvas, avgFps);
    }

    public void update() {
        ball.update(System.currentTimeMillis());
    }

    private void displayFps(Canvas canvas, String fps) {
        if (canvas != null && fps != null) {
            Paint paint = new Paint();
            paint.setARGB(255, 255, 255, 255);
        }
    }
}
