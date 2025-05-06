package com.example.ClinicBooking.DTO;

import lombok.Data;

@Data
public class LoginRequest {
    private String phoneNumber;
    private String password;
}
