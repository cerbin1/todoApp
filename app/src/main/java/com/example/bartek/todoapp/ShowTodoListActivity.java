package com.example.bartek.todoapp;

import android.database.Cursor;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.bartek.todoapp.database.DatabaseHelper;

import static com.example.bartek.todoapp.StringResources.NAME_OF_LIST_STRING;

public class ShowTodoListActivity extends AppCompatActivity {
    private DatabaseHelper database;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_todo_list);
        database = new DatabaseHelper(this);

        displayListElements();
    }

    private void displayListElements() {
        String nameOfList = getIntent().getStringExtra(NAME_OF_LIST_STRING);
        Cursor data = database.getItems(nameOfList);
        LinearLayout container = findViewById(R.id.linearLayout);
        while (data.moveToNext()) {
            TextView itemName = createItemNameTextView(data.getString(0));

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
                if (isChecked(itemName)) {
                    setUnchecked(itemName);
                } else {
                    setChecked(itemName);
                }
            }
        });

        itemButton.setText("Done");
        return itemButton;
    }

    private void setChecked(TextView itemName) {
        itemName.setPaintFlags(itemName.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
        itemName.setTypeface(Typeface.DEFAULT);
        itemName.setTextColor(Color.GRAY);
    }

    private void setUnchecked(TextView itemName) {
        itemName.setPaintFlags(itemName.getPaintFlags() & (~Paint.STRIKE_THRU_TEXT_FLAG));
        itemName.setTypeface(null, Typeface.BOLD);
        itemName.setTextColor(Color.RED);
    }

    private boolean isChecked(TextView itemName) {
        return itemName.getCurrentTextColor() == Color.GRAY;
    }

    public TextView createItemNameTextView(String itemName) {
        TextView itemNameTextView = new TextView(this);
        itemNameTextView.setGravity(Gravity.CENTER);
        itemNameTextView.setText(itemName);
        itemNameTextView.setTextSize(25);
        setUnchecked(itemNameTextView);
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
