package com.example.ClinicBooking.controller;

import com.example.ClinicBooking.DTO.MedicineRequest;
import com.example.ClinicBooking.DTO.MedicineResponse;
import com.example.ClinicBooking.entity.Medicine;
import com.example.ClinicBooking.service.MedicineService;
import org.hibernate.query.Page;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.awt.print.Pageable;
import java.util.List;

@RestController
@RequestMapping("/api/medicines")
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
}

