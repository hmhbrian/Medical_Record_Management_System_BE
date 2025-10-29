package com.example.clinicbooking.DTO.MedicalRecord.ServiceData;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class ImagingOrder {
    private Integer orderId; // ID trong bảng labtests
    private String imagingCode;
    private String imagingName;
    private LocalDateTime requestDate;
    private String status;
    private String assignedStaffName;
}
