package com.example.ClinicBooking.DTO;

import lombok.Data;

@Data
public class LoginResponse {
    private String token;
    private int role;
    private String fullname;
}
