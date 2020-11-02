package com.example.leavemanagement2;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.os.Bundle;
import android.text.InputType;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.leavemanagement2.helpers.SharedPrefManager;
import com.example.leavemanagement2.helpers.Utils;
import com.example.leavemanagement2.models.LeaveRequest;
import com.example.leavemanagement2.models.RequestStatus;
import com.example.leavemanagement2.stores.LeaveRequestStore;
import com.google.firebase.Timestamp;

import java.util.ArrayList;
import java.util.Calendar;

public class RequestLeave extends AppCompatActivity {
    DatePickerDialog datepicker;
    EditText eText1,etext2;
    int startDay, startMonth, startYear;
    int endDay, endMonth, endYear;
    Button btApply;

    LeaveRequestStore leaveRequestStore;
    SharedPrefManager sharedPrefManager;

    EditText etRmployeeID, etDescription, etReason;
    //  TextView tvw;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_request_leave);
        initViews();
        initStores();
        // tvw=(TextView)findViewById(R.id.textView1);

    }

    private void initStores() {
        leaveRequestStore = LeaveRequestStore.getInstance();
        sharedPrefManager = SharedPrefManager.getInstance(this);
    }

    private void initViews() {
        etRmployeeID = (EditText)findViewById(R.id.input_emp_id);
        etDescription = (EditText)findViewById(R.id.input_desc);
        etReason = (EditText)findViewById(R.id.input_reason);
        btApply = (Button)findViewById(R.id.btn_apply);
        eText1=(EditText) findViewById(R.id.start);
        eText1.setInputType(InputType.TYPE_NULL);
        etext2=(EditText) findViewById(R.id.end);
        etext2.setInputType(InputType.TYPE_NULL);
        eText1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Calendar cldr = Calendar.getInstance();
                startDay = cldr.get(Calendar.DAY_OF_MONTH);
                startMonth = cldr.get(Calendar.MONTH);
                startYear = cldr.get(Calendar.YEAR);
                // date picker dialog
                datepicker = new DatePickerDialog(RequestLeave.this,
                        new DatePickerDialog.OnDateSetListener() {
                            @SuppressLint("SetTextI18n")
                            @Override
                            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                                eText1.setText(dayOfMonth + "/" + (monthOfYear + 1) + "/" + year);
                                startDay = dayOfMonth;
                                startMonth = monthOfYear + 1;
                                startYear = year;
                            }
                        }, startYear, startMonth, startDay);
                datepicker.show();
            }
        });

        etext2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final Calendar cldr = Calendar.getInstance();
                endDay = cldr.get(Calendar.DAY_OF_MONTH);
                endMonth = cldr.get(Calendar.MONTH);
                endYear = cldr.get(Calendar.YEAR);
                // date picker dialog
                datepicker = new DatePickerDialog(RequestLeave.this,
                        new DatePickerDialog.OnDateSetListener() {
                            @SuppressLint("SetTextI18n")
                            @Override
                            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                                etext2.setText(dayOfMonth + "/" + (monthOfYear + 1) + "/" + year);
                                endDay = dayOfMonth;
                                endMonth = monthOfYear + 1;
                                endYear = year;
                            }
                        }, endYear, endMonth, endDay);
                datepicker.show();
            }
        });

        btApply.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //TODO validate
                LeaveRequest leaveRequest = new LeaveRequest();
                leaveRequest.setRequestStatus(RequestStatus.PENDING);
                leaveRequest.setEmployeeID(sharedPrefManager.getCurrentEmployee().getEmployeeID());
                leaveRequest.setEmployeeName(sharedPrefManager.getCurrentEmployee().getName());
                leaveRequest.setDescription(etDescription.getText().toString());
                leaveRequest.setReason(etReason.getText().toString());
                leaveRequest.setStartDate(new Timestamp(Utils.parseDate((Utils.convertDateNumbersToString(startYear, startMonth, startDay)))));
                leaveRequest.setEndDate(new Timestamp(Utils.parseDate((Utils.convertDateNumbersToString(endYear, endMonth, endDay)))));
                ArrayList<String> list = new ArrayList<>(sharedPrefManager.getCurrentEmployee().getManagerIDs());
                leaveRequest.setManagerIDs(list);
                leaveRequest.setCreatedAt(System.currentTimeMillis());
                leaveRequestStore.create(leaveRequest, new LeaveRequestStore.OnCreateLeaveRequestListener() {
                    @Override
                    public void onComplete(LeaveRequest leaveRequest) {
                        Toast.makeText(RequestLeave.this, "Leave request succesfully created", Toast.LENGTH_SHORT).show();
                        finish();
                    }
                });
            }
        });

    }
}
