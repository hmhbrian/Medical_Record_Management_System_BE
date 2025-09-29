package com.example.clinicbooking.DTO.Doctor;

import com.example.clinicbooking.DTO.User.BaseUserResponse;
import lombok.Data;

import java.time.LocalDate;

@Data
public class DoctorResponse extends BaseUserResponse {
    public int id;
    public String doctorcode;
    public int departmentId;
    public String department;
    public int positionId;
    public String position;
    public int specialtyId;
    public String specialty;
    public int experienceYears;
    public String certificationName;
    public String issuedBy;
    public LocalDate issueDate;
}
