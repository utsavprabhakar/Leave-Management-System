package com.example.leavemanagement2.stores;

import androidx.annotation.NonNull;

import com.example.leavemanagement2.helpers.Utils;
import com.example.leavemanagement2.models.Employee;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.WriteBatch;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

public class EmployeeStore implements OnFailureListener {
    private static EmployeeStore instance = null;

    public static synchronized  EmployeeStore getInstance() {
        if(instance == null){
            instance = new EmployeeStore();
        }
        return instance;
    }

    private EmployeeStore() {
    }

    public void create(Employee employee, OnCreateListener onCreateListener) {
        employee.setId(UUID.randomUUID().toString());
        employee.save(o -> onCreateListener.onComplete(employee), this);
    }

    public void createOrUpdate(List<Employee> employees, boolean create, OnEmployeesCreatedListener onEmployeesCreatedListener) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        WriteBatch batch = db.batch();
        for(Employee employee : employees){
            if(create)
                employee.setId(UUID.randomUUID().toString());
            HashMap<String, Object> map = new HashMap<>();
            map.put("employeeID", employee.getEmployeeID());
            map.put("managerIDs", employee.getManagerIDs());
            map.put("teamType", employee.getTeamType());
            map.put("subTeamType", employee.getSubTeamType());
            map.put("name", employee.getName());
            map.put("password", employee.getPassword());
            map.put("id", employee.getId());
            batch.set(createEmployeeDoc(employee.getId()), map);
        }

        batch.commit().addOnSuccessListener(aVoid -> {
            if (onEmployeesCreatedListener != null) {
                onEmployeesCreatedListener.onComplete(employees);
            }
        }).addOnFailureListener(this);
    }

    private DocumentReference createEmployeeDoc(String docId) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        return db.collection("users")
                .document(docId);
    }

    public void update(Employee employee, OnUpdateListener onUpdateListener) {
        employee.update(o -> onUpdateListener.onComplete(employee), this);
    }

    //TODO try
    public void getEmployeeByEmployeeID(String employeeID, OnEmployeeFetchedListener onEmployeeFetchedListener) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("users")
                .whereEqualTo("employeeID", employeeID)
                .limit(1)
                .get()
                .addOnFailureListener(this)
                .addOnSuccessListener(querySnapshot -> {
                    Employee employee = null;
                    if(!Utils.isEmpty(querySnapshot)) {
                        DocumentSnapshot documentSnapshot = querySnapshot.getDocuments().get(0);
                        employee = documentSnapshot.toObject(Employee.class);
                    }
                    onEmployeeFetchedListener.onComplete(employee);
                });
    }

    //TODO try
    public void getEmployeesByEmployeeIDs(ArrayList<String> employeeIDs, OnEmployeesFetchedListener onEmployeesFetchedListener) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("users")
                .whereIn("employeeID", employeeIDs)
                .get()
                .addOnFailureListener(this)
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    List<Employee> employees = null;
                    if(!queryDocumentSnapshots.isEmpty()) {
                        employees = queryDocumentSnapshots.toObjects(Employee.class);
                    }
                    onEmployeesFetchedListener.onComplete(employees);
                });
    }

    //TODO try
    public void getEmployeesUnderManager(String managerID, OnEmployeesUnderManagerListener onEmployeesUnderManagerListener) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("users")
                .whereArrayContains("managerIDs", managerID)
                .get()
                .addOnFailureListener(this)
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    List<Employee> employees = null;
                    if(!queryDocumentSnapshots.isEmpty()) {
                        employees = queryDocumentSnapshots.toObjects(Employee.class);
                    }
                    onEmployeesUnderManagerListener.onComplete(employees);
                });
    }

    //Listeners
    public interface OnEmployeeFetchedListener {
        void onComplete(Employee employee);
    }

    public interface OnEmployeesFetchedListener {
        void onComplete(List<Employee> employees);
    }

    public interface OnEmployeesUnderManagerListener {
        void onComplete(List<Employee> employees);
    }

    public interface OnCreateListener {
        void onComplete(Employee employee);
    }

    public interface OnUpdateListener {
        void onComplete(Employee employee);
    }

    public interface OnEmployeesCreatedListener {
        void onComplete(List<Employee> employees);
    }

    @Override
    public void onFailure(@NonNull Exception e) {
        e.printStackTrace();
    }
}

