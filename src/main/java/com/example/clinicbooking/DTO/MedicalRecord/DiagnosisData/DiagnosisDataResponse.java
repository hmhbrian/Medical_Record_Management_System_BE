package com.example.clinicbooking.DTO.MedicalRecord.DiagnosisData;

import com.example.clinicbooking.DTO.Patient.PatientSummary;
import lombok.Data;

import java.util.List;

@Data
public class DiagnosisDataResponse {
    // Thông tin cơ bản (Hiển thị)
    private Integer recordId;
    private Integer patientId;
    private PatientSummary patient;
    private String medicalHistory;
    private String initialSymptoms;

    private String diagnosis;
    private String notes;

    // Dịch vụ Khám đã chỉ định (nếu có, VD: Khám chuyên khoa)
    private Integer examinationServiceId;
    private String examinationServiceName;

    private List<Icd10Response> icd10Diagnoses;
}
