package com.example.bartek.todoapp;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TableLayout;
import android.widget.TableRow;

import com.example.bartek.todoapp.database.DatabaseHelper;

import static android.view.Gravity.CENTER_HORIZONTAL;
import static com.example.bartek.todoapp.StringResources.NAME_OF_LIST_STRING;

public class MainActivity extends AppCompatActivity {
    private DatabaseHelper database;
    private TableLayout tableLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        database = new DatabaseHelper(this);
        tableLayout = findViewById(R.id.tableLayout);

        addListeners();
        displayLists();
    }

    public void onStart() {
        super.onStart();
        tableLayout.removeAllViews();
        displayLists();
    }

    public void addListeners() {
        Button button = findViewById(R.id.createNewListButton);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, CreateTodoListActivity.class);
                startActivity(intent);
            }
        });
    }

    private void displayLists() {
        Cursor data = database.getLists();
        while (data.moveToNext()) {
            TableRow tableRow = new TableRow(this);
            tableRow.setGravity(CENTER_HORIZONTAL);

            String listName = data.getString(0);
            Button button = createShowListButton(listName);

            tableRow.addView(button);
            tableLayout.addView(tableRow);
        }
    }

    @NonNull
    private Button createShowListButton(final String listName) {
        Button button = new Button(this);
        button.setText(listName);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, ShowTodoListActivity.class);
                intent.putExtra(NAME_OF_LIST_STRING, listName);
                startActivity(intent);
            }
        });
        return button;
    }
}
