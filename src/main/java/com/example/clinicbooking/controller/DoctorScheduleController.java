package com.example.clinicbooking.controller;

import com.example.clinicbooking.DTO.Doctor.DoctorScheduleRequest;
import com.example.clinicbooking.DTO.Doctor.DoctorScheduleResponse;
import com.example.clinicbooking.DTO.PatientInScheduleResponse;
import com.example.clinicbooking.entity.DoctorSchedules;
import com.example.clinicbooking.service.DoctorScheduleService;
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

    @GetMapping("/schedules/{scheduleId}/patients")
    public ResponseEntity<List<PatientInScheduleResponse>> getPatients(@PathVariable int scheduleId) {
        var list = scheduleService.GetPatientOfDoctorSchedule(scheduleId);
        return ResponseEntity.ok(list);
    }
}
