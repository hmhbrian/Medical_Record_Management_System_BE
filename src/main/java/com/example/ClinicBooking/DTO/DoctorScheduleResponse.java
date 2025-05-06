package com.example.ClinicBooking.DTO;

import lombok.Data;

import java.time.LocalDate;
import java.time.LocalTime;

@Data
public class DoctorScheduleResponse {
    private LocalDate date;
    private String status;
    private String shiftName;
    private LocalTime start_time;
    private LocalTime end_time;
    private String roomName;
    private int maxPatients;
    private int bookedPatients;
}
