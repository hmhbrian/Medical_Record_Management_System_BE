package com.example.clinicbooking.DTO.Prescription;

import lombok.Data;

import java.util.List;

@Data
public class PrescriptionResponse {
    private Integer prescriptionId;
    private Integer totalDays; // Tổng ngày dùng
    private String status;
    private boolean isSend;
    private List<PrescriptionDetailsResponse> details;
}
