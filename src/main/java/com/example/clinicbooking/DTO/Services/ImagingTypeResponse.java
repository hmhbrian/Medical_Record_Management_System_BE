package com.example.clinicbooking.DTO.Services;

import lombok.Data;

@Data
public class ImagingTypeResponse {
    private int id;
    private String imagingCode;
    private String imagingName;
    private double price;
    private String description;
}
