package com.example.clinicbooking.DTO.Patient;

import lombok.Data;

@Data
public class PatientRp {
    public Integer patientId;
    public String patientCode;
    public String fullName;
    public String phoneNumber;
    public String gender;
    public String dateOfBirth;
    public String email;
}
