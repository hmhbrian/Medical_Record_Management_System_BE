package com.example.clinicbooking.DTO.Doctor;

import com.example.clinicbooking.DTO.User.BaseUserResponse;
import lombok.Data;

import java.time.LocalDate;

@Data
public class DoctorResponse extends BaseUserResponse {
    public Integer id;
    public String doctorcode;
    public Integer departmentId;
    public String department;
    public Integer positionId;
    public String position;
    public Integer specialtyId;
    public String specialty;
    public Integer experienceYears;
    public String certificationName;
    public String issuedBy;
    public LocalDate issueDate;
}
