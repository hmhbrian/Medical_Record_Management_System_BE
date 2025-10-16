package com.example.clinicbooking.controller;

import com.example.clinicbooking.DTO.ApiResponse;
import com.example.clinicbooking.DTO.Doctor.DoctorScheduleRequest;
import com.example.clinicbooking.DTO.Doctor.DoctorScheduleResponse;
import com.example.clinicbooking.DTO.Doctor.DrScheduleSummaryRp;
import com.example.clinicbooking.DTO.PatientInScheduleResponse;
import com.example.clinicbooking.entity.DoctorSchedules;
import com.example.clinicbooking.service.DoctorScheduleService;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

@RestController
@RequestMapping("/api/schedules")
@Tag(name = "DoctorSchedule", description = "Quản lý lịch làm việc của bác sĩ")
public class DoctorScheduleController {
    @Autowired
    private DoctorScheduleService scheduleService;

    @Tag(name = "", description = "Thêm lịch làm việc mới")
    @PostMapping
    public ResponseEntity<ApiResponse<?>> assignSchedule(@RequestBody DoctorScheduleRequest request) {
        DoctorSchedules savedSchedule = scheduleService.assignSchedule(request);
        return ResponseEntity.ok(new ApiResponse<>(true, "Thêm lịch làm việc thành công!", null));
    }

//    @GetMapping("/specialty/{specialtyId}")
//    public ResponseEntity<List<DoctorScheduleResponse>> getSchedulesBySpecialty(@PathVariable int specialtyId) {
//        return ResponseEntity.ok(scheduleService.getSchedulesBySpecialty(specialtyId));
//    }

    @Tag(name = "", description = "Lấy danh sách lịch làm việc theo doctor và ngày bắt đầu trong tuần")
    @GetMapping
    public ResponseEntity<DrScheduleSummaryRp> getSchedulesByDoctor(
            @RequestParam int doctorId,
            @RequestParam String weekStart
    ) {
        LocalDate startDate = LocalDate.parse(weekStart, DateTimeFormatter.ISO_LOCAL_DATE);
        return ResponseEntity.ok(scheduleService.getSchedulesByDoctorAndWeek(doctorId, startDate));
    }

    @Tag(name = "", description = "Lấy danh sách lịch làm việc của doctor đang login và ngày bắt đầu trong tuần")
    @GetMapping("/OfDoctor")
    public ResponseEntity<DrScheduleSummaryRp> getSchedulesOfDoctor(
            @RequestParam String weekStart
    ) {
        LocalDate startDate = LocalDate.parse(weekStart, DateTimeFormatter.ISO_LOCAL_DATE);
        return ResponseEntity.ok(scheduleService.getSchedulesOfDoctorAndWeek(startDate));
    }

//    @GetMapping("/doctor/{doctorId}")
//    public ResponseEntity<List<DoctorScheduleResponse>> getSchedulesByDoctor(@PathVariable int doctorId) {
//        List<DoctorScheduleResponse> schedules = scheduleService.getScheduleByDoctorId(doctorId);
//        return ResponseEntity.ok(schedules);
//    }

    //Lấy danh sách bệnh nhân trong ca làm việc(Doctor đăng nhập)
    @GetMapping("/{scheduleId}/patients")
    public ResponseEntity<List<PatientInScheduleResponse>> getPatients(@PathVariable int scheduleId) {
        var list = scheduleService.GetPatientOfDoctorSchedule(scheduleId);
        return ResponseEntity.ok(list);
    }

    @Tag(name = "", description = "Xóa lịch làm việc")
    @DeleteMapping("/{scheduleId}")
    public ResponseEntity<?> deleteSchedule(@PathVariable int scheduleId) {
        scheduleService.deleteSchedule(scheduleId);
        return ResponseEntity.ok().build();
    }
}
