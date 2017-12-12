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
            final TextView itemName = createItemNameTextView(data.getString(0));
            LinearLayout newEntryLayout = new LinearLayout(this);
            newEntryLayout.addView(itemName, getLayoutParamsForItem());
            newEntryLayout.addView(createItemButton(itemName));
            container.addView(newEntryLayout);
        }
    }

    @NonNull
    private Button createItemButton(final TextView itemName) {
        Button itemButton = new Button(this);
        itemButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                itemName.setPaintFlags(itemName.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
            }
        });

        itemButton.setText("Done");
        return itemButton;
    }

    public TextView createItemNameTextView(String itemName) {
        TextView itemNameTextView = new TextView(this);
        itemNameTextView.setText(itemName);
        itemNameTextView.setTextSize(20);
        return itemNameTextView;
    }

    @NonNull
    private LinearLayout.LayoutParams getLayoutParamsForItem() {
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT);
        params.weight = 1.0f;
        params.gravity = Gravity.START;
        return params;
    }
}
