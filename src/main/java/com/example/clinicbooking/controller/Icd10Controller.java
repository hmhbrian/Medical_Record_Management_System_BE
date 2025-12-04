package com.example.clinicbooking.controller;

import com.example.clinicbooking.DTO.ApiResponse;
import com.example.clinicbooking.DTO.Icd10.Icd10Reponse;
import com.example.clinicbooking.DTO.Icd10.Icd10Request;
import com.example.clinicbooking.service.Icd10.Icd10Service;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "Icd10", description = "Quản lý mã bệnh")
@RestController
@RequestMapping("/api/icd10s")
public class Icd10Controller {
    @Autowired
    private Icd10Service icd10Service;

    @GetMapping
    public ResponseEntity<List<Icd10Reponse>> findAllIcd10(
            @RequestParam(name = "keyword", required = false) String keyword) {

        List<Icd10Reponse> results = icd10Service.search(keyword);
        return ResponseEntity.ok(results);
    }

    @PostMapping
    public ResponseEntity<ApiResponse<?>> createIcd10(@RequestBody Icd10Request request) {
        return ResponseEntity.ok(icd10Service.create(request));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<?>> updateIcd10(
            @PathVariable Integer id,
            @RequestBody Icd10Request request) {
        return ResponseEntity.ok(icd10Service.update(id, request));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<?>> deleteIcd10(@PathVariable Integer id) {
        return ResponseEntity.ok(icd10Service.delete(id));
    }
}
