package com.example.clinicbooking.DTO.Auth;

import lombok.Data;

@Data
public class LoginRequest {
    private String phoneNumber;
    private String password;
}
