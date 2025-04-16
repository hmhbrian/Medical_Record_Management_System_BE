package com.example.ClinicBooking.DTO;

import lombok.Data;

import java.time.LocalDate;

@Data
public class DoctorResponse extends BaseUserResponse {
    public String doctorcode;
    public String department;
    public String specialty;
    public int experienceYears;
    public String certificationName;
    public String issuedBy;
    public LocalDate issueDate;
}
