package com.example.Pathfinder;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

/**
 * Custom dialog that allows for changing of the values the program uses to compare two Waypoints.
 */
public class PreferencesDialog extends DialogFragment {

    private Context context;

    View layout;

    EditText inp1, inp2;

    /**
     * Short constructor to set the value of context
     *
     * @param theContext passed from the main class
     */
    public PreferencesDialog(Context theContext) {
        context = theContext;
    }

    /**
     * Creates and displays the dialog and the buttons.
     *
     * @param savedInstanceState
     * @return  the dialog
     */
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        LayoutInflater inflater = getActivity().getLayoutInflater();

        layout = inflater.inflate(R.layout.preferencesdialog,null);

        inp1 = (EditText) layout.findViewById(R.id.file_distance);
        inp2 = (EditText) layout.findViewById(R.id.lat_lon_comparison);

        inp1.setText(Main.distanceComparison.toString());
        inp2.setText(Main.lat_lonComparison.toString());

        builder.setView(layout)

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
     * Overrides the OnCreateDialog method to prevent dialog from closing unless proper value is in the inputTexts
     */
    @Override
    public void onStart()
    {
        super.onStart();    //super.onStart() is where dialog.show() is actually called on the underlying dialog, so we have to do it after this point
        AlertDialog d = (AlertDialog)getDialog();
        if(d != null)
        {
            Button positiveButton = (Button) d.getButton(Dialog.BUTTON_POSITIVE);
            Button negativeButton = (Button) d.getButton(Dialog.BUTTON_NEGATIVE);
            positiveButton.setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View v)
                {
                    if (!inp1.equals("")) {
                        Main.distanceComparison = Double.parseDouble(inp1.getText().toString());
                        Toast t1 = Toast.makeText(context, "you set distanceComparison value to " + Main.distanceComparison, Toast.LENGTH_LONG);
                        t1.show();
                    }

                    if (!inp2.equals("")) {
                        Main.lat_lonComparison = Double.parseDouble(inp2.getText().toString());
                        Toast t2 = Toast.makeText(context, "you set lat/lonComparison value to " + Main.lat_lonComparison, Toast.LENGTH_LONG);
                        t2.show();
                    }

                    dismiss();
                }
            });
            negativeButton.setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View v)
                {
                    Toast t2 = Toast.makeText(context, "you touched the cancel button", Toast.LENGTH_SHORT);
                    t2.show();

                    dismiss();
                }
            });
        }
    }
}
