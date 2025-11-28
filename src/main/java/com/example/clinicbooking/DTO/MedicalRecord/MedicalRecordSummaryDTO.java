package com.example.clinicbooking.DTO.MedicalRecord;

import com.example.clinicbooking.DTO.Patient.PatientSummary;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class MedicalRecordSummaryDTO {
    private Integer recordId;
    private String recordCode; // Mã Hồ sơ
    private PatientSummary patient;
    private LocalDateTime visitDate;
    private Integer visitNumber;
    private String doctorName;
    private String doctorCode;
    private String specialty;
    private boolean hasLabTests;    // Có chỉ định xét nghiệm không?
    private boolean hasImagingTests; // Có chỉ định hình ảnh không?
    private boolean hasPrescription; // Có đơn thuốc không?
    private String currentStatus;
}
