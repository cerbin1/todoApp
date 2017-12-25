package com.example.bartek.todoapp;

public class Item {
    private final String name;
    private int id;
    private boolean checked;

    public Item(String name, int id, boolean checked) {
        this.name = name;
        this.id = id;
        this.checked = checked;
    }

    public String getName() {
        return name;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }

    public boolean isEmpty() {
        return name.equals("");
    }

    public boolean isChecked() {
        return checked;
    }
}
