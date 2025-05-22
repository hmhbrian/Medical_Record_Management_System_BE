package com.example.ClinicBooking.DTO;

import lombok.Data;

import java.time.LocalDate;

@Data
public class PatientResponse extends BaseUserResponse {
    public int id;
    public String patientcode;
    public String medicalHistory;
    public String insuranceNumber;
}
