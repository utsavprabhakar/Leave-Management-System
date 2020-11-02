package com.example.leavemanagement2.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;

import com.example.leavemanagement2.helpers.SharedPrefManager;
import com.example.leavemanagement2.helpers.Utils;
import com.example.leavemanagement2.listeners.OnLeaveRequestItemClick;
import com.example.leavemanagement2.models.LeaveRequest;
import com.example.leavemanagement2.models.RequestStatus;

import com.example.leavemanagement2.R;

import java.util.List;

public class LeaveRequestArrayAdapter extends ArrayAdapter<LeaveRequest> {
    private List<LeaveRequest> leaveRequestList;
    OnLeaveRequestItemClick onLeaveRequestItemClick;
    SharedPrefManager sharedPrefManager = SharedPrefManager.getInstance(getContext());
    public LeaveRequestArrayAdapter(Context paramContext, List<LeaveRequest> paramList, OnLeaveRequestItemClick onLeaveRequestItemClick) {
        super(paramContext, 0, paramList);
        this.leaveRequestList = paramList;
        this.onLeaveRequestItemClick = onLeaveRequestItemClick;
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        LeaveRequest leaveRequest = getItem(position);
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.item_leave_request, parent, false);
        }
        //fill details in textboxes
        populateView(convertView, position, leaveRequest);
        return convertView;
    }

    private void populateView(View convertView, int position, final LeaveRequest leaveRequest) {
        TextView nameTextView = (TextView) convertView.findViewById(R.id.tv_appTitle);
        TextView descriptionTextView = (TextView) convertView.findViewById(R.id.tv_appSubtitle);
        TextView dateTextView = (TextView) convertView.findViewById(R.id.tv_date);
        Button approveButton = (Button) convertView.findViewById(R.id.bt_approve);
        Button rejectButton = (Button) convertView.findViewById(R.id.bt_reject);
        Button removeButton = (Button) convertView.findViewById(R.id.bt_remove);

        nameTextView.setText(leaveRequest.getEmployeeName());
        descriptionTextView.setText(leaveRequest.getDescription());
        dateTextView.setText((Utils.parseDateToReadable(leaveRequest.getStartDate().toDate().toString()) + " to " + Utils.parseDateToReadable(leaveRequest.getEndDate().toDate().toString())));
        if(leaveRequest.getRequestStatus().equals(RequestStatus.APPROVED)) {
            convertView.findViewById(R.id.container).setBackground(getContext().getResources().getDrawable(R.color.background_green));
        } else if(leaveRequest.getRequestStatus().equals(RequestStatus.REJECTED)) {
            convertView.findViewById(R.id.container).setBackground(getContext().getResources().getDrawable(R.color.die));
        } else {
            //do nothing
            convertView.findViewById(R.id.container).setBackground(getContext().getResources().getDrawable(R.color.background));
        }

        if(sharedPrefManager.getString(getContext(), "identifier").equals("view")) {
            approveButton.setVisibility(View.GONE);
            rejectButton.setVisibility(View.GONE);
            removeButton.setVisibility(View.GONE);
        }

        approveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onLeaveRequestItemClick.onApproveClick(position, leaveRequest);
            }
        });

        rejectButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onLeaveRequestItemClick.onRejectClick(position, leaveRequest);
            }
        });

        removeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onLeaveRequestItemClick.onRemoveClick(position, leaveRequest);
            }
        });
    }
}

