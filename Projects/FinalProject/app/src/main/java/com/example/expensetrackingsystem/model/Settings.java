package com.example.expensetrackingsystem.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * A class that represents the setting object.
 */
@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Settings {
    private String id;
    private double annualIncome;
    private double maxDailyExpense;
    private double savingGoal;
}
