package com.example.clinicbooking.DTO.MedicalService;

import lombok.Data;

@Data
public class MedicalServiceResponse {
    private int id;
    private String Code;
    private String Name;
    private double price;
    private String description;
    private String departmentName;
    private String medicalService;
    private int status; // 1=active, 0=inactive
}
