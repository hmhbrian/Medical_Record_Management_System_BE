package com.example.clinicbooking.DTO.MedicalRecord;

import lombok.Data;
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
