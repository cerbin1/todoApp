package com.example.bartek.todoapp.database;


public final class DatabaseNamesRepository {
    static final String DATABASE_NAME = "todo_list.db";
    static final String TABLE_TEST = "test";
    static final String TABLE_LISTS = "Lists";
    static final String TABLE_ITEMS = "Items";
    static final int DATABASE_VERSION = 1;

    static final String ID = "Id";
    static final String NAME = "Name";
    static final String ELEMENT1 = "Element1";
    static final String ELEMENT2 = "Element2";
    static final String ELEMENT3 = "Element3";

    static final String SQL_CREATE_ENTRIES = "CREATE TABLE "
            + TABLE_TEST + "("
            + ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
            + NAME + " TEXT, "
            + ELEMENT1 + " TEXT, "
            + ELEMENT2 + " TEXT, "
            + ELEMENT3 + " TEXT)";
    static final String SQL_DELETE_ENTRIES =
            "DROP TABLE IF EXISTS " + TABLE_TEST;

    static final String SQL_SELECT_LISTS = "SELECT " + NAME + " FROM " + TABLE_TEST;
}
