package com.example.clinicbooking.controller;


import com.example.clinicbooking.DTO.ApiResponse;
import com.example.clinicbooking.DTO.LabTest.LabTestDetailResponse;
import com.example.clinicbooking.DTO.LabTest.LabTestOfStaffResponse;
import com.example.clinicbooking.DTO.LabTest.LabTestWaitingRequest;
import com.example.clinicbooking.DTO.LabTest.LabTestWaitingResponse;
import com.example.clinicbooking.DTO.PaginatedResponseDTO;
import com.example.clinicbooking.DTO.Payment.PaymentResponse;
import com.example.clinicbooking.DTO.Payment.PaymentSearchRequest;
import com.example.clinicbooking.security.CustomUserDetails;
import com.example.clinicbooking.service.LabTest.LabTestService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/lab-tests")
@RequiredArgsConstructor
@Tag(name = "Lab Test", description = "Quản lý kết quả xét nghiệm")
public class LabTestController {
    private final LabTestService labTestService;

    @PostMapping("/assign/{labTestId}")
    public ResponseEntity<ApiResponse<?>> assignLabTest(@PathVariable Integer labTestId) {
        //Lấy id User đang đăng nhập
        var auth = SecurityContextHolder.getContext().getAuthentication();
        if (!(auth.getPrincipal() instanceof CustomUserDetails cud)) {
            throw new AccessDeniedException("Unauthorized");
        }
        Integer currentUserId = cud.getId();

        return ResponseEntity.ok(labTestService.assignLabTest(labTestId, currentUserId));
    }

    @GetMapping("/waiting")
    public ResponseEntity<PaginatedResponseDTO<LabTestWaitingResponse>> getLabTestWaiting(
            @RequestParam(required = false) String keyword,
            @RequestParam(required = false) Integer doctorId,
            @RequestParam(required = false) Integer specialtyId,
            @RequestParam(required = false) String searchDate,
            @RequestParam(defaultValue = "0") Integer page,
            @RequestParam(defaultValue = "10") Integer size,
            @RequestParam(defaultValue = "requestedDate") String SortBy,
            @RequestParam(defaultValue = "ASC") String SortDir) {
        LabTestWaitingRequest request = new LabTestWaitingRequest();
        request.setKeyword(keyword);
        request.setDoctorId(doctorId);
        request.setSpecialtyId(specialtyId);
        request.setFindDate(searchDate);
        request.setSize(size);
        request.setPage(page);
        request.setSortDir(SortDir);
        request.setSortBy(SortBy);

        PaginatedResponseDTO<LabTestWaitingResponse> response = labTestService.searchLabTestWaiting(request);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/of-staff")
    public ResponseEntity<PaginatedResponseDTO<LabTestOfStaffResponse>> getLabTestOfStaff(
            @RequestParam(required = false) String keyword,
            @RequestParam(required = false) Integer doctorId,
            @RequestParam(required = false) Integer specialtyId,
            @RequestParam(required = false) String searchDate,
            @RequestParam(defaultValue = "0") Integer page,
            @RequestParam(defaultValue = "10") Integer size,
            @RequestParam(defaultValue = "requestedDate") String SortBy,
            @RequestParam(defaultValue = "ASC") String SortDir) {
        LabTestWaitingRequest request = new LabTestWaitingRequest();
        request.setKeyword(keyword);
        request.setDoctorId(doctorId);
        request.setSpecialtyId(specialtyId);
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

        PaginatedResponseDTO<LabTestOfStaffResponse> response = labTestService.searchTestOfLabStaff(request, currentUserId);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{labTestId}/detail")
    public ResponseEntity<LabTestDetailResponse> getLabTestDetails(
            @PathVariable("labTestId") Integer labTestId) {

        LabTestDetailResponse response = labTestService.getLabTestDetails(labTestId);
        return ResponseEntity.ok(response);
    }

}
