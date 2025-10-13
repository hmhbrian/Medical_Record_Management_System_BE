package com.example.clinicbooking.controller;

import com.example.clinicbooking.DTO.Specialty.SpecialtyRequest;
import com.example.clinicbooking.DTO.Specialty.SpecialtyResponse;
import com.example.clinicbooking.entity.Department;
import com.example.clinicbooking.entity.Specialty;
import com.example.clinicbooking.service.SpecialtyService;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "Specialty", description = "Quản lý chuyên khoa")
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

    // IMPORTANT: This route must come BEFORE /{id} to avoid conflicts  
    @GetMapping("/by-department/{departmentId}")
    public ResponseEntity<List<Specialty>> getSpecialtiesByDepartment(@PathVariable("departmentId") int departmentId) {
        List<Specialty> specialties = specialtyService.getSpecialtiesByDepartment(departmentId);
        return ResponseEntity.ok(specialties);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Specialty> getSpecialtyById(@PathVariable("id") int id) {
        return ResponseEntity.ok(specialtyService.getSpecialtyById(id));
    }

    @PostMapping
    public Specialty createSpecialty(@RequestBody SpecialtyRequest specialtyRq) {
        return specialtyService.save(specialtyRq);
    }

    @PutMapping("/{id}")
    public ResponseEntity<String> updateSpecialty(@PathVariable int id, @RequestBody SpecialtyRequest specialtyRq) {
        try {
            Specialty updated = specialtyService.update(id, specialtyRq);
            return ResponseEntity.ok("Cập nhật chuyên khoa thành công!");
        } catch (IllegalArgumentException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteSpecialtyById(@PathVariable int id) {
        if(specialtyService.getSpecialtyById(id) == null) {
            return ResponseEntity.notFound().build();
        }
        specialtyService.deleteSpecialtyById(id);
        return ResponseEntity.ok("Xóa chuyên khoa thành công!");
    }
}
