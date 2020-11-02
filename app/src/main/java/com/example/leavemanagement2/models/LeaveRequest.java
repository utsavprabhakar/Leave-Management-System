package com.example.leavemanagement2.models;

import com.example.leavemanagement2.helpers.FirebaseBaseModel;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.Timestamp;
import com.google.firebase.firestore.FirebaseFirestore;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;

public class LeaveRequest extends FirebaseBaseModel implements Serializable {
    String employeeID;
    String employeeName;
    ArrayList<String> managerIDs;
    Timestamp startDate;
    Timestamp endDate;
    String description;
    String reason;
    RequestStatus requestStatus;
    long createdAt;
    long approvedAt;
    long rejectedAt;


    public void save(OnSuccessListener onSuccessListener, OnFailureListener onFailureListener) {
        HashMap<String, Object> map = new HashMap<>();
        map.put("employeeID", getEmployeeID());
        map.put("employeeName", getEmployeeName());
        map.put("managerIDs", getManagerIDs());
        map.put("startDate", getStartDate());
        map.put("endDate", getEndDate());
        map.put("description", getDescription());
        map.put("reason", getReason());
        map.put("createdAt", System.currentTimeMillis());
        map.put("approvedAt", getApprovedAt());
        map.put("rejectedAt", getRejectedAt());
        map.put("requestStatus", getRequestStatus());
        map.put("id", getId());

        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("leaveRequests")
                .document(getId())
                .set(map)
                .addOnSuccessListener(onSuccessListener)
                .addOnFailureListener(onFailureListener);
    }

    public void update(OnSuccessListener onSuccessListener, OnFailureListener onFailureListener) {
        HashMap<String, Object> map = new HashMap<>();
        map.put("employeeID", getEmployeeID());
        map.put("employeeName", getEmployeeName());
        map.put("managerIDs", getManagerIDs());
        map.put("startDate", getStartDate());
        map.put("endDate", getEndDate());
        map.put("description", getDescription());
        map.put("reason", getReason());
        map.put("createdAt", System.currentTimeMillis());
        map.put("approvedAt", getApprovedAt());
        map.put("rejectedAt", getRejectedAt());
        map.put("requestStatus", getRequestStatus());
        map.put("id", getId());

        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("leaveRequests")
                .document(getId())
                .set(map)
                .addOnSuccessListener(onSuccessListener)
                .addOnFailureListener(onFailureListener);
    }

    public String getEmployeeID() {
        return employeeID;
    }

    public void setEmployeeID(String employeeID) {
        this.employeeID = employeeID;
    }

    public ArrayList<String> getManagerIDs() {
        return managerIDs;
    }

    public void setManagerIDs(ArrayList<String> managerIDs) {
        this.managerIDs = managerIDs;
    }

    public Timestamp getStartDate() {
        return startDate;
    }

    public void setStartDate(Timestamp startDate) {
        this.startDate = startDate;
    }

    public Timestamp getEndDate() {
        return endDate;
    }

    public void setEndDate(Timestamp endDate) {
        this.endDate = endDate;
    }

    public long getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(long createdAt) {
        this.createdAt = createdAt;
    }

    public long getApprovedAt() {
        return approvedAt;
    }

    public void setApprovedAt(long approvedAt) {
        this.approvedAt = approvedAt;
    }

    public long getRejectedAt() {
        return rejectedAt;
    }

    public void setRejectedAt(long rejectedAt) {
        this.rejectedAt = rejectedAt;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public String getEmployeeName() {
        return employeeName;
    }

    public void setEmployeeName(String employeeName) {
        this.employeeName = employeeName;
    }

    public RequestStatus getRequestStatus() {
        return requestStatus;
    }

    public void setRequestStatus(RequestStatus requestStatus) {
        this.requestStatus = requestStatus;
    }
}



