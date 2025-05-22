package com.example.ClinicBooking.DTO;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class AppointmentRequest {
    private int patientId;
    private int doctorId;
    private int doctorScheduleId;
    //private LocalDateTime presentTime;
    private String appointmentTime;
}
