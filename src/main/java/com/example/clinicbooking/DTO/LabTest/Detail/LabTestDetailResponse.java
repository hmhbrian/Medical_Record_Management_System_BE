package com.example.clinicbooking.DTO.LabTest.Detail;

import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
public class LabTestDetailResponse {
    private Integer labTestId;
    private String testName;
    private LocalDateTime resultDate;
    private String result;
    private String status;
    private LocalDateTime requestedDate;
    private String doctorInChargeName;
    private String patientName;
    private List<ParameterDetailResponse> parameters;
}
