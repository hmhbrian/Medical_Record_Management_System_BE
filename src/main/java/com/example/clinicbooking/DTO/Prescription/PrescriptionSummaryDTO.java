package com.example.clinicbooking.DTO.Prescription;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class PrescriptionSummaryDTO {
    private Integer id;
    private String code;
    private LocalDateTime prescriptionDate;
    private LocalDateTime dispenseDate; // Có thể NULL nếu chưa cấp phát
    private String status;
    private String patientName;
    private String doctorName;
    private String doctorCode;
    private String specialty;
    private String pharmacistName; // Có thể NULL nếu chưa cấp phát
    private String pharmacistCode;
    private String recordCode;
}
