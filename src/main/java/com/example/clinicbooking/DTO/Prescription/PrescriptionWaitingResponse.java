package com.example.clinicbooking.DTO.Prescription;

import com.example.clinicbooking.DTO.Patient.PatientSummary;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class PrescriptionWaitingResponse {
    private Integer prescriptionId;
    private String prescriptionCode;
    private String recordCode;
    private LocalDateTime requestedDate;
    private String status;
    private String doctorInChargeName;
    private PatientSummary patient;
}
