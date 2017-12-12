package com.example.bartek.todoapp;

import android.database.Cursor;
import android.graphics.Paint;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

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
        LinearLayout container = findViewById(R.id.linearLayout);
        while (data.moveToNext()) {
            final TextView itemName = new TextView(this);
            itemName.setText(data.getString(0));
            itemName.setTextSize(20);
            Button button = new Button(this);
            button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    itemName.setPaintFlags(itemName.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
                }
            });
            button.setText("Done");
            LinearLayout newEntryLayout = new LinearLayout(this);
            newEntryLayout.addView(itemName, getLayoutParamsForItem());
            newEntryLayout.addView(button);
            container.addView(newEntryLayout);
        }
    }

    @NonNull
    private LinearLayout.LayoutParams getLayoutParamsForItem() {
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        params.weight = 1.0f;
        params.gravity = Gravity.START;
        return params;
    }
}
