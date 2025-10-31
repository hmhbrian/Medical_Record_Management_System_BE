package com.example.clinicbooking.controller;

import com.example.clinicbooking.DTO.Medicine.MedicineRequest;
import com.example.clinicbooking.DTO.Medicine.MedicineResponse;
import com.example.clinicbooking.DTO.Medicine.MedicineSummaryResponse;
import com.example.clinicbooking.service.Medicine.MedicineService;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/medicines")
@Tag(name = "Medicines", description = "Quản lý thuốc")
public class MedicineController {

    @Autowired
    private MedicineService medicineService;

    @PostMapping
    public ResponseEntity<MedicineResponse> create(@RequestBody MedicineRequest request) {
        return ResponseEntity.ok(medicineService.create(request));
    }

    @GetMapping
    public ResponseEntity<List<MedicineResponse>> getAll() {
        return ResponseEntity.ok(medicineService.getAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<MedicineResponse> getById(@PathVariable Integer id) {
        return ResponseEntity.ok(medicineService.getById(id));
    }

    @PutMapping("/{id}")
    public ResponseEntity<MedicineResponse> update(@PathVariable Integer id, @RequestBody MedicineRequest request) {
        return ResponseEntity.ok(medicineService.update(id, request));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Integer id) {
        medicineService.delete(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/search")
    public ResponseEntity<List<MedicineResponse>> search(@RequestParam String name) {
        return ResponseEntity.ok(medicineService.searchByName(name));
    }
    @GetMapping("/expiring")
    public ResponseEntity<List<MedicineResponse>> getExpiringSoon(
            @RequestParam(defaultValue = "30") int days
    ) {
        return ResponseEntity.ok(medicineService.getExpiringMedicines(days));
    }

    @GetMapping("/prescription")
    public ResponseEntity<List<MedicineSummaryResponse>> getMedicineForPrescription(
            @RequestParam(name = "keyword", required = false) String keyword) {
        return ResponseEntity.ok(medicineService.getMedicineForPrescription(keyword));
    }

}

