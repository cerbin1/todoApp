package com.example.bartek.todoapp;

import android.content.Intent;
import android.database.Cursor;
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
import com.example.bartek.todoapp.database.UnexpectedDataException;

import java.util.ArrayList;
import java.util.List;

import static com.example.bartek.todoapp.Item.INITIAL_ITEMS_AMOUNT;
import static com.example.bartek.todoapp.StringResources.ID_OF_LIST;

public class EditListActivity extends AppCompatActivity {
    private DatabaseHelper database;
    private final List<Item> listItems = new ArrayList<>(INITIAL_ITEMS_AMOUNT);

    private int listId;
    private TableLayout tableLayout;
    private String listName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_list);

        database = new DatabaseHelper(this);
        tableLayout = findViewById(R.id.tableLayout);
        listId = getIntent().getIntExtra(ID_OF_LIST, -1);
        listName = getNameOfList();
        getListItems();
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
            saveEditsToList();
            return true;
        }
        if (id == R.id.action_cancel) {
            super.onBackPressed();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void saveEditsToList() {
        String newName = getNewListNameFromEditText();
        saveEditsToDatabase(newName);
        startMainActivity();
    }

    private void saveEditsToDatabase(String newName) {
        database.deleteListElements(listId);
        if (!newName.equals(listName)) {
            database.updateListName(listId, newName);
        }
        listItems.clear();
        for (int i = 1; i < tableLayout.getChildCount(); i++) {
            TableRow tableRow = (TableRow) tableLayout.getChildAt(i);
            EditText editText = (EditText) tableRow.getChildAt(0);
            String itemName = editText.getText().toString();
            listItems.add(new Item(itemName, false));
        }
        database.addItemsToList(listId, listItems);
    }

    @NonNull
    private String getNewListNameFromEditText() {
        return ((EditText) ((TableRow) tableLayout.getChildAt(0)).getChildAt(0)).getText().toString();
    }

    private void startMainActivity() {
        startActivity(new Intent(this, MainActivity.class));
    }

    private void createViewToEdit() {
        tableLayout = findViewById(R.id.tableLayout);

        TableRow headingRow = createTableRow();
        headingRow.addView(createNameOfListEditText());
        tableLayout.addView(headingRow);

        for (Item item : listItems) {
            final TableRow tableRow = createTableRow();
            tableRow.addView(createNameOfItemEditText(item.getName()));
            tableRow.addView(createDeleteItemImageButton(tableRow, item));
            tableLayout.addView(tableRow);
        }
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
    private EditText createNameOfItemEditText(String name) {
        EditText nameOfItemEditText = new EditText(this);
        nameOfItemEditText.setText(name);
        return nameOfItemEditText;
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

    public EditText createNameOfListEditText() {
        EditText editText = new EditText(this);
        editText.setTextSize(25);
        editText.setText(listName);
        return editText;
    }

    private String getNameOfList() {
        Cursor data = database.getNameOfList(listId);
        if (data.moveToNext()) {
            return data.getString(0);
        } else {
            throw new UnexpectedDataException("Not found name of list with given id!");
        }
    }

    @NonNull
    private TableRow createTableRow() {
        TableRow tableRow = new TableRow(this);
        tableRow.setGravity(Gravity.CENTER);
        return tableRow;
    }
}
