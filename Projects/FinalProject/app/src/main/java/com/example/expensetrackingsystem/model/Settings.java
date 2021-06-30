package com.example.expensetrackingsystem.model;

import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class Settings {
    private final double annualIncome;
    private final double maxDailyExpense;
    private final double savingGoal;
}
