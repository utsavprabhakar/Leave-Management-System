package com.example.leavemanagement2;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.leavemanagement2.helpers.SharedPrefManager;
import com.example.leavemanagement2.models.Employee;
import com.example.leavemanagement2.stores.EmployeeStore;

public class LoginActivity extends AppCompatActivity {

    TextView createTextView;
    TextView empl_idTextView;
    TextView empl_passwordTextView;
    Button btn_login;
    EmployeeStore employeeStore;

    SharedPrefManager sharedPrefManager;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        initViews();
        initListeners();
        initStores();
        initVariables();
        if(isLoggedIn()) {
            Intent intent = new Intent(this, Dashboard.class);
            startActivity(intent);
            finish();
        }
        createTextView=(TextView)findViewById(R.id.create);
        createTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(LoginActivity.this, SignupActivity.class);
                startActivity(intent);
            }
        });
    }

    private boolean isLoggedIn() {
        if(sharedPrefManager.getCurrentEmployee() != null) {
            return true;
        }
        return false;
    }

    private void initVariables() {
        sharedPrefManager = SharedPrefManager.getInstance(this);
    }

    private void initStores() {
        employeeStore = EmployeeStore.getInstance();
    }

    private void initViews() {
        createTextView=(TextView)findViewById(R.id.create);
        empl_idTextView = (TextView)findViewById(R.id.input_emp_id);
        empl_passwordTextView = (TextView)findViewById(R.id.input_password);
        btn_login = (Button)findViewById(R.id.btn_login);
    }

    private void initListeners() {
        createTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(LoginActivity.this, SignupActivity.class);
                startActivity(intent);
            }
        });

        btn_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(allFieldsFilled()){
                    validateUserData();
                }
                else
                    Toast.makeText(LoginActivity.this, "Fill all the fields", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void validateUserData() {
        employeeStore.getEmployeeByEmployeeID(empl_idTextView.getText().toString(), new EmployeeStore.OnEmployeeFetchedListener() {
            @Override
            public void onComplete(Employee employee) {
                if(employee == null || !employee.getPassword().equals(empl_passwordTextView.getText().toString())) {
                    Toast.makeText(LoginActivity.this, "Invalid username/password", Toast.LENGTH_SHORT).show();
                } else {
                    startDashBoardActivity();
                    setCurrentEmployeeInSharedPref(employee);
                    finish();
                }
            }
        });
    }

    private void setCurrentEmployeeInSharedPref(Employee employee) {
        sharedPrefManager.setCurrentEmployee(employee);
    }

    private void startDashBoardActivity() {
        Intent intent = new Intent(this, Dashboard.class);
        startActivity(intent);
    }

    private boolean allFieldsFilled() {
        boolean areAllFieldsFilled = true;
        if(empl_idTextView.getText().toString().length() == 0 || empl_passwordTextView.getText().toString().length() ==0) {
            areAllFieldsFilled = false;
        }
        return areAllFieldsFilled;
    }
}
