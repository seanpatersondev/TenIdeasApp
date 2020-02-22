package com.example.android.tenideas.model;

public class Idea {
    private int id;
    private String idea;
    private Boolean isFavourite;
    private int dateId;

    public Idea(int id, String idea, Boolean isFavourite, int dateId) {
        this.id = id;
        this.idea = idea;
        this.isFavourite = isFavourite;
        this.dateId = dateId;
    }

    public Idea() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getIdea() {
        return idea;
    }

    public void setIdea(String idea) {
        this.idea = idea;
    }

    public Boolean getFavourite() {
        return isFavourite;
    }

    public void setFavourite(Boolean favourite) {
        isFavourite = favourite;
    }

    public int getDateId() {
        return dateId;
    }

    public void setDateId(int dateId) {
        this.dateId = dateId;
    }
}
