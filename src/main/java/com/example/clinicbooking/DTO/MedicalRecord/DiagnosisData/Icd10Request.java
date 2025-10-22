package com.example.clinicbooking.DTO.MedicalRecord.DiagnosisData;

import lombok.Data;

@Data
public class Icd10Request {
    private Integer icd10CatalogId;
    private boolean isPrincipal; // TRUE nếu là chẩn đoán chính
}
