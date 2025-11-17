package com.example.clinicbooking.DTO.MedicalRecord;

import com.example.clinicbooking.DTO.Patient.PatientSummary;
import lombok.Data;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
public class MedicalRecordPatientResponse {
    private Integer recordId;
    private String recordCode;
    private Integer appointmentId;
    private String initialSymptoms;
    private String diagnosis;
    private LocalDateTime visitDate;
    private int visitNumber;
    private String status;
    private String DoctorName;
    private String DoctorSpecialty;
}
