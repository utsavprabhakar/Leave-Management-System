package com.example.leavemanagement2;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.leavemanagement2.helpers.SharedPrefManager;
import com.example.leavemanagement2.models.Employee;
import com.example.leavemanagement2.models.SubTeamType;
import com.example.leavemanagement2.models.TeamType;
import com.example.leavemanagement2.stores.EmployeeStore;

import java.util.ArrayList;

public class SignupActivity extends AppCompatActivity {

    Button sign_in;
    EditText text1,text2,text3,text4;
    Spinner spinner1, spinner2;
    EmployeeStore employeeStore;

    SharedPrefManager sharedPrefManager;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);
        initVariables();
        initViews();
        initStores();
        initListeners();
    }

    private void initVariables() {
        sharedPrefManager = SharedPrefManager.getInstance(this);
    }

    private void initStores() {
        employeeStore = EmployeeStore.getInstance();
    }

    private void initListeners() {
        sign_in.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if ((text1.getText().toString().length() <= 0) || (text2.getText().toString().length() <= 0) || (text3.getText().toString().length() <= 0) || (text4.getText().toString().length() <= 0))
                {
                    Toast.makeText(SignupActivity.this, "Fill all fields!", Toast.LENGTH_SHORT).show();
                    //I will use the toast later
                }
                else
                {
                    saveUserDetailsInDb();
                }
            }
        });
    }

    private void saveUserDetailsInDb() {
        Employee employee = createNewEmployee();
        employeeStore.create(employee, new EmployeeStore.OnCreateListener() {
            @Override
            public void onComplete(Employee employee) {
                Toast.makeText(SignupActivity.this, "Welcome "+ employee.getName(), Toast.LENGTH_LONG).show();
                saveEmployeeInSharedPref(employee);
                startNextActivity();
                finish();
            }
        });
    }

    private void saveEmployeeInSharedPref(Employee employee) {
        sharedPrefManager.setCurrentEmployee(employee);
    }

    private void startNextActivity() {
        Intent intent=new Intent(SignupActivity.this,Dashboard.class);
        startActivity(intent);
    }

    private Employee createNewEmployee() {
        Employee newEmployee = new Employee();
        newEmployee.setPassword(text4.getText().toString());
        newEmployee.setName(text2.getText().toString());
        ArrayList<String> managerIDs = new ArrayList<>();
        managerIDs.add(text3.getText().toString());
        newEmployee.setManagerIDs(managerIDs);
        newEmployee.setTeamType(TeamType.valueOf(spinner1.getSelectedItem().toString()));
        newEmployee.setSubTeamType(SubTeamType.valueOf(spinner2.getSelectedItem().toString()));
        newEmployee.setEmployeeID(text1.getText().toString());
        return newEmployee;
    }

    private void initViews() {
        text1=(EditText)findViewById(R.id.input_emp_id);
        text2=(EditText)findViewById(R.id.input_emp_name);
        text3=(EditText)findViewById(R.id.input_manager_id);
        text4=(EditText)findViewById(R.id.password);
        spinner1 = (Spinner)findViewById(R.id.spinner1);
        spinner2 = (Spinner)findViewById(R.id.spinner2);
        sign_in=(Button)findViewById(R.id.btn_signin);
    }
}