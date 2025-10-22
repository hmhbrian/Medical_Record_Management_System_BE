package com.example.clinicbooking.DTO.MedicalRecord.DiagnosisData;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Icd10Response {
    private String code;
    private String name;
    private boolean isPrincipal; // TRUE nếu là chẩn đoán chính
}
