package com.example.clinicbooking.DTO.StaffSchedule;

import lombok.Data;

import java.time.LocalDate;
import java.util.List;

@Data
public class StaffsScheduleRequest {
    private List<Integer> staffIds;
    private int shiftTypeId;
    private int roomId;
    private LocalDate date;
    private String status;
}
