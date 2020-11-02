package com.example.leavemanagement2.listeners;

import com.example.leavemanagement2.models.LeaveRequest;

public interface OnLeaveRequestItemClick {
    void onApproveClick(int position, LeaveRequest leaveRequest);
    void onRejectClick(int position, LeaveRequest leaveRequest);
    void onRemoveClick(int position, LeaveRequest leaveRequest);
}
