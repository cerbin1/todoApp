package com.example.bartek.todoapp.database;


public final class DatabaseNamesRepository {
    static final String DATABASE_NAME = "todo_list.db";
    static final String TABLE_LISTS = "Lists";
    static final String TABLE_ITEMS = "Items";
    static final int DATABASE_VERSION = 5;

    static final String ID_LIST = "Id";
    static final String NAME_LIST = "Name";

    static final String ID_LIST_FOREIGN_KEY = "IdList";
    static final String NAME_ITEMS = "Name";

    static final String SQL_CREATE_ENTRIES = "CREATE TABLE "
            + TABLE_LISTS + "("
            + ID_LIST + " INTEGER PRIMARY KEY AUTOINCREMENT, "
            + NAME_LIST + " TEXT);"
            + "CREATE TABLE "
            + TABLE_ITEMS + "("
            + ID_LIST_FOREIGN_KEY + " FOREIGN KEY(" + ID_LIST_FOREIGN_KEY + ") REFERENCES " + TABLE_LISTS + "(" + ID_LIST + "),"
            + NAME_ITEMS + " TEXT);";

    static final String SQL_DELETE_ENTRIES =
            "DROP TABLE IF EXISTS " + TABLE_LISTS + ";" + " DROP TABLE IF EXISTS " + TABLE_ITEMS + ";";

    static final String SQL_SELECT_LISTS = "SELECT " + NAME_LIST + " FROM " + TABLE_LISTS;
}
