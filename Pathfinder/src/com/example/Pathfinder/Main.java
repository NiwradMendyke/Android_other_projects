package com.example.Pathfinder;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.location.LocationManager;
import android.widget.TextView;
import android.content.Context;
import android.widget.Toast;

import java.io.*;
import java.util.ArrayList;
import java.util.Scanner;

/**
 * The main class of the application.
 * Creates the layout and controls the functions of each of the buttons.
 * Also contains the methods that check whether a route in question has already been previously saved.
 */
public class Main extends Activity implements View.OnClickListener {

    // the various buttons
    Button start, save, open, clear, delete, change;
    static TextView out;

    // the locationManager required for GPS functions in the route method
    static LocationManager manager;

    // boolean to keep track of whether the application is actively recording a route or not
    static boolean isRecording = false;

    // the route
    // kept undeclared until the start button is pressed
    Route route;


    // value that determines how far two coordinates are to be considered close
    static public Double lat_lonComparison = .002;
    static public Double distanceComparison = .7;

    /**
     * method that is called upon creation of the application.
     * @param savedInstanceState   passed variable
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main); // creates the main app layout

        // initializes the various buttons
        start = (Button) findViewById(R.id.button_start);
        save = (Button) findViewById(R.id.button_save);
        open = (Button) findViewById(R.id.button_open);
        clear = (Button) findViewById(R.id.button_clear);
        delete = (Button) findViewById(R.id.button_delete);
        change = (Button) findViewById(R.id.button_preferences);

        out = (TextView) findViewById(R.id.output); // initializes the text display area

        manager = (LocationManager) getSystemService(Context.LOCATION_SERVICE); // initializes the LocationManager

        // sets the listener for each of the buttons
        try {
            start.setOnClickListener(this);
            save.setOnClickListener(this);
            open.setOnClickListener(this);
            clear.setOnClickListener(this);
            delete.setOnClickListener(this);
            change.setOnClickListener(this);
        }   catch (Exception e) { }
    }

    /**
     * Method that is called when a button on the screen is pressed.
     * Cycles through the switch/case cascade to determine which button was selected.
     * Then calls the functions of that particular button.
     *
     * @param v   The layout of the main app screen
     */
    @Override
    public void onClick(View v) {

        switch (v.getId()) {

            //if press start
            case R.id.button_start:
                // checks if the app is already recording
                if (isRecording) {
                    saveRoute(route); // saves the route
                    start.setText("START"); // sets "STOP" back to "START"
                    out.setText(out.getText().toString().substring(0, out.getText().length() - 14)); // removes "recording..." from the screen
                    isRecording = false; // tells program app is no longer recording
                    break;
                }
                start.setText("STOP"); // sets "START" to "STOP"

                // adds "recording....." to the end of the text output
                if (out.getText().toString().equals(""))
                    out.setText("recording.....");
                else
                    out.setText(out.getText() + "\n\nrecording.....");

                route = new Route(this); // begins to record the route
                isRecording = true; // tells program that app is recording
                break;

            //if press save
            case R.id.button_save:
                // first checks if the app is recording
                if (isRecording) {
                    saveRoute(route); // saves the current route
                    route = new Route(this); // and starts recording a new route
                    break;
                }

                break;

            //if press open
            case R.id.button_open:
                // creates the opener dialog
                OpenDialog opener = new OpenDialog(this);
                opener.show(getFragmentManager(), "dialog");
                break;

            //if press clear
            case R.id.button_clear:
                // sets the display output to "" or "recording...." if the app is recording
                if (!out.getText().equals(""))
                    out.setText("");
                if (isRecording)
                    out.setText("recording.....");
                break;

            //if press delete
            case R.id.button_delete:
                // creates the deleter dialog
                DeleteDialog deleter = new DeleteDialog(this);
                deleter.show(getFragmentManager(), "dialog");
                break;

            //if press change
            case R.id.button_preferences:
                PreferencesDialog preferences = new PreferencesDialog(this);
                preferences.show(getFragmentManager(), "dialog");
                break;
        }
    }

    /**
     * Creates the saveDialog.
     * Checks if the passed route is similar to one that has already been saved.
     * If not, passes the route to SaveDialog
     *
     * @param route   The recently-recorded route that the app is about to save
     */
    public void saveRoute(Route route) {

        Log.d("Main", "" + route.getTimes().toString());

        route.setEndTime(); // sets a time for the route

        if (checkRoute(route)) { } // checks if already saved this route
        else { // if not, creates the saveDialog
            SaveDialog saver = new SaveDialog(this, route);
            saver.setCancelable(false);
            saver.show(getFragmentManager(), "dialog");
        }
    }

    /**
     * Method to check if the route is similar to one that has already been saved.
     * If yes, adds the time from current route to end of the similar route in the filebase.
     *
     * @param route   The route in question
     * @return        true if the route is similar to another; false if not
     */
    public boolean checkRoute(Route route) {

        // creates the index file
        File file = new File("/storage/emulated/0/Android/data/com.example.Pathfinder/files/index.txt");

        ArrayList<String> routesToCheck = new ArrayList<String>(); // arraylist of similar routes

        try { // reads through the index file
            InputStream in = new FileInputStream(file);

            double lat;
            double lon;
            double distance;
            String name;

            // Going through the index file reading it in
            //  Index file:
            //
            //     GPS lat1   GPS long1   distance_of_route1    name1
            //     GPS lat2   GPS long2   distance_of_route2    name2
            //
            //   \n's between each entry...

            if (in != null) {
                Scanner sc = new Scanner(file);

                InputStreamReader tmp = new InputStreamReader(in);
                BufferedReader reader = new BufferedReader(tmp);

                while (sc.hasNext()) { // collects the various values of each indexed route variable
                    lat = Double.parseDouble(reader.readLine());
                    lon = Double.parseDouble(reader.readLine());
                    distance = Double.parseDouble(reader.readLine());
                    name = reader.readLine();

                    Log.d("Main", "route distance = " + route.getDistance() + " and distance = " + distance);

                    // Check the rough distance of route and adds those that are likely
                    if ((route.getDistance() == 0.0) && (distance == 0.0))
                        routesToCheck.add(name);
                    if (!(route.getDistance() == 0.0) && !(distance == 0.0))
                        if (Math.abs(route.getPoints().get(0).getLat() - lat) < lat_lonComparison)
                            if (Math.abs(route.getPoints().get(0).getLon() - lon) < lat_lonComparison)
                                routesToCheck.add(name);
                }

                in.close();
            }
        }
        catch (Throwable t) { }

        // checking for a  "match"
        for (String s : routesToCheck) {
            Log.d("Main", s + " is one of the routes to check");
            Main.out.setText(s + " is one of the routes to check\n\n" + Main.out.getText());
        }

        // routesToCheck has only the decent matches
        for (String s : routesToCheck) { // opens and compares each route in the arraylist

            // creates the file of the similar route
            file = new File("/storage/emulated/0/Android/data/com.example.Pathfinder/files/" + s + ".txt");

            // creates a temporary route
            Route temporary;
            double distance = 0;
            ArrayList<WayPoint> pointsInRoute = new ArrayList<WayPoint>();
            ArrayList<Double> times = new ArrayList<Double>();

            Log.d("Main", "increments through the for loop");

            // reads through the file of the temporary route
            try {
                InputStream in = new FileInputStream(file);

                double lat;
                double lon;

                if (in != null) {
                    Scanner sc = new Scanner(file);

                    InputStreamReader tmp = new InputStreamReader(in);
                    BufferedReader reader = new BufferedReader(tmp);

                    ArrayList<String> inputs = new ArrayList<String>();

                    while (sc.hasNext())
                        inputs.add(sc.nextLine());

                    distance = Double.parseDouble(inputs.get(0));

                    int counter = 1;

                    while (!inputs.get(counter).equals("times")) {
                        lat = Double.parseDouble(inputs.get(counter));
                        lon = Double.parseDouble(inputs.get(counter + 1));

                        pointsInRoute.add(new WayPoint(lat, lon));

                        counter += 2;
                    }

                    counter++;

                    while (counter < inputs.size()) {
                        times.add(Double.parseDouble(inputs.get(counter)));
                        counter++;
                    }

                    in.close();
                }
            }
            catch (Throwable t) { }

            temporary = new Route(this, pointsInRoute, s, distance, times); // initializes the temporary route

            // then passes the temporary route and the actual route to the findRoute method
            if (findRoute(temporary, route)) {
                // if they are similar, tells this to the user
                Log.d("Main", "This route is similar to route " + s);
                Toast t1 = Toast.makeText(this, "This route is similar to " + s, Toast.LENGTH_LONG);
                out.setText(route.toString() + "\n\n" + out.getText());
                t1.show();

                try { // adds the new time to the file of the same route
                    Log.d("Main", "adding the time to existing file " + file);
                    BufferedWriter writer = new BufferedWriter(new FileWriter(file, true));
                    writer.write("" + route.getAvgTime());
                    writer.newLine();

                    writer.close();

                } catch (IOException e) {
                    // Unable to create file, likely because external storage is
                    // not currently mounted.
                    Log.d("ExternalStorage", "Error writing " + file);
                }
                return true;
            }
        }

        Log.d("Main", "returning false");
        return false;
    }

    /**
     * Method that given two routes, checks whether they are the same.
     *
     * @param route   The route that the user is trying to save
     * @param check   The temporary route that the program is comparing the actual route to
     * @return        True or false depending on the result of the comparison
     */
    public boolean findRoute(Route route, Route check) {

        // goes through way points to check for a match
        //  probably need a better matching algorithm

        // returns TRUE if it thinks it's the same.
        ArrayList<WayPoint> a = route.getPoints();
        ArrayList<WayPoint> b = check.getPoints();
        double distanceA = route.calcDistance(a.get(0), a.get(a.size() - 1));
        double distanceB = check.calcDistance(b.get(0), b.get(b.size() - 1));
        int hit = 0;
        if (Math.abs(distanceA - distanceB) < .5){
            for (int i = 0; i < a.size(); i++) {    // increments until reaches the end of one arraylist of Waypoints
                if (i >= b.size()) // or the other
                    break;
                if (Math.abs(a.get(i).getLat() - b.get(i).getLat()) < lat_lonComparison)
                    if (Math.abs(a.get(i).getLon() - b.get(i).getLon()) < lat_lonComparison)
                        hit++;
            }
            if (hit > 10 || hit > a.size() / 3) {
                Log.d("Main", "findRoute returns true");
                return true;
            }
        }

        Log.d("Main", "findRoute returns false");
        return false;
    }

}

