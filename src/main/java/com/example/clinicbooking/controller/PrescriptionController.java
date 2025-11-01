package com.example.clinicbooking.controller;

import com.example.clinicbooking.DTO.ApiResponse;
import com.example.clinicbooking.DTO.Prescription.PrescriptionRequest;
import com.example.clinicbooking.DTO.Prescription.PrescriptionResponse;
import com.example.clinicbooking.service.PrescriptionService;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
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

}
