package com.example.ClinicBooking.DTO;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class AppointmentDTO {
    private int id;
    private int patientId;
    private String patientName;
    private int doctorId;
    private String doctorName;
    private int doctorScheduleId;
    private String roomName;
    private String appointmentDate;
    private LocalDateTime presentTime;
    private String appointmentTime;
    private String status;
}
