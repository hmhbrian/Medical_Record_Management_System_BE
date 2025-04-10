package com.example.ClinicBooking.DTO;

import lombok.Data;

import java.time.LocalDate;

@Data
public class PatientRequest {
    // user
    public String fullname;
    public String email;
    public String phoneNumber;
    public LocalDate dateOfBirth;
    public int gender;
    public String address;
    public String password;

    // patient
    public String medicalHistory;
    public String insuranceNumber;
}
