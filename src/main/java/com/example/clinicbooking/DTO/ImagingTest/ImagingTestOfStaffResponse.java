package com.example.clinicbooking.DTO.ImagingTest;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class ImagingTestOfStaffResponse extends ImagingTestWaitingResponse {
    private String result;
    private LocalDateTime resultDate;
}
