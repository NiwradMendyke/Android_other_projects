package com.example.helloWorld;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;

public class MyActivity extends Activity {
    /**
     * Called when the activity is first created.
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

        final ImageButton button = (ImageButton) findViewById(R.id.button_id);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selfDestruct(v);
            }
        });
    }

    public void selfDestruct(View view) {
        android.os.Process.killProcess(android.os.Process.myPid());
    }
}
