package com.example.ClinicBooking.controller;

import com.example.ClinicBooking.DTO.DoctorRequest;
import com.example.ClinicBooking.DTO.DoctorResponse;
import com.example.ClinicBooking.DTO.PatientRequest;
import com.example.ClinicBooking.entity.Doctor;
import com.example.ClinicBooking.entity.Patient;
import com.example.ClinicBooking.service.DoctorService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/doctors")
public class DoctorController {
    private final DoctorService doctorService;

    public DoctorController(DoctorService doctorService) {
        this.doctorService = doctorService;
    }

    @PostMapping
    public ResponseEntity<?> create(@RequestBody DoctorRequest request) {
        try{
            return ResponseEntity.ok(doctorService.createDoctor(request));
        }catch (Exception e){
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping
    public List<DoctorResponse> getAll() {
        return doctorService.getAll();
    }
}
