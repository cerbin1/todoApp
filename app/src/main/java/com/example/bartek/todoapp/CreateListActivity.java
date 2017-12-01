package com.example.bartek.todoapp;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TableLayout;
import android.widget.TableRow;

public class CreateListActivity extends AppCompatActivity {
    private Button addItemButton;
    private TableLayout tableLayout;
    private EditText nameOfItemEditText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_list);
        nameOfItemEditText = findViewById(R.id.nameOfItem);
        tableLayout = findViewById(R.id.tableLayout);
        addItemButton = findViewById(R.id.addNewElementButton);

        addListeners();
    }

    private void addListeners() {
        addItemButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addItem();
            }
        });

    }

    public void addItem() {
        final TableRow tableRow = new TableRow(this);
        tableRow.setGravity(Gravity.CENTER_HORIZONTAL);

        EditText nameOfItem = getNameOfItemEditText();
        tableRow.addView(nameOfItem);

        ImageButton deleteItem = getDeleteItemImageButton(tableRow);
        tableRow.addView(deleteItem);

        tableLayout.addView(tableRow);
    }

    @NonNull
    private ImageButton getDeleteItemImageButton(final TableRow tableRow) {
        ImageButton deleteItem = new ImageButton(this);
        deleteItem.setBackgroundResource(R.drawable.ic_delete);
        deleteItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                tableLayout.removeView(tableRow);
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
