package com.example.clinicbooking.DTO.MedicalRecord;

import com.example.clinicbooking.DTO.MedicalRecord.DiagnosisData.Icd10Response;
import com.example.clinicbooking.DTO.Prescription.PrescriptionResponseDTO;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;

@Data
@EqualsAndHashCode(callSuper = true)
public class MedicalHistoryResponse extends MedicalRecordPatientResponse {
    private List<Icd10Response> icd10Diagnoses;
    private PrescriptionResponseDTO prescription;
}
