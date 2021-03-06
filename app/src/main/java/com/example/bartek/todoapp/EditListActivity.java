package com.example.bartek.todoapp;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TableLayout;
import android.widget.TableRow;

import com.example.bartek.todoapp.database.DatabaseHelper;

import static android.widget.Toast.LENGTH_SHORT;
import static android.widget.Toast.makeText;
import static com.example.bartek.todoapp.StringResources.ID_OF_LIST;

public class EditListActivity extends AppCompatActivity {
    private DatabaseHelper database;
    private TableLayout tableLayout;
    private TodoList todoList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_list);

        database = new DatabaseHelper(this);
        tableLayout = findViewById(R.id.tableLayout);
        int listIdFromExtra = getIntent().getIntExtra(ID_OF_LIST, -1);
        todoList = new TodoList(database, listIdFromExtra);
        createViewToEdit();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.exit_edit, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_save) {
            saveListEdits();
            return true;
        }
        if (id == R.id.action_cancel) {
            super.onBackPressed();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void saveListEdits() {
        boolean resultOfSaveEdits = saveEditsToDatabase();
        if (resultOfSaveEdits) {
            startMainActivity();
        }
    }

    private boolean saveEditsToDatabase() {
        int itemsCount = tableLayout.getChildCount() - 2;
        String newName = getNewListNameFromEditText();
        String[] itemNames = getNamesOfItems(itemsCount);
        if (itemsCount < 1) {
            displayToastWithText("At least one item is required!");
            return false;
        }
        if (newName.isEmpty()) {
            displayToastWithText("Todo list name is empty!");
            return false;
        }
        if (isAnyEmptyItemNameIn(itemNames)) {
            displayToastWithText("One of item name is empty!");
            return false;
        }
        database.deleteListElements(todoList.getId());
        if (todoList.isRenamed(newName)) {
            database.updateListName(todoList.getId(), newName);
        }
        todoList.setItemsFromArray(itemNames);
        database.addItemsToList(todoList.getId(), todoList.getItems());
        return true;
    }

    private boolean isAnyEmptyItemNameIn(String[] itemNames) {
        for (String name : itemNames) {
            if (name.isEmpty()) {
                return true;
            }
        }
        return false;
    }

    @NonNull
    private String getNewListNameFromEditText() {
        return ((EditText) ((TableRow) tableLayout.getChildAt(0)).getChildAt(0)).getText().toString();
    }

    private String[] getNamesOfItems(int itemsCount) {
        String[] itemNames = new String[itemsCount];
        for (int i = 0; i < itemsCount; i++) {
            TableRow tableRow = (TableRow) tableLayout.getChildAt(i + 1);
            EditText editText = (EditText) tableRow.getChildAt(0);
            String itemName = editText.getText().toString();
            itemNames[i] = itemName;
        }
        return itemNames;
    }

    private void startMainActivity() {
        startActivity(new Intent(this, MainActivity.class));
    }

    private void createViewToEdit() {
        tableLayout = findViewById(R.id.tableLayout);

        TableRow headingRow = createTableRow();
        headingRow.addView(createNameOfListEditText());
        tableLayout.addView(headingRow);

        for (Item item : todoList.getItems()) {
            final TableRow tableRow = createTableRow();
            tableRow.addView(createNameOfItemEditText(item.getName()));
            tableRow.addView(createDeleteItemImageButton(tableRow));
            tableLayout.addView(tableRow);
        }
        tableLayout.addView(createRowWithButtonToAddItem());
    }

    private TableRow createRowWithButtonToAddItem() {
        TableRow tableRow = createTableRow();
        tableRow.addView(createButtonToAddItem(tableRow));
        return tableRow;
    }

    private ImageButton createButtonToAddItem(final TableRow tableRow) {
        ImageButton imageButton = new ImageButton(this);
        imageButton.setImageResource(R.drawable.ic_add_item);
        imageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                appendRowWithEmptyItem();
                tableLayout.removeView(tableRow);
                tableLayout.addView(createRowWithButtonToAddItem());
            }
        });
        return imageButton;
    }

    private void appendRowWithEmptyItem() {
        TableRow tableRow = createTableRow();
        tableRow.addView(createNameOfItemEditText(""));
        tableRow.addView(createDeleteItemImageButton(tableRow));
        tableLayout.addView(tableRow);
    }

    @NonNull
    private TableRow createTableRow() {
        TableRow tableRow = new TableRow(this);
        tableRow.setGravity(Gravity.CENTER);
        return tableRow;
    }

    public EditText createNameOfListEditText() {
        EditText editText = new EditText(this);
        editText.setTextSize(25);
        editText.setText(todoList.getName());
        return editText;
    }

    @NonNull
    private EditText createNameOfItemEditText(String name) {
        EditText nameOfItemEditText = new EditText(this);
        nameOfItemEditText.setText(name);
        return nameOfItemEditText;
    }

    @NonNull
    private ImageButton createDeleteItemImageButton(final TableRow tableRow) {
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

    private void displayToastWithText(String text) {
        makeText(EditListActivity.this, text, LENGTH_SHORT).show();
    }
}
