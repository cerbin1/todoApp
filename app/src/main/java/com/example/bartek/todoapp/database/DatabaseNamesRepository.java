package com.example.bartek.todoapp.database;


public final class DatabaseNamesRepository {
    static final String DATABASE_NAME = "todo_list.db";
    static final String TABLE_LISTS = "Lists";
    static final String TABLE_ITEMS = "Items";
    static final int DATABASE_VERSION = 8;

    static final String LIST_ID = "Id";
    static final String LIST_NAME = "Name";

    static final String ITEM_ID_LIST_FOREIGN_KEY = "IdList";
    static final String ITEM_ID = "ItemId";
    static final String ITEM_NAME = "ItemName";
    static final String ITEM_CHECKED = "Checked";

    static final String CREATE_TABLE_LISTS = "CREATE TABLE "
            + TABLE_LISTS + "("
            + LIST_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
            + LIST_NAME + " TEXT)";

    static final String CREATE_TABLE_ITEMS = " CREATE TABLE "
            + TABLE_ITEMS + "("
            + ITEM_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
            + ITEM_ID_LIST_FOREIGN_KEY + " INTEGER, "
            + ITEM_NAME + " TEXT, "
            + ITEM_CHECKED + " INTEGER,"
            + "FOREIGN KEY(" + ITEM_ID_LIST_FOREIGN_KEY + ") REFERENCES " + TABLE_LISTS + "(" + LIST_ID + ")"
            + ");";

    static final String DELETE_TABLE_LISTS = "DROP TABLE IF EXISTS " + TABLE_LISTS;
    static final String DELETE_TABLE_ITEMS = " DROP TABLE IF EXISTS " + TABLE_ITEMS;
    static final String SELECT_LISTS = "SELECT " + LIST_ID + ", " + LIST_NAME + " FROM " + TABLE_LISTS;
    static final String SELECT_ID_OF_LIST = "SELECT " + LIST_ID + " FROM " + TABLE_LISTS + " WHERE " + LIST_NAME + "=?";
    static final String SELECT_ITEMS = "SELECT " + TABLE_ITEMS + "." + ITEM_NAME + ", " + TABLE_ITEMS + "." + ITEM_ID + ", " + TABLE_ITEMS
            + "." + ITEM_CHECKED + " FROM " + TABLE_LISTS + " JOIN " + TABLE_ITEMS + " ON " + TABLE_LISTS
            + "." + LIST_ID + "=" + TABLE_ITEMS + "." + ITEM_ID_LIST_FOREIGN_KEY + " WHERE " + TABLE_LISTS + "." + LIST_NAME + "=?";

    static String getInsertItemToListQuery(int idOfList, String name) {
        return "INSERT INTO " + TABLE_ITEMS + "(" + ITEM_ID_LIST_FOREIGN_KEY + ", " + ITEM_NAME + ", " + ITEM_CHECKED + ") VALUES(" + idOfList + ", \"" + name + "\", 0);";
    }
}
