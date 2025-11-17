package com.example.clinicbooking.DTO.Doctor;

import lombok.Data;

import java.util.List;

@Data
public class DoctorByComplaintResponse {
    private Integer doctorId;
    private String doctorName;
    private String specialtyName;
    private List<DoctorScheduleResponse> schedules;
}
