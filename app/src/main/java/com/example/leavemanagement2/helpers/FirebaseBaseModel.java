package com.example.leavemanagement2.helpers;

public class FirebaseBaseModel {
    String id;

    public FirebaseBaseModel() {
    }

    public FirebaseBaseModel(String id) {
        this.id = id;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
