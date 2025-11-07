package com.example.clinicbooking.controller;

import com.example.clinicbooking.DTO.ImagingTest.ImagingReportResponse;
import com.example.clinicbooking.service.ImagingTestService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/imaging-tests")
@RequiredArgsConstructor
@Tag(name = "Imaging Test", description = "Quản lý kết quả chẩn đoán hình ảnh")
public class ImagingTestController {
    private final ImagingTestService imagingTestService;

    @GetMapping("/{imagingtestId}/detail")
    public ResponseEntity<ImagingReportResponse> getLabTestDetails(
            @PathVariable("imagingtestId") Integer imagingTestId) {

        ImagingReportResponse response = imagingTestService.getImagingReport(imagingTestId);
        return ResponseEntity.ok(response);
    }
}
