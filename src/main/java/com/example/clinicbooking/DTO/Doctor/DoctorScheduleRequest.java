package com.example.clinicbooking.DTO.Doctor;

import lombok.Data;

import java.time.LocalDate;

@Data
public class DoctorScheduleRequest {
    private int doctorId;
    private int shiftTypeId;
    private int roomId;
    private LocalDate date;
    private String status;
    private int maxPatients;
}
