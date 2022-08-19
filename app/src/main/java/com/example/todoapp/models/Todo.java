package com.example.todoapp.models;

import io.realm.RealmObject;

public class Todo extends RealmObject {
    private String id;
    private String title;
    private String description;
    private String location;

//    public Todo() {
//    }
//
//    public Todo(String id, String title, String description) {
//        this.id = id;
//        this.title = title;
//        this.description = description;
//    }

    public String getID() { return id; }
    public void setID(String value) { this.id = value; }

    public String getTitle() { return title; }
    public void setTitle(String value) { this.title = value; }

    public String getDescription() { return description; }
    public void setDescription(String value) { this.description = value; }

    public String getLocation() { return location; }
    public void setLocation(String value) { this.location = value; }
}
