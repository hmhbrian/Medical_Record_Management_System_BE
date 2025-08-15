package com.example.ClinicBooking.Presentation.Controllers;

import com.example.ClinicBooking.DTO.MedicalRecordReponse;
import com.example.ClinicBooking.DTO.MedicalRecordRequest;
import com.example.ClinicBooking.Domain.Entities.MedicalRecord;
import com.example.ClinicBooking.service.MedicalRecordService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/medical-records")
public class MedicalRecordController {

    @Autowired
    private MedicalRecordService recordService;

    @PostMapping
    public ResponseEntity<MedicalRecord> create(@RequestBody MedicalRecordRequest request) {
        return ResponseEntity.ok(recordService.createMedicalRecord(request));
    }

    @GetMapping("/patient/{patientId}")
    public ResponseEntity<List<MedicalRecordReponse>> getByPatient(@PathVariable Integer patientId) {
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

