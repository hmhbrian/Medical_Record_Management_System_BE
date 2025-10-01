package com.example.clinicbooking.DTO.Doctor;

import lombok.Data;

import java.util.List;

@Data
public class DrScheduleSummaryRp {
    int number_schedules;
    int number_patients;
    int total_capacity;
    double usage_rate;
    List<DoctorScheduleResponse> schedules;
}
