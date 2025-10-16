package com.example.clinicbooking.controller;

import com.example.clinicbooking.DTO.ApiResponse;
import com.example.clinicbooking.DTO.Appointment.AppointmentDTO;
import com.example.clinicbooking.DTO.Appointment.AppointmentRequest;
import com.example.clinicbooking.DTO.Appointment.AppointmentSearchRequest;
import com.example.clinicbooking.DTO.Appointment.AppointmentStatusUpdateRequest;
import com.example.clinicbooking.DTO.Staff.StaffResponse;
import com.example.clinicbooking.service.AppointmentService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/appointments")
@Tag(name = "Appointment", description = "Quản lý lịch hẹn khám bệnh(1: Chờ xác nhận, 2: Đã xác nhận, 3:Hoàn thành, 4: Hủy.)")
@RequiredArgsConstructor
public class AppointmentController {
    private final AppointmentService appointmentService;
    @GetMapping("/find-all")
    public ResponseEntity<ApiResponse<Page<AppointmentDTO>>> searchAppointments(
            @RequestParam(required = false) String keyword,
            @RequestParam(required = false) Integer status,
            @RequestParam(required = false) Integer departmentId,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fromDate,
            @RequestParam(defaultValue = "0") Integer page,
            @RequestParam(defaultValue = "10") Integer size
    ) {
        AppointmentSearchRequest req = new AppointmentSearchRequest();
        req.setKeyword(keyword);
        req.setStatus(status);
        req.setDepartmentId(departmentId);
        req.setFromDate(fromDate);
        req.setPage(page);
        req.setSize(size);

        //return ResponseEntity.ok(appointmentService.searchAppointments(req));
        Page<AppointmentDTO> result = appointmentService.searchAppointments(req);
        return ResponseEntity.ok(new ApiResponse<>(true, "Lấy danh sách lịch hẹn thành công!", result));

    }

    @PostMapping
    public ResponseEntity<AppointmentDTO> bookAppointment(@RequestBody AppointmentRequest request) {
        return ResponseEntity.ok(appointmentService.bookAppointment(request));
    }

    @GetMapping("/patient/{patientId}")
    public ResponseEntity<List<AppointmentDTO>> getAppointments(@PathVariable int patientId) {
        return ResponseEntity.ok(appointmentService.getAppointmentsByPatient(patientId));
    }

    @GetMapping("/schedule/{doctorScheduleId}")
    public ResponseEntity<List<AppointmentDTO>> getAppointmentsBySchedule(@PathVariable int doctorScheduleId) {
        return ResponseEntity.ok(appointmentService.getAppointmentsByDoctorSchedule(doctorScheduleId));
    }

    @PutMapping("{AppointmentId}/confirm")
    public ResponseEntity<ApiResponse<?>> ConfirmAppointment(@PathVariable int AppointmentId, @RequestBody int UpdatedByUserId) {
        appointmentService.ConfirmAppointment(
                AppointmentId,
                UpdatedByUserId
        );
        return ResponseEntity.ok(new ApiResponse<>(true, "Xác nhận lịch hẹn thành công!", null));
    }

    @PutMapping("{AppointmentId}/cancel")
    public ResponseEntity<ApiResponse<?>> CancelAppointment(@PathVariable int AppointmentId, @RequestBody AppointmentStatusUpdateRequest request) {
        appointmentService.DeleteAppointment(
                AppointmentId,
                request.getUpdatedByUserId(),
                request.getReason()
        );
        return ResponseEntity.ok(new ApiResponse<>(true, "Hủy lịch hẹn thành công!", null));
    }

    @GetMapping("/status/{status}")
    public ResponseEntity<List<AppointmentDTO>> getAppointmentsByStatus(@PathVariable int status) {
        return ResponseEntity.ok(appointmentService.getAppointmentsByStatus(status));
    }

    @GetMapping("/week")
    public ResponseEntity<List<AppointmentDTO>> getAppointmentsByWeek(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate) {
        return ResponseEntity.ok(appointmentService.getAppointmentsByWeek(startDate));
    }

    @GetMapping("/doctor/{userId}")
    public ResponseEntity<List<AppointmentDTO>> getAppointmentsByDoctor(@PathVariable int userId) {
        return ResponseEntity.ok(appointmentService.getAppointmentsByDoctor(userId));
    }
}
