package com.example.clinicbooking.DTO.Prescription;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class PrescriptionOfStaffResponse extends PrescriptionWaitingResponse{
    private LocalDateTime dipenseDate;
}
