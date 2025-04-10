package com.example.ClinicBooking.DTO;

import lombok.Data;

import java.time.LocalDate;

@Data
public class DoctorResponse {
    public String doctorcode;
    public String fullname;
    public String email;
    public String phoneNumber;
    public LocalDate dateOfBirth;
    public int gender;
    public String address;
    public String department;
    public String specialty;
    public int experienceYears;
    public String certificationName;
    public String issuedBy;
    public LocalDate issueDate;
}
