package com.example.clinicbooking.DTO.User;

import lombok.Data;

import java.time.LocalDate;

@Data
public class BaseUserResponse {
    public String fullname;
    public String email;
    public String phoneNumber;
    public LocalDate dateOfBirth;
    public int gender;
    public String address;
    public String avatar_url;
}
