package com.example.clinicbooking.DTO.LabTest;

import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
public class LabTestDetailResponse {
    private Integer labTestId;
    private String testName;
    private LocalDateTime resultDate;
    private String result;
    private List<ParameterDetailResponse> parameters;
}
