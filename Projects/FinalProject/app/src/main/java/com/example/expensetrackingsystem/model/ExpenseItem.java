package com.example.expensetrackingsystem.model;

import java.sql.Timestamp;

import lombok.Builder;
import lombok.Data;
import lombok.experimental.Accessors;

@Builder
@Data
public class ExpenseItem {
    private float amount;
    private String email;
    private String expenseTypeId;
    private Timestamp timestamp;

    public void setTimestamp(com.google.firebase.Timestamp timestamp) {
        this.timestamp = new Timestamp(timestamp.getSeconds());
    }
}
