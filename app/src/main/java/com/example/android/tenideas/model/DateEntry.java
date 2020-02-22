package com.example.android.tenideas.model;

public class DateEntry {
    private int id;
    private String dateAdded;

    public DateEntry() {
    }

    public DateEntry(int id, String dateAdded) {
        this.id = id;
        this.dateAdded = dateAdded;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getDateAdded() {
        return dateAdded;
    }

    public void setDateAdded(String dateAdded) {
        this.dateAdded = dateAdded;
    }
}
