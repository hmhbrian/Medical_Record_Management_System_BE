package com.example.clinicbooking.controller;

import com.example.clinicbooking.entity.staff_position;
import com.example.clinicbooking.service.StaffPositionService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/staff-positions")
public class StaffPositionController {
    private StaffPositionService staffPositionService;

    public StaffPositionController(StaffPositionService staffPositionService) {
        this.staffPositionService = staffPositionService;
    }

    @GetMapping
    public List<staff_position> getAllStafPosition() {
        return staffPositionService.getAllPosition();
    }

}
