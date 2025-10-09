package com.example.clinicbooking.DTO.Appointment;

import lombok.Data;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
public class AppointmentDTO {
    private int id;
    private int doctorScheduleId;
    private int specialtyId;
    private String code;
    private String patientName;
    private LocalDate patientYearOfBirth;
    private String patientGender;
    private String patientPhone;
    private String patientEmail;
    private String doctorName;
    private String doctorSpecialty;
    private String roomName;
    private String appointmentDate;
    private LocalDateTime presentTime;
    private String appointmentTime;
    private int statusId; // 1: Chờ xác nhận, 2: Đã xác nhận, 3:Hoàn thành, 4: Hủy.
    private String status;
}
