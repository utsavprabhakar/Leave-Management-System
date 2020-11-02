package com.example.leavemanagement2;

import android.app.AppComponentFactory;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import com.example.leavemanagement2.helpers.SharedPrefManager;
import com.example.leavemanagement2.models.Employee;
import com.example.leavemanagement2.models.LeaveRequest;
import com.example.leavemanagement2.stores.LeaveRequestStore;

import com.alamkanak.weekview.WeekViewEvent;
import com.google.gson.Gson;


import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class Dashboard extends AppCompatActivity {
    CardView myProfileCard, leaveRequestCard, viewRequestCard, approveRequestCard, calenderCard;
    SharedPrefManager sharedPrefManager = SharedPrefManager.getInstance(this);
    LeaveRequestStore leaveRequestStore = LeaveRequestStore.getInstance();

    List<LeaveRequest> weekLeaveRequests;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);
        initViews();
        initListeners();
        initVariables();
    }

    private void initVariables() {
        weekLeaveRequests = new ArrayList<>();
    }

    private void initViews() {
        myProfileCard = (CardView)findViewById(R.id.profile_card);
        leaveRequestCard = (CardView)findViewById(R.id.leave_request_card);
        viewRequestCard = (CardView)findViewById(R.id.view_request_card);
        approveRequestCard = (CardView)findViewById(R.id.approve_request_card);
        calenderCard = (CardView)findViewById(R.id.calendar_card);
    }

    private void initListeners() {
        myProfileCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(Dashboard.this, "Coming soon", Toast.LENGTH_LONG).show();
            }
        });

        leaveRequestCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startNextActivity(RequestLeave.class);
            }
        });

        viewRequestCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startNextActivity(RequestViewActivity.class, "view");
            }
        });

        approveRequestCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startNextActivity(RequestViewActivity.class, "approve");
            }
        });

        calenderCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Employee currentEmployee = null;
                fetchSubordinatesLeaves(currentEmployee);
            }
        });
    }

    private void fetchSubordinatesLeaves(Employee currentEmployee) {
        if(sharedPrefManager.getCurrentEmployee() == null) {
            startNextActivity(LoginActivity.class);
            return;
        }
        currentEmployee = sharedPrefManager.getCurrentEmployee();
        leaveRequestStore.getLeaveRequestsOfTeam(currentEmployee.getEmployeeID(), new LeaveRequestStore.OnLeaveRequestsOfTeamFetchedListener() {
            @Override
            public void onComplete(List<LeaveRequest> leaveRequests) {
                if(leaveRequests !=null) {
                    weekLeaveRequests.addAll(leaveRequests);
                    startNextActivityWithExtra(CalendarActivity.class);
                } else {
                    Toast.makeText(Dashboard.this, "No team leaves scheduled", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void startNextActivityWithExtra(Class<CalendarActivity> calendarActivityClass) {
        Intent intent = new Intent(this, calendarActivityClass);
        ArrayList<String> list = new ArrayList<>();
        for(LeaveRequest leaveRequest: weekLeaveRequests) {
            list.add(new Gson().toJson(leaveRequest));
        }
        intent.putStringArrayListExtra("leaveRequests", list);
        startActivity(intent);
    }

    private void startNextActivity(Class nextActivity) {
        Intent intent = new Intent(this, nextActivity);
        startActivity(intent);
    }

    private void startNextActivity(Class nextActivity, String identifier) {
        Intent intent = new Intent(this, nextActivity);
        intent.putExtra("identifier", identifier);
        startActivity(intent);
    }
}
