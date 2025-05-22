package com.example.ClinicBooking.controller;

import com.example.ClinicBooking.DTO.ShiftTypeResponse;
import com.example.ClinicBooking.service.ShiftTypeService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

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
