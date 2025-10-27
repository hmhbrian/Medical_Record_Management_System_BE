package com.example.clinicbooking.controller;

import com.example.clinicbooking.DTO.Services.MedicalExaminationResponse;
import com.example.clinicbooking.DTO.Services.TestTypeResponse;
import com.example.clinicbooking.service.MedicalExamination.MedicalExaminationService;
import com.example.clinicbooking.service.TestType.TestTypeService;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Tag(name = "TestTypes", description = "Quản lý danh mục loại xét nghiệm")
@RestController
@RequestMapping("/api/TestTypes")
public class TestTypeController {
    @Autowired
    private TestTypeService testTypeService;

    //Endpoint tìm kiếm không phân trang (tùy chọn)
    @GetMapping
    public ResponseEntity<List<TestTypeResponse>> findAllExaminations(
            @RequestParam(name = "keyword", required = false) String keyword) {

        List<TestTypeResponse> results = testTypeService.search(keyword);
        return ResponseEntity.ok(results);
    }
}
