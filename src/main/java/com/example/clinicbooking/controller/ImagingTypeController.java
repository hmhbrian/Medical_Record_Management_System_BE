package com.example.clinicbooking.controller;

import com.example.clinicbooking.DTO.Services.ImagingTypeResponse;
import com.example.clinicbooking.DTO.Services.TestTypeResponse;
import com.example.clinicbooking.service.ImagingType.ImagingTypeService;
import com.example.clinicbooking.service.TestType.TestTypeService;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Tag(name = "ImagingTypes", description = "Quản lý danh mục loại chẩn đoán hình ảnh")
@RestController
@RequestMapping("/api/ImagingTypes")
public class ImagingTypeController {
    @Autowired
    private ImagingTypeService imagingTypeService;

    //Endpoint tìm kiếm không phân trang (tùy chọn)
    @GetMapping
    public ResponseEntity<List<ImagingTypeResponse>> findAllExaminations(
            @RequestParam(name = "keyword", required = false) String keyword) {

        List<ImagingTypeResponse> results = imagingTypeService.search(keyword);
        return ResponseEntity.ok(results);
    }
}
