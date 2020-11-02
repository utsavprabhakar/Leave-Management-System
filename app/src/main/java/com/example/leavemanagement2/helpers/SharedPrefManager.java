package com.example.leavemanagement2.helpers;

import android.content.Context;
import android.content.SharedPreferences;

import com.example.leavemanagement2.BuildConfig;
import com.example.leavemanagement2.models.Employee;
import com.google.gson.Gson;

public class SharedPrefManager {
    private static final String SP_NAME = BuildConfig.APPLICATION_ID;
    private static final int ACCESS_MODE = Context.MODE_PRIVATE;

    private static SharedPrefManager sharedPrefManager;
    private SharedPreferences sharedPreferences;
    private Context context;
    private Gson gson;

    public static SharedPrefManager getInstance(Context context) {
        if (null == sharedPrefManager) {
            sharedPrefManager = new SharedPrefManager(context);
        }
        return sharedPrefManager;
    }

    public SharedPrefManager(Context context) {
        this.context = context;
        this.sharedPreferences = context.getSharedPreferences(SP_NAME, ACCESS_MODE);
        this.gson = new Gson();
    }

    public void setCurrentEmployee(Employee employee) {
        setString(context, "PREF_CURRENT_EMPLOYEE", gson.toJson(employee));
    }

    public Employee getCurrentEmployee() {
        return gson.fromJson(getString(context, "PREF_CURRENT_EMPLOYEE"), Employee.class);
    }

    public void setString(Context context, String key, String value) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(key, value);
        editor.apply();
    }

    public String getString(Context context, String key) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(SP_NAME, ACCESS_MODE);
        return sharedPreferences.getString(key, "");
    }

    public void setInteger(Context context, String key, int value) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(SP_NAME, ACCESS_MODE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putInt(key, value);
        editor.apply();
    }

    public int getInteger(Context context, String key) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(SP_NAME, ACCESS_MODE);
        return sharedPreferences.getInt(key, 0);
    }

    public void setLong(Context context, String key, long value) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(SP_NAME, ACCESS_MODE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putLong(key, value);
        editor.apply();
    }

    public long getLong(Context context, String key) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(SP_NAME, ACCESS_MODE);
        return sharedPreferences.getLong(key, 0);
    }

    public void setBoolean(Context context, String key, boolean value) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(SP_NAME, ACCESS_MODE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean(key, value);
        editor.apply();
    }

    public boolean getBoolean(Context context, String key) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(SP_NAME, ACCESS_MODE);
        return sharedPreferences.getBoolean(key, false);
    }

}