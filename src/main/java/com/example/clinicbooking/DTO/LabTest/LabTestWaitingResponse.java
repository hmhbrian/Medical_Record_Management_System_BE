package com.example.clinicbooking.DTO.LabTest;

import com.example.clinicbooking.DTO.Patient.PatientSummary;
import lombok.Data;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
public class LabTestWaitingResponse {
    private Integer labTestId;
    private String labTestName;
    private LocalDateTime requestedDate;
    private String status;
    private String doctorInChargeName;
    private PatientSummary patient;
}
