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
    private TableLayout tableLayout;
    private Button addItemButton;
    private Button createListButton;
    private EditText nameOfItemEditText;

    private DatabaseHelper database;
    private List<Item> listItems;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_list);

        tableLayout = findViewById(R.id.tableLayout);
        addItemButton = findViewById(R.id.addNewElementButton);
        createListButton = findViewById(R.id.createListButton);
        nameOfItemEditText = findViewById(R.id.nameOfItem);

        database = new DatabaseHelper(this);
        listItems = new ArrayList<>(5);

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
                    database.addItemsToList(idOfList, listItems);
                } else {
                    Log.e("Database Error", "Error while creating todo list.");
                }
                finish();
            }

            private int getIdOfList(String nameOfList) {
                Cursor idOfList = database.getIdOfList(nameOfList);
                idOfList.moveToFirst();
                return idOfList.getInt(0);
            }
        });
    }

    public void addTableRow() {
        final TableRow tableRow = new TableRow(this);
        tableRow.setGravity(Gravity.CENTER_HORIZONTAL);
        Item item = new Item(getNameOfItemFromEditText());
        listItems.add(item);
        tableRow.addView(createNameOfItemEditText());
        tableRow.addView(createDeleteItemImageButton(tableRow, item));

        tableLayout.addView(tableRow);
    }

    @NonNull
    private ImageButton createDeleteItemImageButton(final TableRow tableRow, final Item item) {
        ImageButton deleteItem = new ImageButton(this);
        deleteItem.setBackgroundResource(R.drawable.ic_delete);
        deleteItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                tableLayout.removeView(tableRow);
                listItems.remove(item);
            }
        });
        return deleteItem;
    }

    @NonNull
    private EditText createNameOfItemEditText() {
        EditText nameOfItemEditText = new EditText(this);
        nameOfItemEditText.setText(getNameOfItemFromEditText());
        return nameOfItemEditText;
    }

    @NonNull
    private String getNameOfItemFromEditText() {
        return nameOfItemEditText.getText().toString();
    }
}
