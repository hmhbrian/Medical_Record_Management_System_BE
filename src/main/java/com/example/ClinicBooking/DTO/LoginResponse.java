package com.example.ClinicBooking.DTO;

import lombok.Data;

@Data
public class LoginResponse {
    private String token;
    private String role;
    private String position;
    private int userId;
    private String fullname;
}
