package com.example.clinicbooking.DTO.MedicalRecord;

import lombok.Data;

@Data
public class MedicalRecordResponse {
    private int id;
    private String patientName;
    private int patientId;
    private String doctorName;
    private int doctorId;
    private int appointmentId;
    private String initialSymptoms;
    private String diagnosis;
    private String visitDate;
    private int visitNumber;
}
