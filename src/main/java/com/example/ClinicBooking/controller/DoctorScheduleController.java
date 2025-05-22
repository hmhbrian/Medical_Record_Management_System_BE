package com.example.ClinicBooking.controller;

import com.example.ClinicBooking.DTO.DoctorScheduleRequest;
import com.example.ClinicBooking.DTO.DoctorScheduleResponse;
import com.example.ClinicBooking.entity.DoctorSchedules;
import com.example.ClinicBooking.service.DoctorScheduleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

@RestController
@RequestMapping("/api/schedules")
public class DoctorScheduleController {
    @Autowired
    private DoctorScheduleService scheduleService;

    @PostMapping
    public ResponseEntity<?> assignSchedule(@RequestBody DoctorScheduleRequest request) {
        DoctorSchedules savedSchedule = scheduleService.assignSchedule(request);
        return ResponseEntity.ok(savedSchedule);
    }

    @GetMapping("/doctor/{doctorId}")
    public ResponseEntity<List<DoctorScheduleResponse>> getSchedulesByDoctor(@PathVariable int doctorId) {
        List<DoctorScheduleResponse> schedules = scheduleService.getScheduleByDoctorId(doctorId);
        return ResponseEntity.ok(schedules);
    }

    @GetMapping("/specialty/{specialtyId}")
    public ResponseEntity<List<DoctorScheduleResponse>> getSchedulesBySpecialty(@PathVariable int specialtyId) {
        return ResponseEntity.ok(scheduleService.getSchedulesBySpecialty(specialtyId));
    }

    @GetMapping
    public ResponseEntity<List<DoctorScheduleResponse>> getSchedulesByDoctor(
            @RequestParam int doctorId,
            @RequestParam String weekStart
    ) {
        LocalDate startDate = LocalDate.parse(weekStart, DateTimeFormatter.ISO_LOCAL_DATE);
        return ResponseEntity.ok(scheduleService.getSchedulesByDoctorAndWeek(doctorId, startDate));
    }
}
