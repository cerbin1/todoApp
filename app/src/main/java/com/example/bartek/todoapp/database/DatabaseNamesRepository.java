package com.example.bartek.todoapp.database;


public final class DatabaseNamesRepository {
    static final String DATABASE_NAME = "todo_list.db";
    static final String TABLE_LISTS = "Lists";
    static final String TABLE_ITEMS = "Items";
    static final int DATABASE_VERSION = 5;

    static final String ID_LIST = "Id";
    static final String NAME_LIST = "Name";

    static final String ID_LIST_FOREIGN_KEY = "IdList";
    static final String NAME_ITEMS = "ItemName";
    static final String ID_OF_ITEM = "ItemId";

    static final String CREATE_TABLE_LISTS = "CREATE TABLE "
            + TABLE_LISTS + "("
            + ID_LIST + " INTEGER PRIMARY KEY AUTOINCREMENT, "
            + NAME_LIST + " TEXT)";

    static final String CREATE_TABLE_ITEMS = " CREATE TABLE "
            + TABLE_ITEMS + "("
            + ID_OF_ITEM + " INTEGER, "
            + ID_LIST_FOREIGN_KEY + " INTEGER, "
            + NAME_ITEMS + " TEXT, "
            + "Checked INTEGER,"
            + "FOREIGN KEY(" + ID_LIST_FOREIGN_KEY + ") REFERENCES " + TABLE_LISTS + "(" + ID_LIST + ")"
            + ");";

    static final String DELETE_TABLE_LISTS = "DROP TABLE IF EXISTS " + TABLE_LISTS;
    static final String DELETE_TABLE_ITEMS = " DROP TABLE IF EXISTS " + TABLE_ITEMS;
    static final String SQL_SELECT_LISTS = "SELECT " + NAME_LIST + " FROM " + TABLE_LISTS;
    static final String SQL_SELECT_ID_OF_LIST = "SELECT " + ID_LIST + " FROM " + TABLE_LISTS + " WHERE " + NAME_LIST + "=?";
}
