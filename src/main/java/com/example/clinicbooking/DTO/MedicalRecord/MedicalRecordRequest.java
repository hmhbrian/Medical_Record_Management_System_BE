package com.example.clinicbooking.DTO.MedicalRecord;

import lombok.Data;

@Data
public class MedicalRecordRequest {
    private Integer patientId;
    private Integer doctorId;
    private Integer appointmentId;
    private String initialSymptoms;//được bác sĩ hoặc nhân viên y tế ghi nhận ban đầu
}
