package com.example.clinicbooking.DTO.MedicalRecord.Doctor;

import com.example.clinicbooking.DTO.Patient.PatientSummary;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class MedicalRecordResponse {
    private Integer recordId;
    private String recordCode;
    private Integer appointmentId;
    private String initialSymptoms;
    private String diagnosis;
    private LocalDateTime visitDate;
    private int visitNumber;
    private String status;
    private PatientSummary patient;
}
