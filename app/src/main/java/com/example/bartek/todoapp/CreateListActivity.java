package com.example.bartek.todoapp;

import android.database.Cursor;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TableLayout;
import android.widget.TableRow;

import com.example.bartek.todoapp.database.DatabaseHelper;

import java.util.ArrayList;
import java.util.List;

import static android.widget.Toast.LENGTH_SHORT;
import static android.widget.Toast.makeText;
import static com.example.bartek.todoapp.Item.INITIAL_ITEMS_AMOUNT;

public class CreateListActivity extends AppCompatActivity {
    private TableLayout tableLayout;
    private Button addItemButton;
    private Button createListButton;
    private EditText nameOfItemEditText;

    private DatabaseHelper database;
    private final List<Item> items = new ArrayList<>(INITIAL_ITEMS_AMOUNT);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_list);

        tableLayout = findViewById(R.id.tableLayout);
        addItemButton = findViewById(R.id.addNewElementButton);
        createListButton = findViewById(R.id.createListButton);
        nameOfItemEditText = findViewById(R.id.nameOfItem);

        database = new DatabaseHelper(this);

        addListeners();
    }

    private void addListeners() {
        addItemButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                appendTableRow();
            }
        });
        createListButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                createNewList();
            }

            private void createNewList() {
                String nameOfList = ((EditText) findViewById(R.id.nameOfList)).getText().toString();

                if (nameOfList.equals("")) {
                    displayToastWithText("Empty name of list!");
                } else if (nameOfList.length() > 30) {
                    displayToastWithText("Name can have 30 characters max!");
                } else if (items.isEmpty()) {
                    displayToastWithText("Add some items!");
                } else if (isAnyEmptyItemInList()) {
                    displayToastWithText("Some items have no name!");
                } else {
                    if (database.createList(nameOfList)) {
                        int idOfList = getIdOfCreatedList();
                        database.addItemsToList(idOfList, items);
                    } else {
                        Log.e("Database Error", "Error while creating todo list.");
                    }
                    finish();
                }
            }

            private int getIdOfCreatedList() {
                Cursor idOfList = database.getIdOfLastCreatedList();
                idOfList.moveToFirst();
                return idOfList.getInt(0);
            }
        });
    }

    public void appendTableRow() {
        final TableRow tableRow = createTableRow();
        addListItemTo(tableRow);

        tableLayout.addView(tableRow);
    }

    @NonNull
    private TableRow createTableRow() {
        final TableRow tableRow = new TableRow(this);
        tableRow.setGravity(Gravity.CENTER_HORIZONTAL);
        return tableRow;
    }

    private void addListItemTo(TableRow tableRow) {
        Item item = new Item(getNameOfItem(), false);
        items.add(item);
        tableRow.addView(createNameOfItemEditText());
        tableRow.addView(createDeleteItemImageButton(tableRow, item));
    }

    @NonNull
    private String getNameOfItem() {
        return nameOfItemEditText.getText().toString();
    }

    @NonNull
    private EditText createNameOfItemEditText() {
        EditText nameOfItemEditText = new EditText(this);
        nameOfItemEditText.setText(getNameOfItem());
        return nameOfItemEditText;
    }

    @NonNull
    private ImageButton createDeleteItemImageButton(final TableRow tableRow, final Item item) {
        ImageButton deleteItem = new ImageButton(this);
        deleteItem.setBackgroundResource(R.drawable.ic_delete);
        deleteItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                tableLayout.removeView(tableRow);
                items.remove(item);
            }
        });
        return deleteItem;
    }

    public boolean isAnyEmptyItemInList() {
        for (Item item : items) {
            if (item.isEmpty()) {
                return true;
            }
        }
        return false;
    }

    private void displayToastWithText(String text) {
        makeText(CreateListActivity.this, text, LENGTH_SHORT).show();
    }
}
