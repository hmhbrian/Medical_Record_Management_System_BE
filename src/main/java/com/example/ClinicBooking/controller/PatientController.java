package com.example.ClinicBooking.controller;

import com.example.ClinicBooking.DTO.DoctorResponse;
import com.example.ClinicBooking.DTO.PatientRequest;
import com.example.ClinicBooking.DTO.PatientResponse;
import com.example.ClinicBooking.service.IUserService;
import com.example.ClinicBooking.service.PatientService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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
    @GetMapping("/{id}")
    public ResponseEntity<PatientResponse> getbyUserId(@PathVariable("id") Integer userId) {
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
