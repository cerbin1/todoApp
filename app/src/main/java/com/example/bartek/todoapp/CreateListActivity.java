package com.example.bartek.todoapp;

import android.os.Bundle;
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

        addListeners();
    }

    private void addListeners() {
        addItemButton = findViewById(R.id.addNewElementButton);
        addItemButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                addItem();
            }
        });

    }

    public void addItem() {
        tableLayout = findViewById(R.id.tableLayout);
        TableRow tableRow = new TableRow(this);
        tableRow.setGravity(Gravity.CENTER_HORIZONTAL);
        String nameOfItem = nameOfItemEditText.getText().toString();

        EditText textView = new EditText(this);
        textView.setText(nameOfItem);
        tableRow.addView(textView);

        ImageButton deleteItem = new ImageButton(this);
        deleteItem.setBackgroundResource(R.drawable.ic_delete);
        tableRow.addView(deleteItem);

        tableLayout.addView(tableRow);
    }
}
