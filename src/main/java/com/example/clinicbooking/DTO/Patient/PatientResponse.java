package com.example.clinicbooking.DTO.Patient;

import com.example.clinicbooking.DTO.User.BaseUserResponse;
import lombok.Data;

@Data
public class PatientResponse extends BaseUserResponse {
    public int id;
    public String patientCode;
    public String medicalHistory;
    public String insuranceNumber;
}
