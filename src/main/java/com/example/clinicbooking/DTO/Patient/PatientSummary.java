package com.example.clinicbooking.DTO.Patient;

import lombok.Data;

import java.time.LocalDate;

@Data
public class PatientSummary {
    private String patientCode;
    private String fullName;
    private String phoneNumber;
    private LocalDate dateOfBirth;
}
