package com.example.clinicbooking.DTO.MedicalService;

import lombok.Data;

@Data
public class MedicalServiceOverviewResponse {
    // Tổng quát
    private long totalServices;
    private long activeServices;
    private double averagePrice;

    // Theo từng danh mục
    private long examinationCount;
    private long labTestCount;
    private long imagingCount;
}
