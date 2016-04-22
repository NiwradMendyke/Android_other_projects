package com.example.Pathfinder;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.provider.MediaStore.Files;
import android.content.DialogInterface;
import android.app.DialogFragment;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import java.io.*;
import java.util.ArrayList;
import java.util.Scanner;

/**
 * The class that contains the persistent list dialog for user to delete saved routes.
 * Deletes both the text file of the route and the route information from the index file.
 */
public class DeleteDialog extends DialogFragment {

    private Context context; // used for toast statements

    private ArrayList<String> routeNames = new ArrayList<String>(); // list of all the route names
    private ArrayList<Integer> selected; // list of integers representing the indexes of the selected routes

    /**
     * Short constructor to set the value of context
     *
     * @param theContext passed from the main class
     */
    public DeleteDialog(Context theContext) {
        context = theContext;
    }

    /**
     * Called upon creation of the dialog.
     * Takes the list of names then creates the dialog and the buttons.
     *
     * @param savedInstanceState
     * @return  the dialog
     */
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        getNames(); // fills the arraylist of route names

        // creates an array of names from the routeNames arraylist
        String[] names = new String[routeNames.size()];

        selected = new ArrayList<Integer>();

        for (int i = 0; i < routeNames.size(); i++)
            names[i] = routeNames.get(i);

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity()); // creates the dialogBuilder

        builder.setTitle(R.string.which_to_delete)
                // Specify the list array, the items to be selected by default (null for none),
                // and the listener through which to receive callbacks when items are selected
                .setMultiChoiceItems(names, null,
                        new DialogInterface.OnMultiChoiceClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which,
                                                boolean isChecked) {
                                if (isChecked) { // if/else cascade to select/deselect choices in the menu
                                    // If the user checked the item, add it to the selected items
                                    selected.add(which);
                                } else if (selected.contains(which)) {
                                    // Else, if the item is already in the array, remove it
                                    selected.remove(Integer.valueOf(which));
                                }
                            }
                        })
                        // Set the action buttons
                .setPositiveButton(R.string.delete_button, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {
                        // User clicked OK, so save the mSelectedItems results somewhere
                        // or return them to the component that opened the dialog
                        delete(selected); // deletes the selected routes
                    }
                })
                .setNegativeButton(R.string.cancel_button, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) { // does nothing
                    }
                });

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

        java.util.Collections.sort(routeNames, String.CASE_INSENSITIVE_ORDER); // sorts the names in alphabetical order
    }

    /**
     * The method that does the deleting of the route text file and the route info in the index file.
     *
     * @param selected  the arraylist of integers corresponding to the routes to be deleted
     */
    public void delete(ArrayList<Integer> selected) {
        File file1 = new File("/storage/emulated/0/Android/data/com.example.Pathfinder/files/index.txt");
        File file3;
        // temporary arraylist variables to contain the information from the file
        ArrayList<String> inputs = new ArrayList<String>();
        ArrayList<String> outputs = new ArrayList<String>();

        try { // takes text from the index file, and overwrites the index file while omitting the deleted route(s)
            InputStream in = new FileInputStream(file1);

            if (in != null) {
                Scanner sc = new Scanner(file1);

                InputStreamReader tmp = new InputStreamReader(in);
                BufferedReader reader = new BufferedReader(tmp);

                while (sc.hasNext())
                    inputs.add(sc.nextLine());

                for (int a = 0; a < inputs.size(); a += 2) {
                    boolean include = true;

                    for (int b : selected) // checks if the recorded name matches the selected routes
                        if (inputs.get(a + 1).equals(routeNames.get(b)))
                            include = false;

                    if (include) { // if not, adds the line to the output
                        outputs.add(inputs.get(a));
                        outputs.add(inputs.get(a + 1));
                    }
                }

                in.close();
            }
        } catch (Throwable t) {
            Toast
                    .makeText(context, "Exception: " + t.toString(), Toast.LENGTH_LONG)
                    .show();
        }

        boolean didDeleteFile1 = file1.delete(); // deletes the file

        if (didDeleteFile1) { // creates a new index file with the output
            File file2 = new File(context.getExternalFilesDir(null), "index.txt");

            FileWriter fileWriter;

            try { // writes to the new file
                fileWriter = new FileWriter(file2, true);
                BufferedWriter bufferedWriter = new BufferedWriter(fileWriter);

                for (String s : outputs) {
                    bufferedWriter.write(s);
                    bufferedWriter.newLine();
                }

                bufferedWriter.close();
            } catch (Throwable t) {
                Toast
                        .makeText(context, "Exception: " + t.toString(), Toast.LENGTH_LONG)
                        .show();
            }

            for (Integer i : selected) { // deletes the actual file for the selected routes
                file3 = new File("/storage/emulated/0/Android/data/com.example.Pathfinder/files/" + routeNames.get(i) + ".txt");

                //Log.d("deleter", "deleting file " + file3);

                file3.delete();
            }
        }
    }
}
