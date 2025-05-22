package com.example.ClinicBooking.controller;

import com.example.ClinicBooking.DTO.SpecialtyResponse;
import com.example.ClinicBooking.entity.Specialty;
import com.example.ClinicBooking.service.SpecialtyService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/specialties")
public class SpecialtyController {
    private SpecialtyService specialtyService;

    public SpecialtyController(SpecialtyService specialtyService) {
        this.specialtyService = specialtyService;
    }

    @GetMapping
    public List<SpecialtyResponse> getAllSpecialties() {
        return specialtyService.getAll();
    }

    @GetMapping("/{id}")
    public List<Specialty> getSpecialtyById(@PathVariable int id) {
        return specialtyService.getSpecialtiesByDepartment(id);
    }

    @PostMapping
    public Specialty createSpecialty(@RequestBody Specialty specialty) {
        return specialtyService.save(specialty);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Specialty> updateSpecialty(@PathVariable int id, @RequestBody Specialty specialty) {
        if(specialtyService.getSpecialtyById(id) == null) {
            return ResponseEntity.notFound().build();
        }
        specialty.setId(id);
        return ResponseEntity.ok(specialtyService.save(specialty));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteSpecialtyById(@PathVariable int id) {
        if(specialtyService.getSpecialtyById(id) == null) {
            return ResponseEntity.notFound().build();
        }
        specialtyService.deleteSpecialtyById(id);
        return ResponseEntity.ok().build();
    }
}
