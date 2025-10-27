package com.example.clinicbooking.controller;

import com.example.clinicbooking.DTO.ApiResponse;
import com.example.clinicbooking.DTO.StaffSchedule.StaffScheduleRequest;
import com.example.clinicbooking.DTO.StaffSchedule.StaffScheduleResponse;
import com.example.clinicbooking.DTO.StaffSchedule.StaffsScheduleRequest;
import com.example.clinicbooking.service.StaffScheduleService;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@Tag(name = "Staff-Schedules", description = "Quản lý lịch làm việc nhân viên y tế")
@RestController
@RequestMapping("/api/staff-schedules")
public class StaffSchedulesController {
    @Autowired
    private StaffScheduleService staffScheduleService;

    // Thêm lịch làm việc đơn cho nhân viên y tế
    @PostMapping("/add-single")
    public ResponseEntity<ApiResponse<?>> addSingleSchedule(
            @Valid @RequestBody StaffScheduleRequest requestDTO) {

        return ResponseEntity.ok(staffScheduleService.addSingleSchedule(requestDTO));
    }

    // Thêm lịch làm việc cho nhiều nhân viên y tế
    @PostMapping("/add-bulk")
    public ResponseEntity<ApiResponse<?>> addBulkSchedule(
            @Valid @RequestBody StaffsScheduleRequest requestDTO) {

        return ResponseEntity.ok(staffScheduleService.addBulkSchedule(requestDTO));
    }

    @GetMapping("/staff/{staffId}")
    public ResponseEntity<List<StaffScheduleResponse>> getStaffSchedules(
            @PathVariable Integer staffId,
            @RequestParam("start_date") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate) {

        List<StaffScheduleResponse> schedules = staffScheduleService.getStaffSchedules(staffId, startDate);

        if (schedules.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok(schedules);
    }

    @GetMapping("/staff-login")
    public ResponseEntity<List<StaffScheduleResponse>> getSchedulesOfStaffLogin(
            @RequestParam("start_date") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate) {

        List<StaffScheduleResponse> schedules = staffScheduleService.getSchedulesOfStaffLogin(startDate);

        if (schedules.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok(schedules);
    }
}
