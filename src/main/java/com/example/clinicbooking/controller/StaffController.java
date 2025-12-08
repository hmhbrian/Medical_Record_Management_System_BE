package com.example.clinicbooking.controller;

import com.example.clinicbooking.DTO.ApiResponse;
import com.example.clinicbooking.DTO.Staff.StaffRequest;
import com.example.clinicbooking.DTO.Staff.StaffResponse;
import com.example.clinicbooking.DTO.Staff.StaffSummary;
import com.example.clinicbooking.entity.staff_position;
import com.example.clinicbooking.service.IStaffService;
import com.example.clinicbooking.service.StaffPositionService;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "Staffs", description = "Quản lý nhân viên y tế")
@RestController
@RequestMapping("/api/staffs")
public class StaffController {
    @Autowired
    private IStaffService staffService;
    @Autowired
    private StaffPositionService staffPositionService;

    @PostMapping
    public ResponseEntity<ApiResponse<?>> create(@RequestBody StaffRequest request) {
        String message = staffService.create(request);
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(new ApiResponse<>(true, message, null));
    }

    @GetMapping
    public ResponseEntity<ApiResponse<Page<StaffResponse>>> search(
            @RequestParam(required = false) String roleType, // DOCTOR|NURSE|LAB|IMAGING
            @RequestParam(required = false) Integer departmentId,
            @RequestParam(required = false) Integer positionId,
            @RequestParam(required = false) String keyword,
            @ParameterObject Pageable pageable) {
        Page<StaffResponse> page = staffService.search(roleType, departmentId, positionId, keyword, pageable);
        return ResponseEntity.ok(new ApiResponse<>(true, "Lấy danh sách nhân viên thành công!", page));
    }

    @GetMapping("/positions/no-doctor")
    public List<staff_position> getAllStafPosition() {
        return staffPositionService.getAllNoDoctor();
    }

    @GetMapping("/positions/{positionId}")
    public ApiResponse<List<StaffSummary>> getStaffByPosition(@PathVariable int positionId) {
        return staffService.findStaffByPosition(positionId);
    }
}
