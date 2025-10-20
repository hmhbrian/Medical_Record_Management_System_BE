package com.example.clinicbooking.DTO.MedicalRecord;

import com.example.clinicbooking.DTO.Patient.PatientRp;
import lombok.Data;

@Data
public class MedicalRecordResponse {
    private int recordId;
    private int PatientId;
    private String PatientName;
    private String doctorName;
    private int doctorId;
    private int appointmentId;
    private String initialSymptoms;
    private String diagnosis;
    private String visitDate;
    private int visitNumber;
}
