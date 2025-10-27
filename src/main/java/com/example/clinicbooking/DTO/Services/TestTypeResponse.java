package com.example.clinicbooking.DTO.Services;

import lombok.Data;

@Data
public class TestTypeResponse {
    private int id;
    private String testCode;
    private String testName;
    private double price;
    private String description;
}
