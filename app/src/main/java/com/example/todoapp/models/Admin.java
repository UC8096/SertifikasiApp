package com.example.todoapp.models;

public class Admin {
    private String id;
    private String email;
    private String password;

    Admin () {}

    public Admin(String id, String email, String password) {
        this.id = id;
        this.email = email;
        this.password = password;
    }

    public String getID() { return id; }
    public void setID(String value) { this.id = value; }

    public String getEmail() { return email; }
    public void setEmail(String value) { this.email = value; }

    public String getPassword() { return password; }
    public void setPassword(String value) { this.password = value; }

}
