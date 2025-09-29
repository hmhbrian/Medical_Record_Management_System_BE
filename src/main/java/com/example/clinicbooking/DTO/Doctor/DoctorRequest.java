package com.example.clinicbooking.DTO.Doctor;

import com.example.clinicbooking.DTO.User.BaseUserRequest;
import lombok.Data;

import java.time.LocalDate;

@Data
public class DoctorRequest extends BaseUserRequest {
    // Staff
    public int departmentId;
    public int positionId;
    // Doctor
    public int specialtyId;
    public int experienceYears;
    public String certificationName;
    public String issuedBy;
    public LocalDate issueDate;
}
