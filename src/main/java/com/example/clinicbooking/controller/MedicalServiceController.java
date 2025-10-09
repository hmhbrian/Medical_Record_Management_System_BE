package com.example.clinicbooking.controller;

import com.example.clinicbooking.DTO.ApiResponse;
import com.example.clinicbooking.DTO.MedicalService.*;
import com.example.clinicbooking.service.MedicalServiceService;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "MedicalService", description = "Quản lý các dịch vụ y tế (status 1: active, 0: inactive)")
@RestController
@RequestMapping("/api/medical_service")
public class MedicalServiceController {
    @Autowired
    private MedicalServiceService service;

    @GetMapping("/overview")
    public ResponseEntity<ApiResponse<MedicalServiceOverviewResponse>> overview() {
        MedicalServiceOverviewResponse data = service.getOverview();
        return ResponseEntity.ok(new ApiResponse<>(true, "Tổng quan dịch vụ", data));
    }

    @PostMapping
    public ResponseEntity<?> create(@Valid @RequestBody MedicalServiceRequest req) {
        try {
            service.create(req);
            return ResponseEntity.status(HttpStatus.CREATED)
                    .body(new ApiResponse(true, "Thêm dịch vụ thành công", null));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new ApiResponse(false, "Thêm dịch vụ thất bại: " + e.getMessage(), null));
        }
    }

    // Lấy danh sách tất cả, hoặc filter theo type ?type=LAB_TEST/EXAMINATION/IMAGING
    @GetMapping
    public ResponseEntity<ApiResponse<List<MedicalServiceResponse>>> list(
            @RequestParam(name = "type", required = false) EMedicalService type) {
        List<MedicalServiceResponse> result = service.Getlist(type);

        return ResponseEntity.ok(
                new ApiResponse<>(true, "Lấy danh sách dịch vụ thành công", result)
        );
    }

    // =========== XEM CHI TIẾT ===========
    // ví dụ: GET /api/medical-services/detail?type=LAB_TEST&id=5
    @GetMapping("/detail")
    public ResponseEntity<ApiResponse<MedicalServiceResponse>> detail(
            @RequestParam EMedicalService type,
            @RequestParam int id) {

        MedicalServiceResponse data = service.getDetail(type, id);
        return ResponseEntity.ok(new ApiResponse<>(true, "Lấy chi tiết dịch vụ thành công", data));
    }

    // =========== CẬP NHẬT ===========
    // ví dụ: PUT /api/medical-services/update?type=IMAGING&id=10
    @PutMapping("/update")
    public ResponseEntity<ApiResponse<MedicalServiceResponse>> update(
            @RequestParam EMedicalService type,
            @RequestParam int id,
            @RequestBody UpdateMedicalServiceRequest req) {

        MedicalServiceResponse data = service.update(type, id, req);
        return ResponseEntity.ok(new ApiResponse<>(true, "Cập nhật dịch vụ thành công", data));
    }
}
