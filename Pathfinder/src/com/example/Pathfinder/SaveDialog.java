package com.example.Pathfinder;

import android.app.DialogFragment;
import android.os.Bundle;
import android.app.Dialog;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.util.Log;
import android.view.LayoutInflater;
import android.widget.EditText;
import android.view.View;
import android.content.Context;
import android.widget.Toast;
import android.widget.Button;

import java.io.File;
import java.io.FileWriter;
import java.io.BufferedWriter;
import java.io.IOException;
import java.util.ArrayList;

/**
 * This class contains the custom dialog used to save a route.
 * Asks the user for a name for the Route.
 * Checks to make sure that name isn't being used for another route and that it is not an empty string.
 * Then saves the route as a new text file and stores information for the route in the index file.
 */
public class SaveDialog extends DialogFragment {

    private Context context; // collected from the Main class, needed for toast statements

    private Route route; // the route to be saved

    View layout; // the visual aspect of the screen

    EditText inp; // the input that the user enters text in

    /**
     * Constructor for SaveDialog that initializes the values of context and route.
     *
     * @param theContext    passed from the main class
     * @param theRoute      the route to be saved
     */
    public SaveDialog(Context theContext, Route theRoute) {
        context = theContext;
        route = theRoute;
    }

    /**
     * Called upon creation of the dialog.
     * Uses the corresponding custom dialog layout to display the dialog on the screen
     * Sets the positive and negative buttons of the dialog.
     *
     * @param savedInstanceState
     * @return  the dialog
     */
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity()); // creates the dialog

        LayoutInflater inflater = getActivity().getLayoutInflater();

        layout = inflater.inflate(R.layout.savedialog,null); // displays the dialog on the screen

        inp = (EditText) layout.findViewById(R.id.name_input); // initializes the inputText

        builder.setView(layout)

            // creates the positive and negative buttons
            .setPositiveButton(R.string.save_button, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int id) { }
            })
            .setNegativeButton(R.string.cancel_button, new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int id) { }
            });

        return builder.create();
    }

    /**
     * Used to override the button functions within OnCreate to customize when the dialog closes.
     * Contains the functions of the buttons.
     */
    @Override
    public void onStart()
    {
        super.onStart();    //super.onStart() is where dialog.show() is actually called on the underlying dialog, so we have to do it after this point
        AlertDialog d = (AlertDialog)getDialog();
        if(d != null)
        {
            // initializes both buttons
            Button positiveButton = (Button) d.getButton(Dialog.BUTTON_POSITIVE);
            Button negativeButton = (Button) d.getButton(Dialog.BUTTON_NEGATIVE);
            positiveButton.setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View v) // called when the "OK" button is pressed
                {
                    String temp = inp.getText().toString(); // collects the String in the input

                    Toast t1 = Toast.makeText(context, "you didn't enter anything", Toast.LENGTH_LONG);
                    Toast t2 = Toast.makeText(context, "you entered a name that already exists", Toast.LENGTH_LONG);

                    if (temp.equals("")) { // if user didn't enter anything
                        t1.show();
                    } else if (checkName(temp)) { // if user entered a name that is already in use
                        t2.show();
                        inp.setText("");
                    } else { // otherwise dismisses the dialog and saves the route with the given name
                        createRoute(temp);
                        dismiss();
                    }
                }
            });
            negativeButton.setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View v) // called when the "CANCEL" button is pressed
                {
                    Toast t2 = Toast.makeText(context, "you touched the cancel button", Toast.LENGTH_SHORT);
                    t2.show();

                    dismiss(); // dismisses the dialog
                }
            });
        }
    }

    /**
     * Sets the route name based on the input string.
     * And saves the route.
     *
     * @param name  the name of the route given by the user
     */
    public void createRoute(String name) {
        Toast t2 = Toast.makeText(context, "your route was saved as " + name, Toast.LENGTH_SHORT);
        t2.show();
        route.setName(name); // sets the name
        saveRoute(name); // saves the route to text file
    }

    /**
     * The method used to Write the route to text file in internal storage.
     * Also stores information including name and 1st coordiante in index file.
     *
     * @param routeName the name of the route
     */
    public void saveRoute(String routeName) {

        FileWriter fileWriter;

        File indexFile = new File(context.getExternalFilesDir(null), "index.txt");

        try { // writes the 1st coordinate, the distance, and the name of the route to index file
            fileWriter = new FileWriter(indexFile, true);
            BufferedWriter bufferedWriter = new BufferedWriter(fileWriter);

            bufferedWriter.write("" + route.getPoints().get(0).getLat());
            bufferedWriter.newLine();
            bufferedWriter.write("" + route.getPoints().get(0).getLon());
            bufferedWriter.newLine();
            bufferedWriter.write("" + route.getDistance());
            bufferedWriter.newLine();
            bufferedWriter.write(routeName);
            bufferedWriter.newLine();

            bufferedWriter.close();
        }   catch (IOException e) { }

        File f = new File(context.getExternalFilesDir(null), routeName + ".txt");

        ArrayList<WayPoint> points = route.getPoints(); // the temporary list of the coordinates in the route
        ArrayList<Double> times = route.getTimes(); // the temporary list of times for the route

        try { // actually writes the route to a text file
            fileWriter = new FileWriter(f, true);
            BufferedWriter bufferedWriter = new BufferedWriter(fileWriter);

            // writes the distance
            bufferedWriter.write("" + route.getDistance());
            bufferedWriter.newLine();

            // writes the coordinates
            for (WayPoint w : points) {
                bufferedWriter.write("" + w.getLat());
                bufferedWriter.newLine();
                bufferedWriter.write("" + w.getLon());
                bufferedWriter.newLine();
            }

            // indicates that remaining lines contain the times
            bufferedWriter.write("times");
            bufferedWriter.newLine();

            // writing out a bunch of times in milliseconds... below is each of the time
            for (double d : times) {
                bufferedWriter.write("" + d);
                bufferedWriter.newLine();
            }

            bufferedWriter.close();

        } catch (IOException e) {
            // Unable to create file, likely because external storage is
            // not currently mounted.
            Log.d("ExternalStorage", "Error writing " + f);
        }
    }

    /**
     * Checks whether the given name is already being used in the file base.
     *
     * @param name  the name in question
     * @return      True if the name has already been used; False if not
     */
    public boolean checkName(String name) {
        //  Issue on Intel phone... some strange issue on the Intel phone
        //  Something to do with where it is saving it per app????
        File[] files = new File("/storage/emulated/0/Android/data/com.example.Pathfinder/files").listFiles();

        if (files != null)
            for (File file : files) {
                String fileName = file.getName().substring(0, file.getName().length() - 4);
                if (fileName.equals(name))
                    return true;
            }

        return false;
    }
}

