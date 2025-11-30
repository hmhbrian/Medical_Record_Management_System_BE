package com.example.clinicbooking.controller;

import com.example.clinicbooking.DTO.ApiResponse;
import com.example.clinicbooking.DTO.Dashboard.DashboardOverview;
import com.example.clinicbooking.DTO.PaginatedResponseDTO;
import com.example.clinicbooking.DTO.Prescription.*;
import com.example.clinicbooking.DTO.Prescription.Detail.PrescriptionDetailsOfStaffRp;
import com.example.clinicbooking.security.CustomUserDetails;
import com.example.clinicbooking.service.Prescription.PrescriptionService;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

@Tag(name = "Prescription", description = "Quản lý đơn thuốc")
@RestController
@RequestMapping("/api/prescription")
public class PrescriptionController {
    @Autowired
    private PrescriptionService prescriptionService;

    // Tạo hoặc gửi đơn thuốc
    @PostMapping("/record/{recordId}")
    public ResponseEntity<ApiResponse<?>> createPrescription(@PathVariable Integer recordId, @RequestBody PrescriptionRequest prescriptionRequest) {
        return ResponseEntity.ok(prescriptionService.saveOrSendPrescription(recordId,prescriptionRequest));
    }

    @PostMapping("/assign/{prescriptionId}")
    public ResponseEntity<ApiResponse<?>> assignPrescription(@PathVariable Integer prescriptionId) {
        //Lấy id User đang đăng nhập
        var auth = SecurityContextHolder.getContext().getAuthentication();
        if (!(auth.getPrincipal() instanceof CustomUserDetails cud)) {
            throw new AccessDeniedException("Unauthorized");
        }
        Integer currentUserId = cud.getId();

        return ResponseEntity.ok(prescriptionService.assignPrescription(prescriptionId, currentUserId));
    }

    @PutMapping("/complete/{prescriptionId}")
    public ResponseEntity<ApiResponse<?>> completePrescription(@PathVariable Integer prescriptionId) {
        //Lấy id User đang đăng nhập
        var auth = SecurityContextHolder.getContext().getAuthentication();
        if (!(auth.getPrincipal() instanceof CustomUserDetails cud)) {
            throw new AccessDeniedException("Unauthorized");
        }
        Integer currentUserId = cud.getId();
        prescriptionService.completeDispensing(prescriptionId, currentUserId);

        return ResponseEntity.ok(new ApiResponse<>(true, "Hoàn tất phát thuốc thành công.", null));
    }

    // Hủy đơn thuốc và cho phép tạo đơn thuốc mới
    @DeleteMapping("/record/{recordId}")
    public ResponseEntity<ApiResponse<?>> CancelPrescription(@PathVariable Integer recordId) {
        return ResponseEntity.ok(prescriptionService.cancelPrescriptionAndAllowNew(recordId));
    }

    // Lấy đơn thuốc theo hồ sơ ngoại trú
    @GetMapping("/record/{recordId}")
    public ResponseEntity<ApiResponse<?>> getPrescription(@PathVariable Integer recordId) {
        PrescriptionResponse prescription = prescriptionService.getPrescriptionByRecordId(recordId);
        if(prescription == null){
            return ResponseEntity.ok(new ApiResponse<>(false, "Không tìm thấy đơn thuốc cho hồ sơ ngoại trú này.", null));
        }
        return ResponseEntity.ok(new ApiResponse<>(true, "Lấy đơn thuốc thành công.", prescription));
    }

    @GetMapping("/waiting")
    public ResponseEntity<PaginatedResponseDTO<PrescriptionWaitingResponse>> getPrescriptionWaiting(
            @RequestParam(required = false) String keyword,
            @RequestParam(required = false) Integer doctorId,
            @RequestParam(required = false) String searchDate,
            @RequestParam(defaultValue = "0") Integer page,
            @RequestParam(defaultValue = "10") Integer size,
            @RequestParam(defaultValue = "prescriptionDate") String SortBy,
            @RequestParam(defaultValue = "ASC") String SortDir) {
        PrescriptionWaitingRequest request = new PrescriptionWaitingRequest();
        request.setKeyword(keyword);
        request.setDoctorId(doctorId);
        request.setFindDate(searchDate);
        request.setSize(size);
        request.setPage(page);
        request.setSortDir(SortDir);
        request.setSortBy(SortBy);

        PaginatedResponseDTO<PrescriptionWaitingResponse> response = prescriptionService.searchPrescriptionWaiting(request);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/of-staff")
    public ResponseEntity<PaginatedResponseDTO<PrescriptionOfStaffResponse>> getPrescriptionOfStaff(
            @RequestParam(required = false) String keyword,
            @RequestParam(required = false) Integer doctorId,
            @RequestParam(required = false) Integer specialtyId,
            @RequestParam(required = false) String searchDate,
            @RequestParam(defaultValue = "0") Integer page,
            @RequestParam(defaultValue = "10") Integer size,
            @RequestParam(defaultValue = "prescriptionDate") String SortBy,
            @RequestParam(defaultValue = "ASC") String SortDir) {
        PrescriptionWaitingRequest request = new PrescriptionWaitingRequest();
        request.setKeyword(keyword);
        request.setDoctorId(doctorId);
        request.setFindDate(searchDate);
        request.setSize(size);
        request.setPage(page);
        request.setSortDir(SortDir);
        request.setSortBy(SortBy);

        //Lấy id User đang đăng nhập
        var auth = SecurityContextHolder.getContext().getAuthentication();
        if (!(auth.getPrincipal() instanceof CustomUserDetails cud)) {
            throw new AccessDeniedException("Unauthorized");
        }
        Integer currentUserId = cud.getId();

        PaginatedResponseDTO<PrescriptionOfStaffResponse> response = prescriptionService.searchPrescriptionOfPharmacist(request, currentUserId);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{prescriptionId}/detail")
    public ResponseEntity<PrescriptionDetailsOfStaffRp> getLabTestDetails(
            @PathVariable("prescriptionId") Integer prescriptionId) {
        PrescriptionDetailsOfStaffRp response = prescriptionService.getPrescriptionByPrescriptionId(prescriptionId);
        return ResponseEntity.ok(response);
    }
}
