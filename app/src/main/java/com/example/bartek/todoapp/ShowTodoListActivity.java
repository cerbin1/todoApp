package com.example.bartek.todoapp;

import android.content.Intent;
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
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.bartek.todoapp.database.DatabaseHelper;
import com.example.bartek.todoapp.database.UnexpectedDataException;

import java.util.ArrayList;
import java.util.List;

import static com.example.bartek.todoapp.Item.INITIAL_ITEMS_AMOUNT;
import static com.example.bartek.todoapp.StringResources.ID_OF_LIST;

public class ShowTodoListActivity extends AppCompatActivity {
    private DatabaseHelper database;
    private final List<Item> listItems = new ArrayList<>(INITIAL_ITEMS_AMOUNT);
    private int listId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_todo_list);
        database = new DatabaseHelper(this);

        listId = getIntent().getIntExtra(ID_OF_LIST, -1);
        getListItems();
        displayListElements();
    }

    private void getListItems() {
        Cursor data = database.getItems(listId);
        while (data.moveToNext()) {
            String name = data.getString(0);
            int id = data.getInt(1);
            boolean checked;
            if (isItemUnchecked(data)) {
                checked = false;
            } else if (isItemChecked(data)) {
                checked = true;
            } else {
                throw new UnexpectedDataException("Wrong data assigned to SQL boolean, may be 1 or 0 only!");
            }
            listItems.add(new Item(name, checked, id));
        }
    }

    private boolean isItemChecked(Cursor data) {
        return data.getInt(2) == 1;
    }

    private boolean isItemUnchecked(Cursor data) {
        return data.getInt(2) == 0;
    }

    private void displayListElements() {
        LinearLayout container = findViewById(R.id.linearLayout);

        LinearLayout headingLayout = createLinearLayoutForNameOfListAndEditButton();
        headingLayout.addView(createNameOfListTextView());
        headingLayout.addView(createEditListButton());

        container.addView(headingLayout);
        for (Item item : listItems) {
            TextView itemName = createItemNameTextView(item.getName());

            LinearLayout newEntryLayout = new LinearLayout(this);
            newEntryLayout.addView(itemName, getLayoutParamsForItem());
            newEntryLayout.addView(createItemButton(itemName, item));
            container.addView(newEntryLayout);
        }
    }

    @NonNull
    private LinearLayout createLinearLayoutForNameOfListAndEditButton() {
        LinearLayout linearLayout = new LinearLayout(this);
        linearLayout.setGravity(Gravity.CENTER);
        return linearLayout;
    }

    private String getNameOfList() {
        Cursor data = database.getNameOfList(listId);
        if (data.moveToNext()) {
            return data.getString(0);
        } else {
            throw new UnexpectedDataException("Not found name of list with given id!");
        }
    }

    private ImageButton createEditListButton() {
        ImageButton editListButton = new ImageButton(this);
        editListButton.setBackgroundResource(R.drawable.ic_edit);
        editListButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startShowTodoListActivity();
            }

            private void startShowTodoListActivity() {
                Intent intent = new Intent(ShowTodoListActivity.this, EditTodoListActivity.class);
                intent.putExtra(ID_OF_LIST, listId);
                startActivity(intent);
            }
        });
        return editListButton;
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
                    if (database.changeStatusOfItem(item.getId(), 0)) {
                        setUnchecked(itemName);
                    } else {
                        Log.e("Database Error", "Error while updating items.");
                    }
                } else {
                    if (database.changeStatusOfItem(item.getId(), 1)) {
                        setChecked(itemName);
                    } else {
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

    public TextView createNameOfListTextView() {
        TextView textView = new TextView(this);
        textView.setTextSize(25);
        textView.setText(getNameOfList());
        return textView;
    }
}
