package com.example.bartek.todoapp.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import static com.example.bartek.todoapp.database.DatabaseNamesRepository.DATABASE_NAME;
import static com.example.bartek.todoapp.database.DatabaseNamesRepository.ELEMENT1;
import static com.example.bartek.todoapp.database.DatabaseNamesRepository.ELEMENT2;
import static com.example.bartek.todoapp.database.DatabaseNamesRepository.ELEMENT3;
import static com.example.bartek.todoapp.database.DatabaseNamesRepository.NAME;
import static com.example.bartek.todoapp.database.DatabaseNamesRepository.SQL_CREATE_ENTRIES;
import static com.example.bartek.todoapp.database.DatabaseNamesRepository.SQL_DELETE_ENTRIES;
import static com.example.bartek.todoapp.database.DatabaseNamesRepository.SQL_SELECT_LISTS;
import static com.example.bartek.todoapp.database.DatabaseNamesRepository.TABLE_TEST;

public class DatabaseHelper extends SQLiteOpenHelper {

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, 4);
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

    public boolean insert(String name) {
        SQLiteDatabase database = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(NAME, name);
        values.put(ELEMENT1, "elem1");
        values.put(ELEMENT2, "elem2");
        values.put(ELEMENT3, "elem3");
        long result = database.insert(TABLE_TEST, null, values);
        return result != -1;
    }

    public Cursor getLists() {
        SQLiteDatabase database = this.getReadableDatabase();
        return database.rawQuery(SQL_SELECT_LISTS, null);
    }
}
