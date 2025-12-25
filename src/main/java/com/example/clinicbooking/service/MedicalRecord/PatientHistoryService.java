package com.example.clinicbooking.service.MedicalRecord;

import com.example.clinicbooking.DTO.MedicalRecord.DiagnosisData.Icd10Response;
import com.example.clinicbooking.DTO.MedicalRecord.MedicalHistoryResponse;
import com.example.clinicbooking.DTO.Prescription.PrescriptionResponseDTO;
import com.example.clinicbooking.entity.MedicalRecord;
import com.example.clinicbooking.entity.MedicalRecordIcd10;
import com.example.clinicbooking.exceptions.InvalidInputException;
import com.example.clinicbooking.repository.MedicalRecordIcd10Repository;
import com.example.clinicbooking.repository.MedicalRecordRepository;
import com.example.clinicbooking.repository.PatientRepository;
import com.example.clinicbooking.service.Prescription.PrescriptionService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PatientHistoryService {
    private final MedicalRecordRepository recordRepo;
    private final PatientRepository patientRepo;
    private final MedicalRecordIcd10Repository medicalRecordIcd10Repository;
    private final PrescriptionService prescriptionService;


}
