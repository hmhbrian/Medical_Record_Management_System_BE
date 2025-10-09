package com.example.clinicbooking.DTO.Patient;

import com.example.clinicbooking.DTO.User.BaseUserRequest;
import lombok.Data;

@Data
public class PatientRequest extends BaseUserRequest {
    // patient
    public String medicalHistory;
    public String insuranceNumber;
}
