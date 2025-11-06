package com.example.clinicbooking.controller;

import com.example.clinicbooking.DTO.Doctor.DoctorRequest;
import com.example.clinicbooking.DTO.Doctor.DoctorResponse;
import com.example.clinicbooking.service.IDoctorService;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "Doctors", description = "Quản lý bác sĩ")
@RestController
@RequestMapping("/api/doctors")
public class DoctorController {
    private final IDoctorService doctorService;

    public DoctorController(IDoctorService doctorService) {
        this.doctorService = doctorService;
    }

    @PostMapping
    public ResponseEntity<DoctorResponse> create(@RequestBody DoctorRequest request) {
        return ResponseEntity.ok(doctorService.create(request));
    }

    @GetMapping
    public ResponseEntity<List<DoctorResponse>> getAll() {
        List<DoctorResponse> doctors = doctorService.getAll();
        return ResponseEntity.ok(doctors);
    }

    @PutMapping("/{id}")
    public ResponseEntity<DoctorResponse> update(@PathVariable Integer id, @RequestBody DoctorRequest request) {
        return ResponseEntity.ok(doctorService.update(id, request));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable("id") Integer id) {
        doctorService.delete(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/specialty/{id}")
    public ResponseEntity<List<DoctorResponse>> getDoctorsBySpecialty(@PathVariable("id") Integer specialtyId) {
        List<DoctorResponse> doctors = doctorService.getDoctorsBySpecialtyId(specialtyId);
        return ResponseEntity.ok(doctors);
    }

    @GetMapping("/{id}")
    public ResponseEntity<DoctorResponse> getDoctorsById(@PathVariable("id") Integer doctorId) {
        DoctorResponse doctors = doctorService.getDoctorsById(doctorId);
        return ResponseEntity.ok(doctors);
    }
}
