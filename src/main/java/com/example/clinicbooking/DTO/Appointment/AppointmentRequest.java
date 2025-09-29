package com.example.clinicbooking.DTO.Appointment;

import lombok.Data;

@Data
public class AppointmentRequest {
    private int patientId;
    private int doctorId;
    private int doctorScheduleId;
    //private LocalDateTime presentTime;
    private String appointmentTime;
}
