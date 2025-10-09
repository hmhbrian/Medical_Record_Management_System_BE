package com.example.clinicbooking.DTO.MedicalService;

import lombok.Data;

@Data
public class MedicalServiceRequest {
    private String Name;
    private double price;
    private String description;
    private int department_id;
    private int status;
    private EMedicalService medicalService;
}
