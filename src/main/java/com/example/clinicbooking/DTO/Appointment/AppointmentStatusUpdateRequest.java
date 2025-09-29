package com.example.clinicbooking.DTO.Appointment;

import lombok.Data;

@Data
public class AppointmentStatusUpdateRequest {
    private int appointmentId;
    private String status;
    private String reason;
    private int updatedByUserId;
}
