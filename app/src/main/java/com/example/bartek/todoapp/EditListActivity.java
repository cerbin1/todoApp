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
        saveEditsToDatabase();
        startMainActivity();
    }

    private void saveEditsToDatabase() {
        database.deleteListElements(todoList.getId());
        String newName = getNewListNameFromEditText();
        if (todoList.isRenamed(newName)) {
            database.updateListName(todoList.getId(), newName);
        }
        int itemsCount = tableLayout.getChildCount() - 1;
        String[] itemNames = getNamesOfItems(itemsCount);
        todoList.setItemsFromArray(itemNames);
        database.addItemsToList(todoList.getId(), todoList.getItems());
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
            tableRow.addView(createDeleteItemImageButton(tableRow, item));
            tableLayout.addView(tableRow);
        }
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
    private ImageButton createDeleteItemImageButton(final TableRow tableRow, final Item item) {
        ImageButton deleteItem = new ImageButton(this);
        deleteItem.setBackgroundResource(R.drawable.ic_delete);
        deleteItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                tableLayout.removeView(tableRow);
                todoList.remove(item);
            }
        });
        return deleteItem;
    }
}
