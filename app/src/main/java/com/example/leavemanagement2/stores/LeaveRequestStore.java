package com.example.leavemanagement2.stores;

import androidx.annotation.NonNull;

import com.example.leavemanagement2.models.LeaveRequest;
import com.example.leavemanagement2.models.RequestStatus;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.WriteBatch;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

public class LeaveRequestStore implements OnFailureListener {
    private static LeaveRequestStore instance = null;

    public static synchronized LeaveRequestStore getInstance() {
        if(instance == null) {
            instance = new LeaveRequestStore();
        }
        return instance;
    }

    private LeaveRequestStore(){}

    public void create(LeaveRequest leaveRequest, OnCreateLeaveRequestListener onCreateLeaveRequestListener) {
        leaveRequest.setId(UUID.randomUUID().toString());
        leaveRequest.save(o -> onCreateLeaveRequestListener.onComplete(leaveRequest), this);
    }

    public void createOrUpdate(List<LeaveRequest> leaveRequestList, boolean create, OnLeaveRequestsCreatedListener onLeaveRequestsCreatedListener) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        WriteBatch batch = db.batch();
        for(LeaveRequest leaveRequest : leaveRequestList){
            if(create)
                leaveRequest.setId(UUID.randomUUID().toString());
            HashMap<String, Object> map = new HashMap<>();
            map.put("employeeID", leaveRequest.getEmployeeID());
            map.put("managerIDs", leaveRequest.getManagerIDs());
            map.put("startDate", leaveRequest.getStartDate());
            map.put("endDate", leaveRequest.getEndDate());
            map.put("description", leaveRequest.getDescription());
            map.put("reason", leaveRequest.getReason());
            map.put("createdAt", System.currentTimeMillis());
            map.put("approvedAt", leaveRequest.getApprovedAt());
            map.put("rejectedAt", leaveRequest.getRejectedAt());
            map.put("id", leaveRequest.getId());
            batch.set(createLeaveRequestDoc(leaveRequest.getId()), map);
        }

        batch.commit().addOnSuccessListener(aVoid -> {
            if (onLeaveRequestsCreatedListener != null) {
                onLeaveRequestsCreatedListener.onComplete(leaveRequestList);
            }
        }).addOnFailureListener(this);
    }

    private DocumentReference createLeaveRequestDoc(String docId)
    {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        return db.collection("leaveRequests")
                .document(docId);
    }

    public void remove(LeaveRequest leaveRequest, OnLeaveRequestRemovedListener onLeaveRequestRemovedListener) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("leaveRequests").document(leaveRequest.getId())
                .delete()
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        onLeaveRequestRemovedListener.onComplete(leaveRequest);
                    }
                })
                .addOnFailureListener(this);
    }

    public void update(LeaveRequest leaveRequest, OnLeaveRequestUpdatedListener onLeaveRequestUpdatedListener) {
        leaveRequest.update(o -> onLeaveRequestUpdatedListener.onComplete(leaveRequest), this);
    }

    public void getLeaveRequestsForEmployeeID(String employeeID, OnLeaveRequestsFetchedForEmployeeListener onLeaveRequestsFetchedForEmployeeListener) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("leaveRequests")
                .whereArrayContains("managerIDs", employeeID)
                .whereEqualTo("requestStatus", RequestStatus.PENDING.toString())
                .get()
                .addOnFailureListener(this)
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    List<LeaveRequest> leaveRequests = null;
                    if(!queryDocumentSnapshots.isEmpty()) {
                        leaveRequests = queryDocumentSnapshots.toObjects(LeaveRequest.class);
                    }
                    onLeaveRequestsFetchedForEmployeeListener.onComplete(leaveRequests);
                });
    }

    public void updateLeaveRequestsStatus(ArrayList<LeaveRequest> leaveRequests, OnStatusUpdatedListener onStatusUpdatedListener) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        WriteBatch batch = db.batch();
        for(LeaveRequest leaveRequest : leaveRequests){
            HashMap<String, Object> map = new HashMap<>();
            map.put("requestStatus", leaveRequest.getRequestStatus());
            map.put("approvedAt", leaveRequest.getApprovedAt());
            map.put("rejectedAt", leaveRequest.getRejectedAt());
            batch.update(createLeaveRequestDoc(leaveRequest.getId()), map);
        }

        batch.commit().addOnSuccessListener(aVoid -> {
            if (onStatusUpdatedListener != null) {
                onStatusUpdatedListener.onComplete(leaveRequests);
            }
        }).addOnFailureListener(this);
    }

    public void getLeaveRequestsOfEmployeeID(String employeeID, OnLeaveRequestsFetchedOfEmployeeListener onLeaveRequestsFetchedOfEmployeeListener) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("leaveRequests")
                .whereEqualTo("employeeID", employeeID)
                .get()
                .addOnFailureListener(this)
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    List<LeaveRequest> leaveRequests = null;
                    if(!queryDocumentSnapshots.isEmpty()) {
                        leaveRequests = queryDocumentSnapshots.toObjects(LeaveRequest.class);
                    }
                    onLeaveRequestsFetchedOfEmployeeListener.onComplete(leaveRequests);
                });
    }

    public void getLeaveRequestsOfTeam(String employeeID, OnLeaveRequestsOfTeamFetchedListener onLeaveRequestsOfTeamFetchedListener) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("leaveRequests")
                .whereArrayContains("managerIDs", employeeID)
                .whereEqualTo("requestStatus", RequestStatus.APPROVED.toString())
                .get()
                .addOnFailureListener(this)
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    List<LeaveRequest> leaveRequests = null;
                    if(!queryDocumentSnapshots.isEmpty()) {
                        leaveRequests = queryDocumentSnapshots.toObjects(LeaveRequest.class);
                    }
                    onLeaveRequestsOfTeamFetchedListener.onComplete(leaveRequests);
                });
    }

    @Override
    public void onFailure(@NonNull Exception e) {
        e.printStackTrace();
    }

    //Interfaces
    public interface OnLeaveRequestsFetchedForEmployeeListener {
        void onComplete(List<LeaveRequest> leaveRequestList);
    }

    public interface OnLeaveRequestsFetchedOfEmployeeListener {
        void onComplete(List<LeaveRequest> leaveRequestList);
    }

    public interface OnCreateLeaveRequestListener {
        void onComplete(LeaveRequest leaveRequest);
    }

    public interface OnLeaveRequestUpdatedListener {
        void onComplete(LeaveRequest leaveRequest);
    }

    public interface OnLeaveRequestRemovedListener {
        void onComplete(LeaveRequest leaveRequest);
    }

    public interface OnLeaveRequestsCreatedListener {
        void onComplete(List<LeaveRequest> leaveRequestList);
    }

    public interface OnStatusUpdatedListener {
        void onComplete(ArrayList<LeaveRequest> leaveRequests);
    }

    public interface OnLeaveRequestsOfTeamFetchedListener {
        void onComplete(List<LeaveRequest> leaveRequests);
    }
}

