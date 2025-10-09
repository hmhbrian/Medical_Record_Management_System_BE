package com.example.clinicbooking.controller;

import com.example.clinicbooking.DTO.ApiResponse;
import com.example.clinicbooking.DTO.Doctor.DoctorRequest;
import com.example.clinicbooking.DTO.Doctor.DoctorResponse;
import com.example.clinicbooking.DTO.Staff.StaffRequest;
import com.example.clinicbooking.DTO.Staff.StaffResponse;
import com.example.clinicbooking.service.IDoctorService;
import com.example.clinicbooking.service.IStaffService;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Tag(name = "Staffs", description = "Quản lý nhân viên y tế")
@RestController
@RequestMapping("/api/staffs")
public class StaffController {
    private final IStaffService staffService;

    public StaffController(IStaffService staffService) {
        this.staffService = staffService;
    }

    @PostMapping
    public ResponseEntity<ApiResponse<?>> create(@RequestBody StaffRequest request) {
        String message = staffService.create(request);
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(new ApiResponse<>(true, message,null));
    }
    @GetMapping
    public ResponseEntity<ApiResponse<Page<StaffResponse>>> search(
            @RequestParam(required = false) String roleType,      // DOCTOR|NURSE|LAB|IMAGING
            @RequestParam(required = false) Integer departmentId,
            @RequestParam(required = false) Integer positionId,
            @RequestParam(required = false) String keyword,
            @ParameterObject Pageable pageable
    ) {
        Page<StaffResponse> page = staffService.search(roleType, departmentId, positionId, keyword, pageable);
        return ResponseEntity.ok(new ApiResponse<>(true, "Lấy danh sách nhân viên thành công!", page));
    }
}
