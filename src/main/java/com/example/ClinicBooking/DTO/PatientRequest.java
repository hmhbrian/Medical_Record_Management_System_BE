package com.example.ClinicBooking.DTO;

import lombok.Data;

import java.time.LocalDate;

@Data
public class PatientRequest extends BaseUserRequest {
    // patient
    public String medicalHistory;
    public String insuranceNumber;
}
