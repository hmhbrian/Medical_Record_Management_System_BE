package com.example.clinicbooking.controller;

import com.example.clinicbooking.DTO.ApiResponse;
import com.example.clinicbooking.DTO.MedicalRecord.DiagnosisData.DiagnosisDataResponse;
import com.example.clinicbooking.DTO.MedicalRecord.DiagnosisData.DiagnosisUpdateRequest;
import com.example.clinicbooking.DTO.MedicalRecord.MedicalRecordMetricsResponse;
import com.example.clinicbooking.DTO.MedicalRecord.MedicalRecordRequest;
import com.example.clinicbooking.DTO.MedicalRecord.MedicalRecordResponse;
import com.example.clinicbooking.DTO.MedicalRecord.MedicalRecordSearchRequest;
import com.example.clinicbooking.DTO.MedicalRecord.ServiceData.ServiceOrderResponse;
import com.example.clinicbooking.DTO.MedicalRecord.ServiceData.ServiceOrdersRequest;
import com.example.clinicbooking.DTO.PaginatedResponseDTO;
import com.example.clinicbooking.entity.MedicalRecord;
import com.example.clinicbooking.entity.MedicalRecordStatus;
import com.example.clinicbooking.service.MedicalRecord.MedicalRecordService;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
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

    //Lấy danh sách hồ sơ ngoại trú của bác sĩ với phân trang và lọc
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

    // Thống kê hồ sơ ngoại trú theo ngày
    @GetMapping("/overview-metrics")
    public ResponseEntity<MedicalRecordMetricsResponse> getMetricsByDate(@RequestParam String date) {
        MedicalRecordMetricsResponse metrics = recordService.getMetricsByDate(date);
        return ResponseEntity.ok(metrics);
    }

    // Tạo mới hồ sơ ngoại trú
    @PostMapping
    public ResponseEntity<ApiResponse<?>> create(@RequestBody MedicalRecordRequest request) {
        recordService.CreateMedicalRecord(request);
        return ResponseEntity.ok(new ApiResponse<>(true, "Hồ sơ ngoại trú được tạo thành công", null));
    }

    // Cập nhật trạng thái hồ sơ ngoại trú thành "Đang tiến hành"
    @PutMapping("/{recordId}/in_progress")
    public ResponseEntity<ApiResponse<?>> update_inProgress(@PathVariable Integer recordId) {
        Boolean updateStatus = recordService.UpdateMedicalRecordStatus(recordId, MedicalRecordStatus.IN_PROGRESS.name());
        if(!updateStatus){
            return ResponseEntity.badRequest().body(new ApiResponse<>(false, "Trạng thái hồ sơ không thể cập nhật lại!", null));
        }
        return ResponseEntity.ok(new ApiResponse<>(true, "Cập nhật trạng thái hồ sơ thành công!", null));
    }

    // Lấy hồ sơ ngoại trú theo bệnh nhân
    @GetMapping("/patient/{patientId}")
    public ResponseEntity<List<MedicalRecordResponse>> getByPatient(@PathVariable Integer patientId) {
        return ResponseEntity.ok(recordService.getRecordsByPatientId(patientId));
    }

    // Lấy hồ sơ ngoại trú theo bác sĩ
    @GetMapping("/doctor/{doctorId}")
    public ResponseEntity<List<MedicalRecordResponse>> getByDoctor(@PathVariable Integer doctorId) {
        return ResponseEntity.ok(recordService.getRecordsByDoctorId(doctorId));
    }

    // Lấy tất cả hồ sơ ngoại trú, nhóm theo bệnh nhân
    @GetMapping("/grouped/patient")
    public ResponseEntity<List<MedicalRecord>> getAllGroupedByPatient() {
        return ResponseEntity.ok(recordService.getAllRecordsGroupedByPatient());
    }

    // Lấy hồ sơ ngoại trú theo ID
    @GetMapping("/{id}")
    public ResponseEntity<MedicalRecord> getById(@PathVariable Integer id) {
        return recordService.getRecordById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    //Cập nhật chẩn đoán cho hồ sơ ngoại trú.
    @PutMapping("/{recordId}/diagnosis")
    public ResponseEntity<ApiResponse<?>> updateDiagnosis(
            @PathVariable Integer recordId,
            @RequestBody DiagnosisUpdateRequest dto) {

        recordService.updateDiagnosis(recordId, dto);
        return ResponseEntity.ok(new ApiResponse<>(true, "Cập nhật chẩn đoán thành công", null));
    }

    //Lấy dữ liệu để hiển thị Tab 1: Khám & Chẩn đoán.
    @GetMapping("/{recordId}/diagnosis-data")
    public ResponseEntity<ApiResponse<DiagnosisDataResponse>> getDiagnosisData(
            @PathVariable Integer recordId) {

        DiagnosisDataResponse response = recordService.getDiagnosisData(recordId);

        // Xử lý NotFoundException bằng ControllerAdvice hoặc ExceptionHandler
        if (response == null) {
            return ResponseEntity.ok(new ApiResponse<>(true, "Lấy dữ liệu khám & chẩn đoán thất bại", null));
        }

        return ResponseEntity.ok(new ApiResponse<>(true, "Lấy dữ liệu khám & chẩn đoán thành công", response));
    }

    //Tạo các chỉ định xét nghiệm/hình ảnh và phiếu thanh toán liên quan.
    @PostMapping("/{recordId}/service-orders")
    public ResponseEntity<ApiResponse<?>> createServiceOrders(
            @PathVariable Integer recordId,
            @RequestBody ServiceOrdersRequest dto) {

        // Kiểm tra dữ liệu đầu vào cơ bản
        if ((dto.getLabTestCatalogIds() == null || dto.getLabTestCatalogIds().isEmpty()) &&
                (dto.getImagingTypeIds() == null || dto.getImagingTypeIds().isEmpty())) {
            return ResponseEntity.badRequest().body(new ApiResponse<>(false, "Cần chỉ định ít nhất một dịch vụ.", null));
        }

        // Trả về 201 Created (hoặc 200 OK)
        return new ResponseEntity<>(recordService.createServiceOrders(recordId, dto), HttpStatus.CREATED);
    }

    //Lấy danh sách các chỉ định xét nghiệm/hình ảnh đã tạo cho Tab 2.
    @GetMapping("/{recordId}/service-orders")
    public ResponseEntity<List<ServiceOrderResponse>> getServiceOrders(
            @PathVariable Integer recordId) {

        List<ServiceOrderResponse> response = recordService.getServiceOrders(recordId);

        return ResponseEntity.ok(response);
    }
}

