package com.example.clinicbooking.DTO.ImagingTest;

import com.example.clinicbooking.DTO.Patient.PatientSummary;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class ImagingTestWaitingResponse {
    private Integer imagingTestId;
    private String imagingTestName;
    private LocalDateTime requestedDate;
    private String status;
    private String doctorInChargeName;
    private String specialty;
    private PatientSummary patient;
}
