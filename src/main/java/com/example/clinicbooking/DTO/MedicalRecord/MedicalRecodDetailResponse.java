package com.example.clinicbooking.DTO.MedicalRecord;

import com.example.clinicbooking.DTO.MedicalRecord.DiagnosisData.Icd10Response;
import com.example.clinicbooking.DTO.MedicalRecord.ServiceData.ServiceOrderResponse;
import com.example.clinicbooking.DTO.Payment.DetailForAdmin.InvoiceSummaryResponse;
import com.example.clinicbooking.DTO.Prescription.PrescriptionResponseDTO;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
public class MedicalRecodDetailResponse {
    // Thông tin cơ bản
    private Integer recordId;
    private String patientName;
    private String patientCode;
    private LocalDateTime visitDate;
    // 1. Chẩn đoán
    private String diagnosisText;
    private List<Icd10Response> icd10Diagnoses; // Danh sách mã ICD-10 đã lưu
    // 2. Chỉ định & Kết quả
    private List<ServiceOrderResponse> serviceOrders; // Danh sách chung các chỉ định (XN, CĐHA)
    // 3. Đơn thuốc
    private PrescriptionResponseDTO prescription; // Chi tiết đơn thuốc (Nếu có)
    // 4. Hóa đơn
    private List<InvoiceSummaryResponse> invoices;
}
