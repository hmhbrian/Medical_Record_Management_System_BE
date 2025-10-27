package com.example.clinicbooking.DTO.Services;

import lombok.Data;

@Data
public class MedicalExaminationResponse {
    private int id;
    private String examinationCode;
    private String examinationName;
    private double price;
    private String description;
}
