package com.example.clinicbooking.DTO.MedicalRecord.DiagnosisData;

import lombok.Data;

import java.util.List;

@Data
public class DiagnosisUpdateRequest {
    private String notes; // Ghi chú khám lâm sàng
    private String diagnosis; // Chẩn đoán văn bản

    // Dịch vụ Khám đã chỉ định (nếu có)
    private Integer examinationServiceId;

    // Danh sách đầy đủ các mã ICD-10 được chọn (Bao gồm cả mã chính và mã phụ)
    private List<Icd10_DiagnosisRequest> icd10List;
}
