package com.example.expensetrackingsystem.model;

import lombok.Builder;
import lombok.Data;

/**
 * A class that represents the user object.
 */
@Builder
@Data
public class User {
    private final String username;
    private final String email;
    private final String password;
    private final String phone;
}
