package com.example.expensetrackingsystem.model;

import com.google.firebase.Timestamp;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Expense {
    private Double amount;
    private Timestamp timestamp;
    private String expenseType;
    private String email;
}

