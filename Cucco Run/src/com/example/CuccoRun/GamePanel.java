package com.example.CuccoRun;
import com.example.TheHeroRuns.R;

import android.content.Context;
import android.graphics.*;
import android.util.Log;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import java.io.*;
import java.util.Random;
import java.util.ArrayList;
import android.widget.Toast;
import android.view.Display;
import android.view.WindowManager;

/**
 * @author impaler
 * This is the main surface that handles the ontouch events and draws
 * the image to the screen.
 */
public class GamePanel extends SurfaceView implements SurfaceHolder.Callback {

    private static final String TAG = GamePanel.class.getSimpleName();

    public MainThread thread;
    public Context theContext;

    public static int width;
    public static int height;

    public int width1 = 1280; // the width of the screen
    public int height1 = 650; // the heigh of the screen

    // creates the bitmaps the objects will be using
    Bitmap background = BitmapFactory.decodeResource(getResources(), R.drawable.background);
    Bitmap linkSprite = BitmapFactory.decodeResource(getResources(), R.drawable.linkframe);
    Bitmap chicken = BitmapFactory.decodeResource(getResources(), R.drawable.cucco);

    // creates the list of objects on the screen
    private ArrayList<AnimatedObject> objectsOnScreen = new ArrayList<AnimatedObject>();
    private RunningLink link;
    private BackGround back;

    // creates the cuccos that start off the screen
    private ArrayList<AnimatedObject> objectsOffScreen = new ArrayList<AnimatedObject>();
    private Cucco cucco1, cucco2, cucco3, cucco4;


    private int numberOfCuccos = 0; // the number of cuccos the player jumps over
    private int difficulty = 5; // the speed with which the cuccos move towards the player
    private int highScore;

    // creates the image that appears on the death screen
    private DyingLink dyingLink;

    // where link and ground level cuccos start
    private int groundLevel;

    // various booleans
    private boolean isDeathScreen = false;
    public boolean startAgain = false;

    // different text objects for the death screen
    private Paint linkDied = new Paint();
    private Paint highestScore = new Paint();
    private Paint clickContinue = new Paint();

    // the fps to be displayed
    private String avgFps;
    public void setAvgFps(String avgFps) {
        this.avgFps = avgFps;
    }


    /**
     * The initializer for GamePanel.
     * Declares most of the above variables.
     * Places the Animated objects into either onscreen arraylist or offscreen arraylist.
     * @param context
     */
    public GamePanel(Context context) {
        super(context);

        // adding the callback (this) to the surface holder to intercept events
        getHolder().addCallback(this);

        theContext = context;

        getScreenSize();

        // create objects and load bitmap
        back = new BackGround(background, 0, 0, 5, 2, width, height);
        groundLevel = (int)(height * .67);
        link = new RunningLink(linkSprite, (int)(.25 * width), groundLevel, 10, 4);
        groundLevel += link.spriteHeight;
        cucco1 = new Cucco(chicken, width, groundLevel, 50, 1);
        cucco2 = new Cucco(chicken, width, (int)(groundLevel * .8), 50, 1);
        cucco3 = new Cucco(chicken, width, (int)(groundLevel * .9), 50, 1);
        cucco4 = new Cucco(chicken, width, groundLevel, 50, 2);

        dyingLink = new DyingLink(BitmapFactory.decodeResource(getResources(), R.drawable.dying), width / 2, height / 2, 10, 1);

        // adds objects to onscreen arraylist
        objectsOnScreen.add(back);
        objectsOnScreen.add(link);

        // adds objects to offscreen arraylist
        objectsOffScreen.add(cucco1);
        objectsOffScreen.add(cucco2);
        objectsOffScreen.add(cucco3);

        // tells player the controls
        Toast message = Toast.makeText(theContext, "Press the screen to make Link run, tap screen again to make Link jump. Dodge the cuccos.", Toast.LENGTH_LONG);
        message.show();

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

    /**
     * The method that detects taps on the screen,
     * sorts them, and calls other methods based on the state of several booleans.
     * @param event
     * @return
     */
    @Override
    public boolean onTouchEvent(MotionEvent event) {

        // only calls these if not on the death screen
        if (!isDeathScreen)
            switch (event.getAction() & MotionEvent.ACTION_MASK) {

                // one finger means Link is running
                case MotionEvent.ACTION_DOWN:
                    AnimatedObject.isRunning = true;
                    break;

                // another tap tells Link to jump
                case MotionEvent.ACTION_POINTER_DOWN:
                    if (RunningLink.jumpingDown || RunningLink.jumpingUp)
                        break;
                    //Log.d("a", "ACTION_POINTER_DOWN");
                    RunningLink.jumpingUp = true;
                    numberOfCuccos++;
                    difficulty++;
                    break;

                case MotionEvent.ACTION_UP:
                    AnimatedObject.isRunning = false;
                    //Log.d("a", "ACTION_UP");
                    break;
            }

        // but if deathscreen is true, a tap restarts the game
        else if (event.getAction() == MotionEvent.ACTION_DOWN)
            startAgain = true;

        return true;
    }

    /**
     * The method that actually creates the bitmaps on the screen.
     * @param canvas
     */
    public void render(Canvas canvas) {

        canvas.drawColor(Color.BLACK);

        drawText(canvas);

        //goes through and draws each object in objectsOnScreen
        for (AnimatedObject o : objectsOnScreen)
            o.draw(canvas);

        // checks to see if there is a collision
        for (AnimatedObject o : objectsOnScreen)
            if (o.getClass().equals(Cucco.class))
                if (checkForCollision(link, (Cucco) o))
                    break;


        // display fps
        displayFps(canvas, avgFps);
    }

    /**
     * This is the game update method.
     * It iterates through the objects in the two arraylists
     * and updates each object's postion and image on the screen.
     */
    public void update() {
        if (startAgain)
            restart();

        // checks to see how many cuccos, if over 20, adds the big cucco
        if (difficulty >= 25) {
            objectsOffScreen.add(cucco4);
        }

        // if Link is running, adds cuccos to the screen and updates each object
        if (AnimatedObject.isRunning) {
            createObstacles();

            for (AnimatedObject o : objectsOnScreen)
                o.update(System.currentTimeMillis());
        }

        //Log.d("D", "updating...");
    }

    private void displayFps(Canvas canvas, String fps) {
        if (canvas != null && fps != null) {
            Paint paint = new Paint();
            paint.setARGB(255, 255, 255, 255);
        }
    }

    /**
     * The method that adds cuccos to the screen.
     * First checks if the cuccos on the screen are a certain distance across.
     * This distance decreases as the game goes on, making it more and more difficult.
     * Then for each cucco in offscreen, it has a certain % chance of adding another cucco.
     * And finally, it checks to see if the cucco has reached the end of the screen.
     * If so, it removes the cucco from the onscreen arraylist.
     */
    private void createObstacles() {
        //Log.d("D", "starting createObstacles() method");

        // creates the random int generator
        Random randomInt = new Random();

        boolean isRoom = true;

        // checks to see if there is room on the screen for another cucco
        for (AnimatedObject o : objectsOnScreen)    {
            if (o.getClass().equals(Cucco.class))
                if (o.getRight() > (width * (.3 + (.01 * (difficulty - 5))))) // the space examined depends on the value of difficulty
                    isRoom = false;
        }

        // the percent chance also depends on the value of difficulty
        if (isRoom)
            for ( AnimatedObject o : objectsOffScreen ) {
                int random = randomInt.nextInt(100);

                if (random <= difficulty / 4) {
                    objectsOnScreen.add(o);
                    //Log.d("GamePanel", "Another cucco was added on screen");
                    o.onScreen = true;

                    break;
                }
            }

        // checks each cucco to see if it should be removed from the on screen arraylist
        for ( int a = 0; a < objectsOnScreen.size(); a++) {

            AnimatedObject object = objectsOnScreen.get(a);

            if (!object.onScreen) {
                objectsOnScreen.remove(a);
                //Log.d("GamePanel", "a bird has been removed");

                // increments both difficulty and number of cuccos jumped over
                //numberOfCuccos++;
                //Log.d("GamePanel", "the value of numberOfCuccos is now " + numberOfCuccos);
                //difficulty++;
                //Log.d("D", "bird has been removed");
            }
        }
    }

    /**
     * Takes link and the cucco in question and checks if there is a collision.
     * First checks if there is an intersection between the destRects.
     * If there is, the method takes the area of intersection and checks if there any non-transparent pixels.
     * If there are, it means there is a collision.
     *
     * @param sprite1 Link, the character jumping over the cucco
     * @param sprite2 the cucco, the thing Link needs to jump over
     * @return
     */
    private boolean checkForCollision(RunningLink sprite1, Cucco sprite2) {
        if (sprite1.x<0 && sprite2.x<0 && sprite1.y<0 && sprite2.y<0)
            return false;

        // creates rectangles used below to avoid aliasing
        Rect r1 = new Rect(sprite1.destRect.left, sprite1.destRect.top, sprite1.destRect.right, sprite1.destRect.bottom);
        Rect r2 = new Rect(sprite2.destRect.left, sprite2.destRect.top, sprite2.destRect.right, sprite2.destRect.bottom);
        Rect r3 = new Rect(r1);

        // checks the area of intersection if it exists
        if(r1.intersect(r2)) {
            for (int i = r1.left; i < r1.right; i++) {
                for (int j = r1.top; j < r1.bottom; j++) {
                    if (linkSprite.getPixel( i-r3.left, j-r3.top ) != Color.TRANSPARENT) {
                        if (chicken.getPixel( i-r2.left, j-r2.top ) != Color.TRANSPARENT) {
                            AnimatedObject.isRunning = false;

                            // tells the program that it should go to the death screen
                            isDeathScreen = true;

                            objectsOnScreen.clear();

                            back.isFaded = true;

                            objectsOnScreen.add(back);
                            objectsOnScreen.add(dyingLink);

                            return true;
                        }
                    }
                }
            }
        }
        return false;
    }

    /**
     * Method that creates and sets the text on the death screen.
     * @param canvas
     */
    public void drawText(Canvas canvas) {

        if (isDeathScreen) {
            Log.d("GamePanel", "the value of highScore = " + highScore);
            if (highScore == 0)
                highScore = getHighscore();

            linkDied.setColor(Color.WHITE);
            linkDied.setTextSize(80);
            linkDied.setTypeface(Typeface.DEFAULT_BOLD);
            linkDied.setTextAlign(Paint.Align.CENTER);

            canvas.drawText("LINK DIED", width / 2, 200, linkDied);

            highestScore.setColor(Color.WHITE);
            highestScore.setTextSize(40);
            highestScore.setTypeface(Typeface.DEFAULT_BOLD);
            highestScore.setTextAlign(Paint.Align.CENTER);

            if (highScore == numberOfCuccos)
                canvas.drawText("New high score!!", width/ 2, 270, highestScore);
            else
                canvas.drawText("High score: " + highScore, width / 2, 270, highestScore);

            canvas.drawText("Your score: " + numberOfCuccos, width / 2, 320, highestScore);

            clickContinue.setColor(Color.WHITE);
            clickContinue.setTextSize(20);
            clickContinue.setTypeface(Typeface.DEFAULT_BOLD);
            clickContinue.setTextAlign(Paint.Align.CENTER);

            canvas.drawText("tap anywhere to continue", width / 2, dyingLink.destRect.bottom + 30, clickContinue);
        }
    }


    /**
     * Method that resets most of the variables and objects of GamePanel.
     * Called after the user taps the death screen to restart the game.
     */
    public void restart() {
        objectsOnScreen.clear();

        numberOfCuccos = 0;
        difficulty = 5;

        back.isFaded = false;
        RunningLink.jumpingUp = false;
        RunningLink.jumpingDown = false;

        back = new BackGround(background, 0, 0, 5, 2, width, height);

        groundLevel = (int)(height * .67);

        link = new RunningLink(linkSprite, (int)(.25 * width), groundLevel, 10, 4);

        groundLevel += link.spriteHeight;

        cucco1 = new Cucco(chicken, width, groundLevel, 50, 1);
        cucco2 = new Cucco(chicken, width, (int)(groundLevel * .8), 50, 1);
        cucco3 = new Cucco(chicken, width, (int)(groundLevel * .9), 50, 1);
        cucco4 = new Cucco(chicken, width, groundLevel, 50, 2);

        objectsOnScreen.add(back);
        objectsOnScreen.add(link);

        objectsOffScreen.clear();

        objectsOffScreen.add(cucco1);
        objectsOffScreen.add(cucco2);
        objectsOffScreen.add(cucco3);

        startAgain = false;

        isDeathScreen = false;

        highScore = 0;
    }

    public int getHighscore() {
        int highScore = numberOfCuccos;

        File file1 = new File("/storage/emulated/0/Android/data/com.example.TheHeroRuns/files/highscores.txt");

        try {
            InputStream in = new FileInputStream(file1);
            InputStreamReader tmp = new InputStreamReader(in);
            BufferedReader reader = new BufferedReader(tmp);

            int temp = Integer.parseInt(reader.readLine());

            //Log.d("GamePanel", "value of the saved score is " + temp);

            if (temp > highScore)
                highScore = temp;

        }   catch (Throwable t) { }

        FileWriter fileWriter;

        file1.delete();

        File file2 = new File(theContext.getExternalFilesDir(null), "highscores.txt");

        try {
            //Log.d("GamePanel", "saving the score");
            fileWriter = new FileWriter(file2, true);
            BufferedWriter bufferedWriter = new BufferedWriter(fileWriter);

            bufferedWriter.write("" + highScore);

            bufferedWriter.close();
        }   catch (IOException e) { }

        return highScore;
    }
}

