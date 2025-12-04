package com.example.clinicbooking.DTO.Dashboard.Doctor;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AppointmentUpcomingDTO {
    private Integer appointmentId;
    private String appointmentCode;
    private String patientName;
    private LocalDate appointmentDate;
    private String appointmentTime;
}
