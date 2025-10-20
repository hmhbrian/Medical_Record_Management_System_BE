package com.example.clinicbooking.controller;

import com.example.clinicbooking.DTO.ShiftTypeResponse;
import com.example.clinicbooking.service.ShiftTypeService;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;


@Tag(name = "ShiftType", description = "Quản lý các ca làm việc")
@RestController
@RequestMapping("/api/shift-types")
public class ShiftTypeController {
    private final ShiftTypeService shiftTypeService;

    public ShiftTypeController(ShiftTypeService shiftTypeService) {
        this.shiftTypeService = shiftTypeService;
    }

    @GetMapping
    public ResponseEntity<List<ShiftTypeResponse>> getAll() {
        return ResponseEntity.ok(shiftTypeService.getAll());
    }
}
