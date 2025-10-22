package com.example.clinicbooking.controller;

import com.example.clinicbooking.DTO.Icd10.Icd10Reponse;
import com.example.clinicbooking.DTO.MedicalExaminationResponse;
import com.example.clinicbooking.service.Icd10.Icd10Service;
import com.example.clinicbooking.service.MedicalExamination.MedicalExaminationService;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Tag(name = "Icd10", description = "Quản lý mã bệnh")
@RestController
@RequestMapping("/api/icd10s")
public class Icd10Controller {
    @Autowired
    private Icd10Service icd10Service;

    @GetMapping
    public ResponseEntity<List<Icd10Reponse>> findAllExaminations(
            @RequestParam(name = "keyword", required = false) String keyword) {

        List<Icd10Reponse> results = icd10Service.search(keyword);
        return ResponseEntity.ok(results);
    }
}
