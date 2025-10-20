package com.example.clinicbooking.controller;

import com.example.clinicbooking.DTO.ApiResponse;
import com.example.clinicbooking.DTO.Doctor.DrScheduleSummaryRp;
import com.example.clinicbooking.DTO.Doctor.ScheduleslotRp;
import com.example.clinicbooking.service.ScheduleSlotService;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

@RestController
@RequestMapping("/api/scheduleslots")
@Tag(name = "ScheduleSlot", description = "Quản lý thời gian trong lịch làm việc của bác sĩ")
public class ScheduleSlotController {
    @Autowired
    private ScheduleSlotService scheduleSlotService;

    @GetMapping("/{scheduleSlotId}/availability")
    public ResponseEntity<ApiResponse<List<ScheduleslotRp>>> getSchedulesOfDoctor(@PathVariable Integer scheduleSlotId) {
        List<ScheduleslotRp> scheduleslotRps = scheduleSlotService.findByScheduleDoctorId(scheduleSlotId);
        return ResponseEntity.ok(new ApiResponse<>(true, "Lấy danh sách khung giờ thành công", scheduleslotRps));
    }
}
