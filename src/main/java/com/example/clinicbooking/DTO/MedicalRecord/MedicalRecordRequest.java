package com.example.clinicbooking.DTO.MedicalRecord;

import lombok.Data;

@Data
public class MedicalRecordRequest {
    private Integer patientId;
    private Integer doctorId;
    private Integer appointmentId;
    private String initialSymptoms;
    private String diagnosis;
}
