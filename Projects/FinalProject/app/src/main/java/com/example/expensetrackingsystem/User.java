package com.example.expensetrackingsystem;

import lombok.Builder;
import lombok.Data;
import lombok.experimental.Accessors;

@Builder
@Data
public class User
{
    private String username;
    private String email;
    private String password;
    private String phone;
}
