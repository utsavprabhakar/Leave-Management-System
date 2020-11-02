package com.example.leavemanagement2;

import android.content.Intent;
import android.graphics.RectF;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.util.TypedValue;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import com.alamkanak.weekview.DateTimeInterpreter;
import com.alamkanak.weekview.MonthLoader;
import com.alamkanak.weekview.WeekView;
import com.alamkanak.weekview.WeekViewEvent;
import com.example.leavemanagement2.helpers.SharedPrefManager;
import com.example.leavemanagement2.models.Employee;
import com.example.leavemanagement2.models.LeaveRequest;
import com.example.leavemanagement2.stores.LeaveRequestStore;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.gson.Gson;

import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;

import static com.example.leavemanagement2.helpers.Utils.getEventTitle;

public class CalendarActivity extends AppCompatActivity implements WeekView.EventClickListener, MonthLoader.MonthChangeListener, WeekView.EventLongPressListener, WeekView.EmptyViewLongPressListener {

    private static final String TAG = RequestViewActivity.class.toString();
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    private static final int TYPE_DAY_VIEW = 1;
    private static final int TYPE_THREE_DAY_VIEW = 2;
    private static final int TYPE_WEEK_VIEW = 3;
    private int mWeekViewType = TYPE_THREE_DAY_VIEW;
    private WeekView mWeekView;

    LeaveRequestStore leaveRequestStore;
    SharedPrefManager sharedPrefManager;
    Employee currentEmployee;
    ArrayList<LeaveRequest> leaveRequests;
    static int weekViewEventId = 0;
    HashSet<String> leaveRequestIdString;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calendar);
        initVariables();
        initViews();
        initStores();
        processInput();
    }

    private void initStores() {
        leaveRequestStore = LeaveRequestStore.getInstance();
        sharedPrefManager = SharedPrefManager.getInstance(this);
    }

    private void initViews() {
        mWeekView = (WeekView)findViewById(R.id.weekView);

        mWeekView.setOnEventClickListener(this);

        mWeekView.setMonthChangeListener(this);

        mWeekView.setEventLongPressListener(this);
    }

    private void initVariables() {
        leaveRequests = new ArrayList<>();
        leaveRequestIdString = new HashSet<>();
    }

    private void processInput() {
        ArrayList<String> stringLeaveRequests = new ArrayList<>();
        stringLeaveRequests = getIntent().getStringArrayListExtra("leaveRequests");
        for(String str: stringLeaveRequests) {
            leaveRequests.add(new Gson().fromJson(str, LeaveRequest.class));
        }
        setupDateTimeInterpreter(false);
    }

    private void startNextActivity(Class loginActivityClass) {
        Intent intent = new Intent(this, loginActivityClass);
        startActivity(intent);
        finish();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        int id = item.getItemId();
        setupDateTimeInterpreter(id == R.id.action_week_view);
        switch (id){
            case R.id.action_today:
                mWeekView.goToToday();
                return true;
            case R.id.action_day_view:
                if (mWeekViewType != TYPE_DAY_VIEW) {
                    item.setChecked(!item.isChecked());
                    mWeekViewType = TYPE_DAY_VIEW;
                    mWeekView.setNumberOfVisibleDays(1);


                    mWeekView.setColumnGap((int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 8, getResources().getDisplayMetrics()));
                    mWeekView.setTextSize((int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, 12, getResources().getDisplayMetrics()));
                    mWeekView.setEventTextSize((int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, 12, getResources().getDisplayMetrics()));
                }
                return true;
            case R.id.action_three_day_view:
                if (mWeekViewType != TYPE_THREE_DAY_VIEW) {
                    item.setChecked(!item.isChecked());
                    mWeekViewType = TYPE_THREE_DAY_VIEW;
                    mWeekView.setNumberOfVisibleDays(3);


                    mWeekView.setColumnGap((int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 8, getResources().getDisplayMetrics()));
                    mWeekView.setTextSize((int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, 12, getResources().getDisplayMetrics()));
                    mWeekView.setEventTextSize((int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, 12, getResources().getDisplayMetrics()));
                }
                return true;
            case R.id.action_week_view:
                if (mWeekViewType != TYPE_WEEK_VIEW) {
                    item.setChecked(!item.isChecked());
                    mWeekViewType = TYPE_WEEK_VIEW;
                    mWeekView.setNumberOfVisibleDays(7);

                    mWeekView.setColumnGap((int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 2, getResources().getDisplayMetrics()));
                    mWeekView.setTextSize((int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, 10, getResources().getDisplayMetrics()));
                    mWeekView.setEventTextSize((int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, 10, getResources().getDisplayMetrics()));
                }
                return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void setupDateTimeInterpreter(final boolean shortDate) {
        mWeekView.setDateTimeInterpreter(new DateTimeInterpreter() {
            @Override
            public String interpretDate(Calendar date) {
                SimpleDateFormat weekdayNameFormat = new SimpleDateFormat("EEE", Locale.getDefault());
                String weekday = weekdayNameFormat.format(date.getTime());
                SimpleDateFormat format = new SimpleDateFormat(" M/d", Locale.getDefault());

                if (shortDate)
                    weekday = String.valueOf(weekday.charAt(0));
                return weekday.toUpperCase() + format.format(date.getTime());
            }

            @Override
            public String interpretTime(int hour) {
                return hour > 11 ? (hour - 12) + " PM" : (hour == 0 ? "12 AM" : hour + " AM");
            }
        });
    }

    @Override
    public List<? extends WeekViewEvent> onMonthChange(int newYear, int newMonth) {
        List<WeekViewEvent> weekViewEvents = new ArrayList<>();

        Log.d("Utsav", "onMonthChange called");
        for(LeaveRequest leaveRequest: leaveRequests) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                if(!leaveRequestIdString.contains(leaveRequest.getId())){
                    Log.d("Utsav", "inside for ");
                    weekViewEvents.add(createEvent(leaveRequest, newYear, newMonth));
                    leaveRequestIdString.add(leaveRequest.getId());
                }
            }
        }
        return weekViewEvents;
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private WeekViewEvent createEvent(LeaveRequest leaveRequest, int newYear, int newMonth) {

        Calendar startTime = Calendar.getInstance();
        Date startDate = leaveRequest.getStartDate().toDate();
        LocalDate localStartDate = startDate.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
        startTime.set(Calendar.HOUR_OF_DAY, 3);
        startTime.set(Calendar.MINUTE, 0);
        startTime.set(Calendar.MONTH, localStartDate.getMonthValue()-1);
        startTime.set(Calendar.YEAR, localStartDate.getYear());
        startTime.set(Calendar.DAY_OF_MONTH, localStartDate.getDayOfMonth());
        Calendar endTime = (Calendar) startTime.clone();

        Date endDate = leaveRequest.getEndDate().toDate();
        LocalDate localEndDate = endDate.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
        endTime.set(Calendar.MONTH, localEndDate.getMonthValue()-1);
        endTime.set(Calendar.YEAR, localEndDate.getYear());
        endTime.set(Calendar.DAY_OF_MONTH, localEndDate.getDayOfMonth());
        endTime.add(Calendar.HOUR, 1);
//        Calendar startTime = Calendar.getInstance();
//        startTime.set(Calendar.HOUR_OF_DAY, 3);
//        startTime.set(Calendar.MINUTE, 0);
//        startTime.set(Calendar.MONTH, newMonth-1);
//        startTime.set(Calendar.YEAR, newYear);
//        Calendar endTime = (Calendar) startTime.clone();
//        endTime.add(Calendar.HOUR, 1);
//        endTime.set(Calendar.MONTH, newMonth-1);
        return new WeekViewEvent(weekViewEventId++, getEventTitle(leaveRequest), startTime, endTime);
    }

    @Override
    public void onEmptyViewLongPress(Calendar time) {
//        Toast.makeText(this, "Empty view long pressed: " + getEventTitle(time), Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onEventClick(WeekViewEvent event, RectF eventRect) {
        Toast.makeText(this, "Clicked " + event.getName(), Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onEventLongPress(WeekViewEvent event, RectF eventRect) {
        Toast.makeText(this, "Long pressed event: " + event.getName(), Toast.LENGTH_SHORT).show();
    }
}