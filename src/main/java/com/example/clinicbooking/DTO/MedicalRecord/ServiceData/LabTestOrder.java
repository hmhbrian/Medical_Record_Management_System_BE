package com.example.clinicbooking.DTO.MedicalRecord.ServiceData;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class LabTestOrder {
    private Integer orderId; // ID trong bảng labtests
    private String testCode;
    private String testName;
    private LocalDateTime requestDate;
    private String status;
    private String assignedStaffName;
}
