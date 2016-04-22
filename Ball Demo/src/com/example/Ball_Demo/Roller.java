package com.example.Ball_Demo;

import android.app.Activity;
import android.os.Bundle;
import android.content.pm.ActivityInfo;
import android.util.Log;

public class Roller extends Activity {

    Panel panel;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        panel = new Panel(this);

        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);

        setContentView(panel);
    }

    @Override
    public void onStart() {
        Log.d("D", "starting onStart");

        super.onStart();

        MainThread.mPaused = false;
    }

    @Override
    public void onResume() {
        Log.d("D", "starting onResume");

        super.onResume();

        /**synchronized (MainThread.mPauseLock) {
         MainThread.mPaused = false;
         MainThread.mPauseLock.notifyAll();
         }**/

        MainThread.mPaused = false;

        MainThread.running = true;
    }

    @Override
    public void onRestart() {
        Log.d("D", "starting onRestart");

        super.onRestart();
    }

    @Override
    public void onPause() {
        Log.d("D", "starting onPause");

        /**synchronized (MainThread.mPauseLock) {
         MainThread.mPaused = true;
         }**/

        MainThread.mPaused = true;

        super.onPause();
        Log.d("D", "ending onPause");
    }

    @Override
    public void onStop() {
        Log.d("D", "starting onStop");

        super.onStop();
    }
}
