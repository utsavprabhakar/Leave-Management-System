package com.example.leavemanagement2.models;

import com.example.leavemanagement2.helpers.FirebaseBaseModel;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.FirebaseFirestore;


import java.util.ArrayList;
import java.util.HashMap;

public class Employee extends FirebaseBaseModel {
    String employeeID;
    String name;
    TeamType teamType;
    SubTeamType subTeamType;
    ArrayList<String> managerIDs;
    String password;

    public void save(OnSuccessListener onSuccessListener, OnFailureListener onFailureListener) {
        HashMap<String, Object> map = new HashMap<>();
        map.put("employeeID", getEmployeeID());
        map.put("managerIDs", getManagerIDs());
        map.put("teamType", getTeamType());
        map.put("subTeamType", getSubTeamType());
        map.put("name", getName());
        map.put("password", getPassword());
        map.put("id", getId());

        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("users")
                .document(getId())
                .set(map)
                .addOnSuccessListener(onSuccessListener)
                .addOnFailureListener(onFailureListener);
    }

    public void update(OnSuccessListener onSuccessListener, OnFailureListener onFailureListener) {
        HashMap<String, Object> map = new HashMap<>();
        map.put("employeeID", getEmployeeID());
        map.put("managerIDs", getManagerIDs());
        map.put("teamType", getTeamType());
        map.put("subTeamType", getSubTeamType());
        map.put("name", getName());
        map.put("password", getPassword());
        map.put("id", getId());

        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("users")
                .document(getId())
                .set(map)
                .addOnSuccessListener(onSuccessListener)
                .addOnFailureListener(onFailureListener);
    }

    public Employee(String employeeID, String name, TeamType teamType, SubTeamType subTeamType, ArrayList<String> managerIDs) {

    }

    public Employee() {

    }

    public String getEmployeeID() {
        return employeeID;
    }

    public void setEmployeeID(String employeeID) {
        this.employeeID = employeeID;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public TeamType getTeamType() {
        return teamType;
    }

    public void setTeamType(TeamType teamType) {
        this.teamType = teamType;
    }

    public SubTeamType getSubTeamType() {
        return subTeamType;
    }

    public void setSubTeamType(SubTeamType subTeamType) {
        this.subTeamType = subTeamType;
    }

    public ArrayList<String> getManagerIDs() {
        return managerIDs;
    }

    public void setManagerIDs(ArrayList<String> managerIDs) {
        this.managerIDs= managerIDs;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}

