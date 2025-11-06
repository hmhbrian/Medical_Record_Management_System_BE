package com.example.clinicbooking.DTO.LabTest;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class LabTestOfStaffResponse extends LabTestWaitingResponse{
    private String result;
    private LocalDateTime resultDate;
}
