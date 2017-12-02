package com.example.bartek.todoapp.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.bartek.todoapp.Item;

import java.util.List;

import static com.example.bartek.todoapp.database.DatabaseNamesRepository.DATABASE_NAME;
import static com.example.bartek.todoapp.database.DatabaseNamesRepository.DATABASE_VERSION;
import static com.example.bartek.todoapp.database.DatabaseNamesRepository.NAME_LIST;
import static com.example.bartek.todoapp.database.DatabaseNamesRepository.SQL_CREATE_ENTRIES;
import static com.example.bartek.todoapp.database.DatabaseNamesRepository.SQL_DELETE_ENTRIES;
import static com.example.bartek.todoapp.database.DatabaseNamesRepository.SQL_SELECT_LISTS;
import static com.example.bartek.todoapp.database.DatabaseNamesRepository.TABLE_LISTS;

public class DatabaseHelper extends SQLiteOpenHelper {

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL(SQL_CREATE_ENTRIES);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqLiteDatabase.execSQL(SQL_DELETE_ENTRIES);
        onCreate(sqLiteDatabase);
    }

    public boolean createTodoList(String name) {
        SQLiteDatabase database = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(NAME_LIST, name);
        long result = database.insert(TABLE_LISTS, null, values);
        return result != -1;
    }

    public boolean addItems(String nameOfList, List<Item> items) {
        SQLiteDatabase database = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        Cursor data = database.rawQuery("SELECT Id FROM Lists WHERE Name =" + nameOfList, null);
        int idOfList = data.getInt(0);

        for (Item item : items) {
            database.execSQL("INSERT INTO Items VALUES(" + idOfList + ", " + item.getName() + ")");
        }
        values.put(NAME_LIST, nameOfList);
        long result = database.insert(TABLE_LISTS, null, values);
        return result != -1;
    }

    public Cursor getLists() {
        SQLiteDatabase database = this.getReadableDatabase();
        return database.rawQuery(SQL_SELECT_LISTS, null);
    }
}
