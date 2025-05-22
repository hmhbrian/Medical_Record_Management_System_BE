package com.example.ClinicBooking.DTO;

import lombok.Data;

import java.time.LocalDate;
import java.time.LocalTime;

@Data
public class DoctorScheduleResponse {
    private int id;
    private LocalDate date;
    private String status;
    private String shift;
    private LocalTime start_time;
    private LocalTime end_time;
    private String location;
    private int maxPatients;
    private int bookedPatients;
}
