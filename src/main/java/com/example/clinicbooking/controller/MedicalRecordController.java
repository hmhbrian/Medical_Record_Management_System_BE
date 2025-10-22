package com.example.clinicbooking.controller;

import com.example.clinicbooking.DTO.ApiResponse;
import com.example.clinicbooking.DTO.MedicalRecord.MedicalRecordMetricsResponse;
import com.example.clinicbooking.DTO.MedicalRecord.MedicalRecordRequest;
import com.example.clinicbooking.DTO.MedicalRecord.MedicalRecordResponse;
import com.example.clinicbooking.DTO.MedicalRecord.MedicalRecordSearchRequest;
import com.example.clinicbooking.DTO.PaginatedResponseDTO;
import com.example.clinicbooking.entity.MedicalRecord;
import com.example.clinicbooking.entity.MedicalRecordStatus;
import com.example.clinicbooking.service.MedicalRecord.MedicalRecordService;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;


@Tag(name = "MedicalRecord", description = "Quản lý hồ sơ ngoại trú")
@RestController
@RequestMapping("/api/medical-records")
public class MedicalRecordController {

    @Autowired
    private MedicalRecordService recordService;

    @GetMapping("/OfDoctor")
    public ResponseEntity<PaginatedResponseDTO<MedicalRecordResponse>> getDoctorMedicalRecords(
            @RequestParam(required = false) String keyword,
            @RequestParam(required = false) String status,
            @RequestParam(required = false) String curentDate,
            @RequestParam(defaultValue = "0") Integer page,
            @RequestParam(defaultValue = "10") Integer size,
            @RequestParam(defaultValue = "visitNumber") String SortBy,
            @RequestParam(defaultValue = "ASC") String SortDir) {
        MedicalRecordSearchRequest request = new MedicalRecordSearchRequest();
        request.setQuery(keyword);
        request.setStatus(status);
        request.setCurrentDate(curentDate);
        request.setSize(size);
        request.setPage(page);
        request.setSortDir(SortDir);
        request.setSortBy(SortBy);

        PaginatedResponseDTO<MedicalRecordResponse> response = recordService.searchRecords(request);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/overview-metrics")
    public ResponseEntity<MedicalRecordMetricsResponse> getMetricsByDate(@RequestParam String date) {
        MedicalRecordMetricsResponse metrics = recordService.getMetricsByDate(date);
        return ResponseEntity.ok(metrics);
    }

    @PostMapping
    public ResponseEntity<ApiResponse<?>> create(@RequestBody MedicalRecordRequest request) {
        recordService.CreateMedicalRecord(request);
        return ResponseEntity.ok(new ApiResponse<>(true, "Hồ sơ ngoại trú được thêm thành công", null));
    }

    @PutMapping("/{recordId}/in_progress")
    public ResponseEntity<ApiResponse<?>> update_inProgress(@PathVariable Integer recordId) {
        Boolean updateStatus = recordService.UpdateMedicalRecordStatus(recordId, MedicalRecordStatus.IN_PROGRESS.name());
        if(!updateStatus){
            return ResponseEntity.badRequest().body(new ApiResponse<>(false, "Trạng thái hồ sơ không thể cập nhật lại!", null));
        }
        return ResponseEntity.ok(new ApiResponse<>(true, "Cập nhật trạng thái hồ sơ thành công!", null));
    }

    @GetMapping("/patient/{patientId}")
    public ResponseEntity<List<MedicalRecordResponse>> getByPatient(@PathVariable Integer patientId) {
        return ResponseEntity.ok(recordService.getRecordsByPatientId(patientId));
    }

    @GetMapping("/doctor/{doctorId}")
    public ResponseEntity<List<MedicalRecord>> getByDoctor(@PathVariable Integer doctorId) {
        return ResponseEntity.ok(recordService.getRecordsByDoctorId(doctorId));
    }

    @GetMapping("/grouped/patient")
    public ResponseEntity<List<MedicalRecord>> getAllGroupedByPatient() {
        return ResponseEntity.ok(recordService.getAllRecordsGroupedByPatient());
    }

    @GetMapping("/{id}")
    public ResponseEntity<MedicalRecord> getById(@PathVariable Integer id) {
        return recordService.getRecordById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
}

