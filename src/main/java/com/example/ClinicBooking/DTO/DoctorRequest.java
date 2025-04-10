package com.example.ClinicBooking.DTO;

import lombok.Data;

import java.time.LocalDate;

@Data
public class DoctorRequest {
    // user
    public String fullname;
    public String email;
    public String phoneNumber;
    public LocalDate dateOfBirth;
    public int gender;
    public String address;
    public String password;
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
