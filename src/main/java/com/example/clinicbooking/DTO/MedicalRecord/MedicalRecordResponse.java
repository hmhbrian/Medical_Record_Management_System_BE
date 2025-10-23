package com.example.clinicbooking.DTO.MedicalRecord;

import com.example.clinicbooking.DTO.Patient.PatientSummary;
import jakarta.persistence.criteria.CriteriaBuilder;
import lombok.Data;

import java.time.LocalDate;

@Data
public class MedicalRecordResponse {
    private Integer recordId;
    private String recordCode;
    private Integer appointmentId;
    private String initialSymptoms;
    private String diagnosis;
    private LocalDate visitDate;
    private int visitNumber;
    private String status;
    private PatientSummary patient;
}
