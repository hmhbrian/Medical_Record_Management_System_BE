package com.example.clinicbooking.DTO.Doctor;

import lombok.Data;

import java.time.LocalDate;
import java.time.LocalTime;

@Data
public class DoctorScheduleResponse {
    private int id;
    private String shift;
    private LocalDate date;
    private LocalTime start_time;
    private LocalTime end_time;
    private String location;
    private int maxPatients;
    private int bookedPatients;
}
