package com.example.bartek.todoapp;

import android.database.Cursor;

import com.example.bartek.todoapp.database.DatabaseHelper;
import com.example.bartek.todoapp.database.UnexpectedDataException;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class TodoList {
    private DatabaseHelper database;
    private final int id;
    private String name;
    private List<Item> items;

    public TodoList(DatabaseHelper database, int id) {
        this.database = database;
        this.id = id;
        name = getNameFromDatabase(id);
        items = new ArrayList<>(5);
        setItems();
//        this.items = items;
    }

    private String getNameFromDatabase(int id) {
        Cursor data = database.getNameOfList(id);
        if (data.moveToNext()) {
            return data.getString(0);
        } else {
            throw new UnexpectedDataException("Not found name of list with given id!");
        }
    }

    private void setItems() {
        Cursor data = database.getItems(id);
        while (data.moveToNext()) {
            String name = data.getString(0);
            boolean checked;
            int id = data.getInt(1);
            if (isItemUnchecked(data)) {
                checked = false;
            } else if (isItemChecked(data)) {
                checked = true;
            } else {
                throw new UnexpectedDataException("Wrong data assigned to SQL boolean, may be 1 or 0 only!");
            }
            items.add(new Item(name, checked, id));
        }
    }

    private boolean isItemChecked(Cursor data) {
        return data.getInt(2) == 1;
    }

    private boolean isItemUnchecked(Cursor data) {
        return data.getInt(2) == 0;
    }

    public List<Item> getItems() {
        return items;
    }

    public int getId() {
        return id;
    }

    public void remove(Item item) {
        items.remove(item);
    }

    public String getName() {
        return name;
    }

    public boolean isRenamed(String newName) {
        return !name.equals(newName);
    }

    public void setItemsFromArray(String[] itemNames) {
        items.clear();
        items = new ArrayList<>(itemNames.length);
        for (int i = 0; i < itemNames.length; i++) {
            items.add(new Item(itemNames[i], false));
        }
    }
}
