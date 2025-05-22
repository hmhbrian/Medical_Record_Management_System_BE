package com.example.ClinicBooking.controller;

import com.example.ClinicBooking.DTO.AppointmentDTO;
import com.example.ClinicBooking.DTO.AppointmentRequest;
import com.example.ClinicBooking.DTO.AppointmentStatusUpdateRequest;
import com.example.ClinicBooking.service.AppointmentService;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/appointments")
@RequiredArgsConstructor
public class AppointmentController {
    private final AppointmentService appointmentService;

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

    @PostMapping("/status")
    public ResponseEntity<String> updateAppointmentStatus(@RequestBody AppointmentStatusUpdateRequest request) {
        appointmentService.updateAppointmentStatus(
                request.getAppointmentId(),
                request.getStatus(),
                request.getUpdatedByUserId(),
                request.getReason()
        );
        return ResponseEntity.ok("Appointment status updated successfully.");
    }

    @GetMapping("/status/{status}")
    public ResponseEntity<List<AppointmentDTO>> getAppointmentsByStatus(@PathVariable String status) {
        return ResponseEntity.ok(appointmentService.getAppointmentsByStatus(status));
    }

    @GetMapping("/week")
    public ResponseEntity<List<AppointmentDTO>> getAppointmentsByWeek(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate) {
        return ResponseEntity.ok(appointmentService.getAppointmentsByWeek(startDate));
    }

    @GetMapping("/doctor/{doctorId}")
    public ResponseEntity<List<AppointmentDTO>> getAppointmentsByDoctor(@PathVariable int doctorId) {
        return ResponseEntity.ok(appointmentService.getAppointmentsByDoctor(doctorId));
    }
}
