package com.example.bartek.todoapp;

public class Item {
    public final static int INITIAL_ITEMS_AMOUNT = 5;

    private final String name;
    private final int id;
    private final boolean checked;

    public Item(String name, int id, boolean checked) {
        this.name = name;
        this.id = id;
        this.checked = checked;
    }

    public String getName() {
        return name;
    }

    public int getId() {
        return id;
    }

    public boolean isChecked() {
        return checked;
    }

    public boolean isEmpty() {
        return name.equals("");
    }
}
