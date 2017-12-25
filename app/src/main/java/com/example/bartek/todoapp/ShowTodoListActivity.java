package com.example.bartek.todoapp;

import android.database.Cursor;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.bartek.todoapp.database.DatabaseHelper;
import com.example.bartek.todoapp.database.UnexpectedDataException;

import java.util.ArrayList;
import java.util.List;

import static com.example.bartek.todoapp.StringResources.NAME_OF_LIST_STRING;

public class ShowTodoListActivity extends AppCompatActivity {
    private DatabaseHelper database;
    private final List<Item> listItems = new ArrayList<>(5);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_todo_list);
        database = new DatabaseHelper(this);
        getListItems();
        displayListElements();
    }

    private void getListItems() {
        String nameOfList = getIntent().getStringExtra(NAME_OF_LIST_STRING);
        Cursor data = database.getItems(nameOfList);
        while (data.moveToNext()) {
            String name = data.getString(0);
            int id = data.getInt(1);
            boolean checked;
            if (data.getInt(2) == 0) {
                checked = false;
            } else if (data.getInt(2) == 1) {
                checked = true;
            } else {
                throw new UnexpectedDataException("Wrong data assigned to SQL boolean, may be 1 or 0 only!");
            }
            listItems.add(new Item(name, id, checked));
        }
    }

    private void displayListElements() {
        LinearLayout container = findViewById(R.id.linearLayout);
        for (Item item : listItems) {
            TextView itemName = createItemNameTextView(item.getName());

            LinearLayout newEntryLayout = new LinearLayout(this);
            newEntryLayout.addView(itemName, getLayoutParamsForItem());
            newEntryLayout.addView(createItemButton(itemName, item));
            container.addView(newEntryLayout);
        }
    }

    @NonNull
    private Button createItemButton(final TextView itemName, final Item item) {
        Button itemButton = new Button(this);
        if (item.isChecked()) {
            setChecked(itemName);
        }
        itemButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isChecked(itemName)) {
                    setUnchecked(itemName);
                    if (!database.changeStatusOfItem(item.getId(), 0)) {
                        Log.e("Database Error", "Error while updating items.");
                    }
                } else {
                    setChecked(itemName);
                    if (!database.changeStatusOfItem(item.getId(), 1)) {
                        Log.e("Database Error", "Error while updating items.");
                    }
                }
            }
        });

        itemButton.setText(R.string.done);
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
