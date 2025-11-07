package com.example.clinicbooking.DTO.LabTest.Detail;

import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
public class LabTestDetailRequest {
    private String result;
    private Boolean finalizeResult; //true if final, false if draft
    private List<ParameterDetailRequest> parameters;

}
