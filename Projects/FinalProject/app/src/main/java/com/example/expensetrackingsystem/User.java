package com.example.expensetrackingsystem;

import lombok.Builder;
import lombok.Data;
import lombok.experimental.Accessors;

@Builder
@Data
@Accessors(fluent = true)
public class User {
    private final String username;
    private final String email;
    private final String password;
    private final String phone;
}
