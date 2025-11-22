package com.example.clinicbooking.DTO.MedicalRecord.DiagnosisData;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class Icd10Request {
    private Integer icd10CatalogId;
    @JsonProperty("isPrincipal")
    private boolean isPrincipal; // TRUE nếu là chẩn đoán chính
}
