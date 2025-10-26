package com.example.clinicbooking.DTO.StaffSchedule;

import lombok.Data;

import java.time.LocalDate;
import java.time.LocalTime;

@Data
public class StaffScheduleResponse {
    private int id;
    private LocalDate date;
    private String status;
    private String shift;
    private LocalTime start_time;
    private LocalTime end_time;
    private String location;
}
