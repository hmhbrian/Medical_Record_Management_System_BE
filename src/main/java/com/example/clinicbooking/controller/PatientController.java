package com.example.clinicbooking.controller;

import com.example.clinicbooking.DTO.Patient.PatientRequest;
import com.example.clinicbooking.DTO.Patient.PatientResponse;
import com.example.clinicbooking.service.IPatientService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import io.swagger.v3.oas.annotations.tags.Tag;

import java.util.List;

@Tag(name = "Patients", description = "Quản lý bệnh nhân")
@RestController
@RequestMapping("/api/patients")
@AllArgsConstructor
public class PatientController {
    private final IPatientService patientService;

    @PostMapping
    public ResponseEntity<PatientResponse> create(@RequestBody PatientRequest request) {
        return ResponseEntity.ok(patientService.create(request));
    }

    @GetMapping
    public ResponseEntity<List<PatientResponse>> getAll() {
        return ResponseEntity.ok(patientService.getAll());
    }

    @GetMapping("/{userId}")
    public ResponseEntity<PatientResponse> getbyUserId(@PathVariable Integer userId) {
        PatientResponse patient = patientService.getbyUserId(userId);
        return ResponseEntity.ok(patient);
    }

    @PutMapping("/{id}")
    public ResponseEntity<PatientResponse> update(@PathVariable Integer id, @RequestBody PatientRequest request) {
        return ResponseEntity.ok(patientService.update(id, request));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Integer id) {
        patientService.delete(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/search")
    public ResponseEntity<PatientResponse> search(@RequestParam String keyword) {
        return ResponseEntity.ok(patientService.searchPatients(keyword));
    }
}
