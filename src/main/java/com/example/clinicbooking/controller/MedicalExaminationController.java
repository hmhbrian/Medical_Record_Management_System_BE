package com.example.clinicbooking.controller;

import com.example.clinicbooking.DTO.MedicalExaminationResponse;
import com.example.clinicbooking.DTO.MedicalRecord.MedicalRecordResponse;
import com.example.clinicbooking.DTO.PaginatedResponseDTO;
import com.example.clinicbooking.entity.Medical_Examination;
import com.example.clinicbooking.service.MedicalExamination.MedicalExaminationService;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Tag(name = "Medical Examination", description = "Quản lý danh mục khám bệnh")
@RestController
@RequestMapping("/api/medical-examinations")
public class MedicalExaminationController {
    @Autowired
    private MedicalExaminationService medicalExaminationService;

    //Endpoint tìm kiếm không phân trang (tùy chọn)
    @GetMapping
    public ResponseEntity<List<MedicalExaminationResponse>> findAllExaminations(
            @RequestParam(name = "keyword", required = false) String keyword) {

        List<MedicalExaminationResponse> results = medicalExaminationService.search(keyword);
        return ResponseEntity.ok(results);
    }
}
