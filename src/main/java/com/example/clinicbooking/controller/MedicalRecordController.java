package com.example.clinicbooking.controller;

import com.example.clinicbooking.DTO.ApiResponse;
import com.example.clinicbooking.DTO.MedicalRecord.MedicalRecordRequest;
import com.example.clinicbooking.DTO.MedicalRecord.MedicalRecordResponse;
import com.example.clinicbooking.entity.MedicalRecord;
import com.example.clinicbooking.service.MedicalRecordService;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@Tag(name = "MedicalRecord", description = "Quản lý hồ sơ ngoại trú")
@RestController
@RequestMapping("/api/medical-records")
public class MedicalRecordController {

    @Autowired
    private MedicalRecordService recordService;

    @PostMapping
    public ResponseEntity<ApiResponse<?>> create(@RequestBody MedicalRecordRequest request) {
        recordService.createMedicalRecord(request);
        return ResponseEntity.ok(new ApiResponse<>(true, "Hồ sơ ngoại trú được thêm thành công", null));
    }

    @GetMapping("/patient/{patientId}")
    public ResponseEntity<List<MedicalRecordResponse>> getByPatient(@PathVariable Integer patientId) {
        return ResponseEntity.ok(recordService.getRecordsByPatientId(patientId));
    }

    @GetMapping("/doctor/{doctorId}")
    public ResponseEntity<List<MedicalRecord>> getByDoctor(@PathVariable Integer doctorId) {
        return ResponseEntity.ok(recordService.getRecordsByDoctorId(doctorId));
    }

    @GetMapping("/grouped/patient")
    public ResponseEntity<List<MedicalRecord>> getAllGroupedByPatient() {
        return ResponseEntity.ok(recordService.getAllRecordsGroupedByPatient());
    }

    @GetMapping("/{id}")
    public ResponseEntity<MedicalRecord> getById(@PathVariable Integer id) {
        return recordService.getRecordById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
}

