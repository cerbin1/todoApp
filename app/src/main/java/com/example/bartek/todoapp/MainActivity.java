package com.example.bartek.todoapp;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TableLayout;
import android.widget.TableRow;

import com.example.bartek.todoapp.database.DatabaseHelper;

import static android.view.Gravity.CENTER_HORIZONTAL;

public class MainActivity extends AppCompatActivity {
    private DatabaseHelper database;
    private TableLayout tableLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        tableLayout = findViewById(R.id.tableLayout);
        database = new DatabaseHelper(this);

        addListeners();
        displayLists();
    }

    public void onStart() {
        super.onStart();
        tableLayout.removeAllViews();
        displayLists();
    }

    private void displayLists() {
        Cursor data = database.getLists();
        while (data.moveToNext()) {
            TableRow tableRow = new TableRow(this);
            tableRow.setGravity(CENTER_HORIZONTAL);

            Button button = new Button(this);
            button.setText(data.getString(0));

            tableRow.addView(button);
            tableLayout.addView(tableRow);
        }
    }

    public void addListeners() {
        Button button = (Button) findViewById(R.id.createNewListButton);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, CreateListActivity.class);
                startActivity(intent);
            }
        });
    }
}
