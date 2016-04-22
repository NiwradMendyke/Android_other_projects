package com.example.GPS_Demo;

import android.app.Activity;
import android.os.Bundle;
import android.location.LocationManager;
import android.content.Context;
import android.content.Intent;
import android.provider.Settings;
import android.location.LocationListener;
import android.location.Location;
import android.location.Criteria;
import android.util.Log;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;
import android.view.View;

public class GPS_App extends Activity implements LocationListener {

    TextView text;
    ImageButton button;

    static LocationManager locationManager;
    private String provider;

    /**
     * Called when the activity is first created.
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.main);

        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

        Criteria criteria = new Criteria();
        provider = locationManager.getBestProvider(criteria, false);
        final Location location = locationManager.getLastKnownLocation(provider);

        Log.d("gpsApp", "location is " + location.toString());

        // Initialize the location fields
        if (location != null) {
            System.out.println("Provider " + provider + " has been selected.");
        }
        else {
            text.setText("Location not available");
        }

        text = (TextView) findViewById(R.id.text);
        //lo = (TextView) findViewById(R.id.longitude);

        button = (ImageButton) findViewById(R.id.the_button);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onLocationChanged(location);
            }
        });
     }

    // Define the callback method that receives location updates
    @Override
    public void onLocationChanged(Location location) {

        int latitude = (int)location.getLatitude();
        int longitude = (int)location.getLongitude();

        if (((Integer)latitude != null) && ((Integer)longitude != null)) {
            text.setText("Your current location is: " + latitude + ", " + longitude);
        }
    }

    @Override
    public void onProviderEnabled(String provider) {
        Toast.makeText(this, "Enabled new provider " + provider,
                Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onProviderDisabled(String provider) {
        Toast.makeText(this, "Disabled provider " + provider,
                Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) { }

    /* Request updates at startup */
    @Override
    protected void onResume() {
        super.onResume();
        locationManager.requestLocationUpdates(provider, 400, 1, this);
    }

    /* Remove the locationlistener updates when Activity is paused */
    @Override
    protected void onPause() {
        super.onPause();
        locationManager.removeUpdates(this);
    }

}
