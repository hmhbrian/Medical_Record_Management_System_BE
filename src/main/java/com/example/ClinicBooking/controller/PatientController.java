package com.example.ClinicBooking.controller;

import com.example.ClinicBooking.DTO.PatientRequest;
import com.example.ClinicBooking.DTO.PatientResponse;
import com.example.ClinicBooking.service.IUserService;
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
}
