package com.example.leavemanagement2.helpers;

import com.example.leavemanagement2.models.LeaveRequest;
import com.google.firebase.firestore.QuerySnapshot;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Utils {

    public static boolean isEmpty(QuerySnapshot queryDocumentSnapshots) {
        return null == queryDocumentSnapshots || queryDocumentSnapshots.isEmpty();
    }

    public static Date parseDate(String date) {
        try {
            return new SimpleDateFormat("yyyy-MM-dd").parse(date);
        } catch (ParseException e) {
            return null;
        }
    }

    public static String parseDateToReadable(String date) {
        String []arr = date.trim().split(" ");
        return arr[1] + " " + arr[2] + " "+ arr[5];
    }

    public static Date parseStringToDate(String date) throws ParseException {
        Date date1=new SimpleDateFormat("dd/MM/yyyy").parse(date);
        return date1;
    }

    public static String convertDateNumbersToString(int year, int month, int day) {
        String dayString = (day/10 ==0) ? ("0"+day): (""+day);
        String monthString = (month/10 == 0) ? ("0"+month) : (""+month);
        String yearString = year+"";
        return yearString+"-"+monthString+"-"+dayString;
    }

    public static String getEventTitle(LeaveRequest leaveRequest) {
        return leaveRequest.getEmployeeName() + " (" + leaveRequest.getEmployeeID()+ ")";
    }
}
