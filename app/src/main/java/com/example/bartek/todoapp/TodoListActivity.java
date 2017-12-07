package com.example.bartek.todoapp;

import android.database.Cursor;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.Button;
import android.widget.LinearLayout;

import com.example.bartek.todoapp.database.DatabaseHelper;

public class TodoListActivity extends AppCompatActivity {
    private DatabaseHelper database;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_todo_list);
        database = new DatabaseHelper(this);

        displayListElements();
    }

    private void displayListElements() {
        String nameOfList = getIntent().getStringExtra("nameOfList");
        Cursor data = database.getItems(nameOfList);
        LinearLayout linearLayout = findViewById(R.id.linearLayout);
        while (data.moveToNext()) {
            Button button = new Button(this);
            button.setText(data.getString(0));
            linearLayout.addView(button);
        }
    }
}
