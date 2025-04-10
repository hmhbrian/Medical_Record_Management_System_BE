package com.example.ClinicBooking.controller;

import com.example.ClinicBooking.DTO.PatientRequest;
import com.example.ClinicBooking.DTO.PatientResponse;
import com.example.ClinicBooking.entity.Patient;
import com.example.ClinicBooking.service.PatientService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/patients")
public class PatientController {
    private final PatientService patientService;

    public PatientController(PatientService patientService) {
        this.patientService = patientService;
    }

    @PostMapping
    public ResponseEntity<?> create(@RequestBody PatientRequest request) {
        try{
            return ResponseEntity.ok(patientService.createPatient(request));
        }catch (Exception e){
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping
    public List<PatientResponse> getAll() {
        return patientService.getAll();
    }
}
