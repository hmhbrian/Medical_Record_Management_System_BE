package com.example.clinicbooking.controller;

import com.example.clinicbooking.DTO.ApiResponse;
import com.example.clinicbooking.DTO.ImagingTest.*;
import com.example.clinicbooking.DTO.LabTest.LabTestOfStaffResponse;
import com.example.clinicbooking.DTO.LabTest.LabTestWaitingRequest;
import com.example.clinicbooking.DTO.LabTest.LabTestWaitingResponse;
import com.example.clinicbooking.DTO.PaginatedResponseDTO;
import com.example.clinicbooking.security.CustomUserDetails;
import com.example.clinicbooking.service.ImagingTest.ImagingTestService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/imaging-tests")
@RequiredArgsConstructor
@Tag(name = "Imaging Test", description = "Quản lý kết quả chẩn đoán hình ảnh")
public class ImagingTestController {
    private final ImagingTestService imagingTestService;

    @PostMapping("/assign/{imagingTestId}")
    public ResponseEntity<ApiResponse<?>> assignImagingTest(@PathVariable Integer imagingTestId) {
        //Lấy id User đang đăng nhập
        var auth = SecurityContextHolder.getContext().getAuthentication();
        if (!(auth.getPrincipal() instanceof CustomUserDetails cud)) {
            throw new AccessDeniedException("Unauthorized");
        }
        Integer currentUserId = cud.getId();

        return ResponseEntity.ok(imagingTestService.assignImagingTest(imagingTestId, currentUserId));
    }

    @PutMapping("/{imagingTestId}/upload-results")
    public ApiResponse<?> uploadResults(
            @PathVariable Integer imagingTestId,
            @ModelAttribute ImagingResultUploadRequest request // Sử dụng @ModelAttribute cho form-data
    ) {
        //Lấy id User đang đăng nhập
        var auth = SecurityContextHolder.getContext().getAuthentication();
        if (!(auth.getPrincipal() instanceof CustomUserDetails cud)) {
            throw new AccessDeniedException("Unauthorized");
        }
        Integer currentUserId = cud.getId();

        return imagingTestService.uploadAndSaveImagingResults(imagingTestId, request, currentUserId);
    }

    @GetMapping("/{imagingtestId}/detail")
    public ResponseEntity<ImagingReportResponse> getLabTestDetails(
            @PathVariable("imagingtestId") Integer imagingTestId) {

        //Lấy id User đang đăng nhập
        var auth = SecurityContextHolder.getContext().getAuthentication();
        if (!(auth.getPrincipal() instanceof CustomUserDetails cud)) {
            throw new AccessDeniedException("Unauthorized");
        }
        Integer currentUserId = cud.getId();

        ImagingReportResponse response = imagingTestService.getLabTestDetails(imagingTestId, currentUserId);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/waiting")
    public ResponseEntity<PaginatedResponseDTO<ImagingTestWaitingResponse>> getImagingTestWaiting(
            @RequestParam(required = false) String keyword,
            @RequestParam(required = false) Integer doctorId,
            @RequestParam(required = false) Integer specialtyId,
            @RequestParam(required = false) String searchDate,
            @RequestParam(defaultValue = "0") Integer page,
            @RequestParam(defaultValue = "10") Integer size,
            @RequestParam(defaultValue = "requestedDate") String SortBy,
            @RequestParam(defaultValue = "ASC") String SortDir) {
        ImagingTestWaitingRequest request = new ImagingTestWaitingRequest();
        request.setKeyword(keyword);
        request.setDoctorId(doctorId);
        request.setSpecialtyId(specialtyId);
        request.setFindDate(searchDate);
        request.setSize(size);
        request.setPage(page);
        request.setSortDir(SortDir);
        request.setSortBy(SortBy);

        PaginatedResponseDTO<ImagingTestWaitingResponse> response = imagingTestService.searchImagingTestWaiting(request);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/of-staff")
    public ResponseEntity<PaginatedResponseDTO<ImagingTestOfStaffResponse>> getLabTestOfStaff(
            @RequestParam(required = false) String keyword,
            @RequestParam(required = false) Integer doctorId,
            @RequestParam(required = false) Integer specialtyId,
            @RequestParam(required = false) String searchDate,
            @RequestParam(defaultValue = "0") Integer page,
            @RequestParam(defaultValue = "10") Integer size,
            @RequestParam(defaultValue = "requestedDate") String SortBy,
            @RequestParam(defaultValue = "ASC") String SortDir) {
        ImagingTestWaitingRequest request = new ImagingTestWaitingRequest();
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

        PaginatedResponseDTO<ImagingTestOfStaffResponse> response = imagingTestService.searchImagingTestOfStaff(request, currentUserId);
        return ResponseEntity.ok(response);
    }
}
