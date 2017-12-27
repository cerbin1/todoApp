package com.example.bartek.todoapp.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.bartek.todoapp.Item;

import java.util.List;

import static com.example.bartek.todoapp.database.DatabaseNamesRepository.CREATE_TABLE_ITEMS;
import static com.example.bartek.todoapp.database.DatabaseNamesRepository.CREATE_TABLE_LISTS;
import static com.example.bartek.todoapp.database.DatabaseNamesRepository.DATABASE_NAME;
import static com.example.bartek.todoapp.database.DatabaseNamesRepository.DATABASE_VERSION;
import static com.example.bartek.todoapp.database.DatabaseNamesRepository.DELETE_TABLE_ITEMS;
import static com.example.bartek.todoapp.database.DatabaseNamesRepository.DELETE_TABLE_LISTS;
import static com.example.bartek.todoapp.database.DatabaseNamesRepository.ITEM_CHECKED;
import static com.example.bartek.todoapp.database.DatabaseNamesRepository.ITEM_ID;
import static com.example.bartek.todoapp.database.DatabaseNamesRepository.ITEM_ID_LIST_FOREIGN_KEY;
import static com.example.bartek.todoapp.database.DatabaseNamesRepository.LIST_ID;
import static com.example.bartek.todoapp.database.DatabaseNamesRepository.LIST_NAME;
import static com.example.bartek.todoapp.database.DatabaseNamesRepository.SELECT_ID_OF_LIST;
import static com.example.bartek.todoapp.database.DatabaseNamesRepository.SELECT_ITEMS;
import static com.example.bartek.todoapp.database.DatabaseNamesRepository.SELECT_LISTS;
import static com.example.bartek.todoapp.database.DatabaseNamesRepository.SELECT_NAME_OF_LIST;
import static com.example.bartek.todoapp.database.DatabaseNamesRepository.TABLE_ITEMS;
import static com.example.bartek.todoapp.database.DatabaseNamesRepository.TABLE_LISTS;
import static com.example.bartek.todoapp.database.DatabaseNamesRepository.getInsertItemToListQuery;

public class DatabaseHelper extends SQLiteOpenHelper {

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL(CREATE_TABLE_LISTS);
        sqLiteDatabase.execSQL(CREATE_TABLE_ITEMS);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqLiteDatabase.execSQL(DELETE_TABLE_LISTS);
        sqLiteDatabase.execSQL(DELETE_TABLE_ITEMS);
        onCreate(sqLiteDatabase);
    }

    public boolean createTodoList(String name) {
        SQLiteDatabase database = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(LIST_NAME, name);
        long result = database.insert(TABLE_LISTS, null, values);
        return result != -1;
    }

    public void addItemsToList(int idOfList, List<Item> items) {
        SQLiteDatabase database = this.getWritableDatabase();
        for (Item item : items) {
            database.execSQL(getInsertItemToListQuery(idOfList, item.getName()));
        }
    }

    public Cursor getLists() {
        SQLiteDatabase database = this.getReadableDatabase();
        return database.rawQuery(SELECT_LISTS, null);
    }

    public Cursor getIdOfList(String nameOfList) {
        SQLiteDatabase database = this.getReadableDatabase();
        return database.rawQuery(SELECT_ID_OF_LIST, new String[]{nameOfList});
    }

    public Cursor getItems(int listId) {
        SQLiteDatabase database = this.getReadableDatabase();
        return database.rawQuery(SELECT_ITEMS, new String[]{Integer.toString(listId)});
    }

    public Cursor getNameOfList(int listId) {
        SQLiteDatabase database = this.getReadableDatabase();
        return database.rawQuery(SELECT_NAME_OF_LIST, new String[]{Integer.toString(listId)});
    }

    public boolean changeStatusOfItem(int idOfItem, int value) {
        SQLiteDatabase database = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(ITEM_CHECKED, value);
        int result = database.update(TABLE_ITEMS, contentValues, ITEM_ID + "=" + idOfItem, null);
        return result != 0;
    }

    public void deleteListElements(int listId) {
        SQLiteDatabase database = this.getWritableDatabase();
        database.delete(TABLE_ITEMS, ITEM_ID_LIST_FOREIGN_KEY + "=?", new String[]{Integer.toString(listId)});
    }

    public boolean updateListName(int listId, String listName) {
        SQLiteDatabase database = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(LIST_NAME, listName);
        int result = database.update(TABLE_LISTS, contentValues, LIST_ID + "=" + listId, null);
        return result != 0;
    }

    public void uncheckAllItems(int listId) {
        SQLiteDatabase database = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(ITEM_CHECKED, 0);
        database.update(TABLE_ITEMS, contentValues, ITEM_ID_LIST_FOREIGN_KEY + "=" + listId, null);
    }
}
