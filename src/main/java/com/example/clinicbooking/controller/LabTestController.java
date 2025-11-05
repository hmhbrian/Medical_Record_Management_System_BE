package com.example.clinicbooking.controller;


import com.example.clinicbooking.DTO.LabTest.LabTestDetailResponse;
import com.example.clinicbooking.service.LabTest.LabTestService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/lab-tests")
@RequiredArgsConstructor
@Tag(name = "Lab Test", description = "Quản lý kết quả xét nghiệm")
public class LabTestController {
    private final LabTestService labTestService;

    @GetMapping("/{labTestId}/detail")
    public ResponseEntity<LabTestDetailResponse> getLabTestDetails(
            @PathVariable("labTestId") Integer labTestId) {

        LabTestDetailResponse response = labTestService.getLabTestDetails(labTestId);
        return ResponseEntity.ok(response);
    }
}
