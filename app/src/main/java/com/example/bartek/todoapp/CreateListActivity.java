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

public class CreateListActivity extends AppCompatActivity {
    private Button addItemButton;
    private Button createListButton;
    private TableLayout tableLayout;
    private EditText nameOfItemEditText;
    private DatabaseHelper database;
    private List<Item> items;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_list);
        nameOfItemEditText = findViewById(R.id.nameOfItem);
        tableLayout = findViewById(R.id.tableLayout);
        addItemButton = findViewById(R.id.addNewElementButton);
        createListButton = findViewById(R.id.createListButton);
        database = new DatabaseHelper(this);
        items = new ArrayList<>(5);

        addListeners();
    }

    private void addListeners() {
        addItemButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addTableRow();
            }
        });
        createListButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String nameOfList = ((EditText) findViewById(R.id.nameOfList)).getText().toString();

                if (database.createTodoList(nameOfList)) {
                    int idOfList = getIdOfList(nameOfList);
                    database.addItems(idOfList, items);
                } else {
                    Log.e("Database Error", "Error while creating todo list.");
                }
                finish();
            }

            private int getIdOfList(String nameOfList) {
                Cursor data = database.getIdOfList(nameOfList);
                data.moveToFirst();
                return data.getInt(0);
            }
        });
    }

    public void addTableRow() {
        final TableRow tableRow = new TableRow(this);
        tableRow.setGravity(Gravity.CENTER_HORIZONTAL);
        Item item = new Item(getNameOfItemFromEditText());
        items.add(item);
        tableRow.addView(getNameOfItemEditText());
        tableRow.addView(getDeleteItemImageButton(tableRow, item));

        tableLayout.addView(tableRow);
    }

    @NonNull
    private ImageButton getDeleteItemImageButton(final TableRow tableRow, final Item item) {
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

    @NonNull
    private EditText getNameOfItemEditText() {
        EditText nameOfItemEditText = new EditText(this);
        nameOfItemEditText.setText(getNameOfItemFromEditText());
        return nameOfItemEditText;
    }

    @NonNull
    private String getNameOfItemFromEditText() {
        return nameOfItemEditText.getText().toString();
    }
}
