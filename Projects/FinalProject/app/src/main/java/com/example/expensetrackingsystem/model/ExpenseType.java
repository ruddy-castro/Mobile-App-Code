package com.example.expensetrackingsystem.model;

import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class ExpenseType {
    private final String email;
    private final String itemName;
}
