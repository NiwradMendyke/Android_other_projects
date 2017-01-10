package com.codepath.simpletodo;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.EditText;

public class AddTaskActivity extends AppCompatActivity {

    EditText etEdit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_task);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        etEdit = (EditText) findViewById(R.id.etEdit);
    }

    public void cancel(View view) {
        Intent intent = new Intent();
        setResult(2, intent);
        finish();
    }

    public void addNew(View view) {
        Intent intent = new Intent();
        intent.putExtra("taskName", etEdit.getText().toString());
        setResult(1, intent);
        finish();
        etEdit.setText("");
    }
}
