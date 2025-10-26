package com.example.clinicbooking.DTO.StaffSchedule;

import lombok.Data;

import java.time.LocalDate;

@Data
public class StaffScheduleRequest {
    private int staffId;
    private int shiftTypeId;
    private int roomId;
    private LocalDate date;
    private String status;
}
