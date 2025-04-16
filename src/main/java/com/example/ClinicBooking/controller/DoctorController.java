package com.example.ClinicBooking.controller;

import com.example.ClinicBooking.DTO.DoctorRequest;
import com.example.ClinicBooking.DTO.DoctorResponse;
import com.example.ClinicBooking.service.IUserService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/doctors")
public class DoctorController {
    private final IUserService<DoctorResponse, DoctorRequest> doctorService;

    public DoctorController(IUserService<DoctorResponse, DoctorRequest> doctorService) {
        this.doctorService = doctorService;
    }

    @PostMapping
    public ResponseEntity<DoctorResponse> create(@RequestBody DoctorRequest request) {
        return ResponseEntity.ok(doctorService.create(request));
    }

    @GetMapping
    public ResponseEntity<List<DoctorResponse>> getAll() {
        return ResponseEntity.ok(doctorService.getAll());
    }
}
