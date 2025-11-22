package com.example.clinicbooking.DTO.Prescription.Detail;

import lombok.Data;

import java.util.List;

@Data
public class PrescriptionDetailsOfStaffRp {
    private Integer prescriptionId;
    private Integer totalDays; // Tổng ngày dùng
    private List<PrescriptionDetailsResponse> details;
}
