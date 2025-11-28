package com.example.clinicbooking.DTO.Prescription;

import com.example.clinicbooking.DTO.Prescription.Detail.PrescriptionDetailsResponse;
import lombok.Data;

import java.util.List;

@Data
public class PrescriptionResponseDTO {
    private Integer totalDays; // Tổng ngày dùng
    private String status;
    private String pharmacistName;
    private String pharmacistCode;
    private List<PrescriptionDetailsResponse> details;
}
