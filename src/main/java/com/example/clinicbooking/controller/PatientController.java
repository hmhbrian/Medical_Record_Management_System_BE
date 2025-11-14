package com.example.clinicbooking.controller;

import com.example.clinicbooking.DTO.Patient.PatientRequest;
import com.example.clinicbooking.DTO.Patient.PatientResponse;
import com.example.clinicbooking.service.IUserService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import io.swagger.v3.oas.annotations.tags.Tag;

import java.util.List;

@Tag(name = "Patients", description = "Quản lý bệnh nhân")
@RestController
@RequestMapping("/api/patients")
public class PatientController {
    private final IUserService<PatientResponse,PatientRequest> patientService;

    public PatientController(IUserService<PatientResponse,PatientRequest> patientService) {
        this.patientService = patientService;
    }

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
}
