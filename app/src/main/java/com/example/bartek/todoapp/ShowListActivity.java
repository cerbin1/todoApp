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

import static com.example.bartek.todoapp.StringResources.ID_OF_LIST;

public class ShowListActivity extends AppCompatActivity {
    private DatabaseHelper database;
    private TodoList todoList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_list);
        database = new DatabaseHelper(this);
        int listIdFromExtra = getIntent().getIntExtra(ID_OF_LIST, -1);
        todoList = new TodoList(database, listIdFromExtra);
        displayListElements();
    }

    private void displayListElements() {
        final LinearLayout container = findViewById(R.id.linearLayout);

        LinearLayout headingLayout = createLinearLayoutForNameOfListAndEditButton();
        headingLayout.addView(createNameOfListTextView());
        headingLayout.addView(createEditListButton());

        container.addView(headingLayout);
        for (Item item : todoList.getItems()) {
            TextView itemName = createItemNameTextView(item.getName());

            LinearLayout newEntryLayout = new LinearLayout(this);
            newEntryLayout.addView(itemName, getLayoutParamsForItem());
            newEntryLayout.addView(createItemButton(itemName, item));
            container.addView(newEntryLayout);
        }

        container.addView(createResetCheckedItemsButton(container));
    }

    @NonNull
    private LinearLayout createLinearLayoutForNameOfListAndEditButton() {
        LinearLayout linearLayout = new LinearLayout(this);
        linearLayout.setGravity(Gravity.CENTER);
        return linearLayout;
    }

    public TextView createNameOfListTextView() {
        TextView textView = new TextView(this);
        textView.setTextSize(25);
        textView.setText(getNameOfList());
        return textView;
    }

    private String getNameOfList() {
        Cursor data = database.getNameOfList(todoList.getId());
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
                startShowListActivity();
            }

            private void startShowListActivity() {
                Intent intent = new Intent(ShowListActivity.this, EditListActivity.class);
                intent.putExtra(ID_OF_LIST, todoList.getId());
                startActivity(intent);
            }
        });
        return editListButton;
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

    @NonNull
    private Button createItemButton(final TextView itemName, final Item item) {
        Button itemButton = new Button(this);
        if (item.isChecked()) {
            setChecked(itemName);
        }
        itemButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (changedStatusOfItemInDatabase()) {
                    if (item.isChecked()) {
                        setUnchecked(itemName);
                    } else {
                        setChecked(itemName);
                    }
                } else {
                    Log.e("Database Error", "Error while updating items.");
                }
            }

            private boolean changedStatusOfItemInDatabase() {
                return database.changeStatusOfItem(item.getId(), item.isChecked() ? 0 : 1);
            }
        });

        itemButton.setText(R.string.done);
        return itemButton;
    }

    @NonNull
    private Button createResetCheckedItemsButton(final LinearLayout container) {
        Button resetCheckedItemsButton = new Button(this);
        resetCheckedItemsButton.setText("Reset checked items");
        resetCheckedItemsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                uncheckAllItems();
            }

            private void uncheckAllItems() {
                for (int i = 1; i < container.getChildCount() - 1; i++) {
                    LinearLayout nextItemLayout = (LinearLayout) container.getChildAt(i);
                    TextView itemName = (TextView) nextItemLayout.getChildAt(0);
                    setUnchecked(itemName);
                }
                database.uncheckAllItems(todoList.getId());
            }
        });
        return resetCheckedItemsButton;
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
}
