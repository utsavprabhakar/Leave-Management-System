package com.example.leavemanagement2;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.ListView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.leavemanagement2.adapters.LeaveRequestArrayAdapter;
import com.example.leavemanagement2.helpers.SharedPrefManager;
import com.example.leavemanagement2.helpers.Utils;
import com.example.leavemanagement2.listeners.OnLeaveRequestItemClick;
import com.example.leavemanagement2.models.Employee;
import com.example.leavemanagement2.models.LeaveRequest;
import com.example.leavemanagement2.models.RequestStatus;
import com.example.leavemanagement2.models.SubTeamType;
import com.example.leavemanagement2.models.TeamType;
import com.example.leavemanagement2.stores.EmployeeStore;
import com.example.leavemanagement2.stores.LeaveRequestStore;
import com.google.firebase.Timestamp;

import java.util.ArrayList;
import java.util.List;

public class RequestViewActivity extends AppCompatActivity implements OnLeaveRequestItemClick {
    private static final String TAG = RequestViewActivity.class.toString();

    //data stores
    EmployeeStore employeeStore;
    ListView listView;
    LeaveRequestStore leaveRequestStore;

    ArrayList<LeaveRequest> updatedLeaveRequests;
    String identifier;

    ArrayList<LeaveRequest> leaveRequests;
    Employee currentEmployee;

    SharedPrefManager sharedPrefManager;
    LeaveRequestArrayAdapter adapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_requestview);
        sharedPrefManager = SharedPrefManager.getInstance(this);

//        if(sharedPrefManager.getCurrentEmployee() == null) {
//            startNextActivity(LoginActivity.class);
//            Toast.makeText(this, "You have been logged out. Log in to continue", Toast.LENGTH_SHORT).show();
//        }
        initVariables();
        processInput();
        initStores();

        if(identifier.equals("approve")) {
            showLeaveRequestsForApproval();
        } else {
            showLeaveRequestOfCurrentEmployee();
        }
    }

    private void initStores() {
        leaveRequestStore = LeaveRequestStore.getInstance();
    }

    private void processInput() {
        identifier = getIntent().getStringExtra("identifier");
        sharedPrefManager.setString(this, "identifier", identifier);
    }

    private void initVariables() {
        employeeStore = EmployeeStore.getInstance();
        updatedLeaveRequests = new ArrayList<>();
        currentEmployee = sharedPrefManager.getCurrentEmployee();
        leaveRequests = new ArrayList<>();
        updatedLeaveRequests = new ArrayList<>();
    }

    @Override
    protected void onResume() {
        super.onResume();
//        createLeaveRequest();
//        getLeaveRequest();
//        ArrayList<LeaveRequest> leaveRequestArrayList = createSeedDataLeaveRequests(10);
//        ArrayList<Employee> employees = createSeedDataEmployee(10);
//        addEmployeesInDb(employees);
        //addLeaveRequestsInDb(leaveRequestArrayList);
//        LeaveRequestArrayAdapter adapter = new LeaveRequestArrayAdapter(this, leaveRequestArrayList, this);
//        listView = findViewById(R.id.list_view);
//        listView.setAdapter(adapter);
//        adapter.notifyDataSetChanged();
    }

    private void showLeaveRequestsForApproval() {
        leaveRequestStore.getLeaveRequestsForEmployeeID(currentEmployee.getEmployeeID(), new LeaveRequestStore.OnLeaveRequestsFetchedForEmployeeListener() {
            @Override
            public void onComplete(List<LeaveRequest> leaveRequestList) {
                if(leaveRequestList == null || leaveRequestList.isEmpty()) {
                    goToDashBoard();
                    return;
                }
                leaveRequests.addAll(leaveRequestList);
                updateView();
            }
        });
    }

    private void goToDashBoard() {
        startNextActivity(Dashboard.class);
        Toast.makeText(this, "No requests to show", Toast.LENGTH_LONG).show();
        this.finish();
    }

    private void showLeaveRequestOfCurrentEmployee() {
        leaveRequestStore.getLeaveRequestsOfEmployeeID(currentEmployee.getEmployeeID(), new LeaveRequestStore.OnLeaveRequestsFetchedOfEmployeeListener() {
            @Override
            public void onComplete(List<LeaveRequest> leaveRequestList) {
                if(leaveRequestList == null || leaveRequestList.isEmpty()) {
                    goToDashBoard();
                    return;
                }
                leaveRequests.addAll(leaveRequestList);
                updateView();
            }
        });
    }

    private void updateView() {
        adapter = new LeaveRequestArrayAdapter(this, leaveRequests, this);
        listView = findViewById(R.id.list_view);
        listView.setAdapter(adapter);
        adapter.notifyDataSetChanged();
    }

    private void addEmployeesInDb(ArrayList<Employee> employees) {
        EmployeeStore employeeStore= EmployeeStore.getInstance();
        employeeStore.createOrUpdate(employees, true, new EmployeeStore.OnEmployeesCreatedListener() {
            @Override
            public void onComplete(List<Employee> employees) {
                Toast.makeText(RequestViewActivity.this, "created "+ employees.size() + " employees ", Toast.LENGTH_LONG).show();
            }
        });
    }

    private ArrayList<Employee> createSeedDataEmployee(int num) {
        ArrayList<Employee> employees = new ArrayList<>();
        for(int i=1;i<=num;i++) {
            employees.add(createEmployee(String.valueOf(i+1), TeamType.SYSTEM, SubTeamType.SERVICES, "Empl name"));
        }
        return employees;
    }

    private void addLeaveRequestsInDb(ArrayList<LeaveRequest> leaveRequestArrayList) {
        leaveRequestStore = LeaveRequestStore.getInstance();
        leaveRequestStore.createOrUpdate(leaveRequestArrayList, true, new LeaveRequestStore.OnLeaveRequestsCreatedListener() {
            @Override
            public void onComplete(List<LeaveRequest> leaveRequestList) {
                Toast.makeText(RequestViewActivity.this, "created "+ leaveRequestArrayList.size() + "leave Requests", Toast.LENGTH_LONG).show();
            }
        });

    }

    private ArrayList<LeaveRequest> createSeedDataLeaveRequests(int num) {
        ArrayList<LeaveRequest> leaveRequests = new ArrayList<>();
        for(int i=1;i<=num;i++) {
            leaveRequests.add(createLeaveRequest(String.valueOf(i+1), "Random Description number = "+i, "2020-11-01", "2020-11-10"));
        }
        return leaveRequests;
    }


    private void getLeaveRequest() {
        LeaveRequestStore leaveRequestStore = LeaveRequestStore.getInstance();
        leaveRequestStore.getLeaveRequestsOfEmployeeID("1", new LeaveRequestStore.OnLeaveRequestsFetchedOfEmployeeListener() {
            @Override
            public void onComplete(List<LeaveRequest> leaveRequestList) {
                updateLeaveRequest(leaveRequestList.get(0));
            }
        });
    }

    private LeaveRequest createLeaveRequest(String employeeID, String description, String startDate, String endDate) {
        LeaveRequest leaveRequest = new LeaveRequest();
        leaveRequest.setDescription(description);
        leaveRequest.setReason(" I nedd leave please give me leave");
        leaveRequest.setEmployeeID(employeeID);
        leaveRequest.setEmployeeName("UTSAV");
        leaveRequest.setStartDate(new Timestamp(Utils.parseDate(startDate)));
        leaveRequest.setEndDate(new Timestamp(Utils.parseDate(endDate)));
        ArrayList<String> managerIDs = new ArrayList<>();
        managerIDs.add("10");
        managerIDs.add("11");
        leaveRequest.setManagerIDs(managerIDs);
        return leaveRequest;
        //updateLeaveRequest(leaveRequest);
    }

    private Employee createEmployee(String employeeID, TeamType teamType, SubTeamType subTeamType, String name) {
        Employee employee = new Employee();
        employee.setEmployeeID(employeeID);
        employee.setTeamType(teamType);
        employee.setSubTeamType(subTeamType);
        ArrayList<String> managerIDs = new ArrayList<>();
        managerIDs.add("10");
        managerIDs.add("11");
        employee.setManagerIDs(managerIDs);
        employee.setName(name);
        employee.setPassword("1234");
        return employee;
    }

    private void updateLeaveRequest(LeaveRequest leaveRequest) {
        leaveRequest.setApprovedAt(System.currentTimeMillis());
        LeaveRequestStore leaveRequestStore = LeaveRequestStore.getInstance();
        leaveRequestStore.update(leaveRequest, new LeaveRequestStore.OnLeaveRequestUpdatedListener() {
            @Override
            public void onComplete(LeaveRequest leaveRequest) {
                int x = 5;
                Log.d(TAG, "updated leaveRequest");
            }
        });
    }

    private void createLeaveRequest(LeaveRequest leaveRequest) {
        LeaveRequestStore leaveRequestStore = LeaveRequestStore.getInstance();
        leaveRequestStore.create(leaveRequest, new LeaveRequestStore.OnCreateLeaveRequestListener() {
            @Override
            public void onComplete(LeaveRequest leaveRequest) {
                Log.d(TAG, "created new leave");
            }
        });
    }

    private void startNextActivity(Class nextActivity) {
        Intent intent = new Intent(this, nextActivity);
        startActivity(intent);
        this.finish();
    }

    @Override
    protected void onPause() {
        super.onPause();
        updateLeaveRequestInDb();
    }

    private void updateLeaveRequestInDb() {
        leaveRequestStore.updateLeaveRequestsStatus(updatedLeaveRequests, new LeaveRequestStore.OnStatusUpdatedListener() {
            @Override
            public void onComplete(ArrayList<LeaveRequest> leaveRequests) {

            }
        });
    }

    @Override
    public void onApproveClick(int position, LeaveRequest leaveRequest) {
        leaveRequest.setRequestStatus(RequestStatus.APPROVED);
        leaveRequest.setApprovedAt(System.currentTimeMillis());
        updatedLeaveRequests.add(leaveRequest);
        adapter.notifyDataSetChanged();
        Log.d("TAG", "Approved");
    }

    @Override
    public void onRejectClick(int position, LeaveRequest leaveRequest) {
        leaveRequest.setRejectedAt(System.currentTimeMillis());
        leaveRequest.setRequestStatus(RequestStatus.REJECTED);
        updatedLeaveRequests.add(leaveRequest);
        adapter.notifyDataSetChanged();
        Log.d("TAG", "Reject");
    }

    @Override
    public void onRemoveClick(int position, LeaveRequest leaveRequest) {
        Log.d("TAG", "Remove");
    }
}
