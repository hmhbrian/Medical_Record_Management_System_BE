package com.example.clinicbooking.controller;

import com.example.clinicbooking.DTO.ApiResponse;
import com.example.clinicbooking.DTO.LabParameter.LabParameterDTO;
import com.example.clinicbooking.DTO.LabParameter.TestTypeParameterDetailDTO;
import com.example.clinicbooking.DTO.Services.MedicalExaminationResponse;
import com.example.clinicbooking.DTO.Services.TestTypeResponse;
import com.example.clinicbooking.service.MedicalExamination.MedicalExaminationService;
import com.example.clinicbooking.service.TestType.TestTypeService;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "TestTypes", description = "Quản lý danh mục loại xét nghiệm")
@RestController
@RequestMapping("/api/testtypes")
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

    @GetMapping("/{testTypeId}/parameters")
    public ResponseEntity<TestTypeParameterDetailDTO> getTestParametersDetail(@PathVariable Integer testTypeId) {

        TestTypeParameterDetailDTO dto = testTypeService.getTestParameters(testTypeId);

        // Trả về HTTP 200 OK với dữ liệu chi tiết
        return ResponseEntity.ok(dto);
    }

    @PutMapping("/{testTypeId}/parameters")
    public ResponseEntity<ApiResponse<?>> updateTestParameters(
            @PathVariable Integer testTypeId,
            @RequestBody List<LabParameterDTO> updatedParameters) {

        testTypeService.saveAllParameters(testTypeId, updatedParameters);

        return ResponseEntity.ok(new ApiResponse<>(true, "Cập nhật tham số xét nghiệm thành công", null));
    }
}
