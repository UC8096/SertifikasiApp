package com.example.todoapp.models;

import io.realm.RealmObject;

public class Todo extends RealmObject {
    private String id;
    private String nama;
    private String alamat;
    private String phone;
    private String sex;
    private String location;
    private byte[] image;

    public String getID() {
        return id;
    }

    public void setID(String value) {
        this.id = value;
    }

    public String getNama() {
        return nama;
    }

    public void setNama(String value) {
        this.nama = value;
    }

    public String getAlamat() {
        return alamat;
    }

    public void setAlamat(String value) {
        this.alamat = value;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String value) {
        this.phone = value;
    }

    public String getSex() {
        return sex;
    }

    public void setSex(String value) {
        this.sex = value;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String value) {
        this.location = value;
    }

    public byte[] getImage() {
        return image;
    }

    public void setImage(byte[] value) {
        this.image = value;
    }
}
