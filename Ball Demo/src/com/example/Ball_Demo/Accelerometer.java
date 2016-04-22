package com.example.Ball_Demo;

import android.app.Activity;
import android.hardware.SensorEventListener;
import android.hardware.SensorEvent;
import android.hardware.SensorManager;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.content.Context;
import android.util.Log;

/**
 * Created by darwinmendyke on 7/14/14.
 */
public class Accelerometer extends Activity implements SensorEventListener {

    private SensorManager senSensorManager;
    private Sensor senAccelerometer;
    private long lastUpdate = 0;
    private float last_x, last_y, last_z;
    private static final int SHAKE_THRESHOLD = 600;

    public static int x, y = 0;

    public Accelerometer(Context context) {

        senSensorManager = (SensorManager) context.getSystemService(Context.SENSOR_SERVICE);
        senAccelerometer = senSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        senSensorManager.registerListener(this, senAccelerometer , SensorManager.SENSOR_DELAY_NORMAL);
    }

    @Override
    public void onSensorChanged(SensorEvent event) {

        if (event.sensor.getType() == Sensor.TYPE_ACCELEROMETER) {

            int someNumber = 100;
            float xChange = someNumber * event.values[1];
            //values[2] can be -90 to 90
            float yChange = someNumber * 2 * event.values[0];

            if (Math.abs(xChange - x) >= 100) {
                x = (int)xChange;
                //Log.d("Accelerometer", "value of x = " + x);
            }

            if (Math.abs(yChange - y) >= 100) {
                y = (int)yChange;
                //Log.d("Accelerometer", "value of y = " + y);
            }

            BouncyBall.isBouncing = true;
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }

    protected void onPause() {
        super.onPause();
        senSensorManager.unregisterListener(this);
    }

    protected void onResume() {
        super.onResume();
        senSensorManager.registerListener(this, senAccelerometer, SensorManager.SENSOR_DELAY_NORMAL);
    }


}
