package com.example.clinicbooking.DTO.Appointment;

import lombok.Data;

@Data
public class AppointmentStatusUpdateRequest {
    private String reason;
    private int updatedByUserId;
}
