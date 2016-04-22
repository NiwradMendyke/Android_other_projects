package com.example.Pathfinder;

import android.app.DialogFragment;
import android.content.Context;
import android.os.Bundle;
import android.app.Dialog;
import android.app.AlertDialog;
import android.content.DialogInterface;

import java.io.*;

import android.util.Log;
import android.widget.Toast;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Scanner;

/**
 * This class contains the list dialog which allows the user to select a saved route to display to the screen.
 * Uses a list dialog.
 * Takes the selected route, opens it from text file.
 * Then outputs it on the screen.
 */
public class OpenDialog extends DialogFragment {

    private Context context;

    private ArrayList<String> routeNames = new ArrayList<String>(); // the list of routes, used for the list dialog

    /**
     * Short constructor to set the value of context
     *
     * @param theContext passed from the main class
     */
    public OpenDialog(Context theContext) {
        context = theContext;
    }

    /**
     * Called upon creation of the OpenDialog.
     * Gets the names of the files saved, and uses it to display the dialog.
     * Creates the positive and negative buttons.
     *
     * @param savedInstanceState
     * @return  the dialog
     */
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        getNames(); // fills routeNames

        String[] names = new String[routeNames.size()]; // creates an array from the arraylist, need array for the dialog

        for (int i = 0; i < routeNames.size(); i++)
            names[i] = routeNames.get(i);

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        // builds the list dialog
        builder.setTitle("Choose a Route")
                .setItems(names, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {

                        Route route = getRoute(which); // gets the route based on the value of which

                        // outputs selected route to the screen
                        if (!Main.out.getText().equals(""))
                            Main.out.setText(route.toString() + "\n\n" + Main.out.getText());
                        else
                            Main.out.setText(route.toString());
                    } });

        return builder.create();
    }

    /**
     * Accesses the files for the app.
     * Records the list of file names except for the index file.
     */
    public void getNames() {
        File[] files = new File("/storage/emulated/0/Android/data/com.example.Pathfinder/files").listFiles();

        if (files != null)
            for (File file : files) {
                String fileName = file.getName().substring(0, file.getName().length() - 4);
                if (!fileName.equals("index"))
                    routeNames.add(fileName);
            }

        java.util.Collections.sort(routeNames, String.CASE_INSENSITIVE_ORDER); // sorts the list of names in alphabetical order
    }

    /**
     * Takes the corresponding route name for the selected index int.
     * Uses that route name to read that route from the file storage.
     * Creates a temp. route which it returns.
     *
     * @param index   the int representing index of the chosen route
     * @return        the route to be displayed
     */
    public Route getRoute(int index) {
        String name = routeNames.get(index);
        ArrayList<WayPoint> pointsInRoute = new ArrayList<WayPoint>();
        ArrayList<Double> times = new ArrayList<Double>();

        double distance = 0;

        File file = new File("/storage/emulated/0/Android/data/com.example.Pathfinder/files/" + name + ".txt");

        try { // reads the file for route information
            InputStream in = new FileInputStream(file);

            // holder variables for the lat and lon of each waypoint in the route
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
        catch (Throwable t) {
            Toast
                    .makeText(context, "Exception: " + t.toString(), Toast.LENGTH_LONG)
                    .show();
        }

        Log.d("Opener", "times: " + times.toString());

        return new Route(context, pointsInRoute, name, distance, times); // returns the temporary route
    }
}

