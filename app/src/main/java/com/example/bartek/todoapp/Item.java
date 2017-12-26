package com.example.bartek.todoapp;

public class Item {
    public final static int INITIAL_ITEMS_AMOUNT = 5;

    private final String name;
    private final boolean checked;
    private int id;

    public Item(String name, boolean checked) {
        this.name = name;
        this.checked = checked;
    }

    public Item(String name, boolean checked, int id) {
        this.name = name;
        this.checked = checked;
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public boolean isChecked() {
        return checked;
    }

    public int getId() {
        return id;
    }

    public boolean isEmpty() {
        return name.equals("");
    }
}
