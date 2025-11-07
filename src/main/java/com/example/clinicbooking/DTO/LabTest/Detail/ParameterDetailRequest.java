package com.example.clinicbooking.DTO.LabTest.Detail;

import lombok.Data;

@Data
public class ParameterDetailRequest {
    private Integer detailId;
    private String resultValue;
    private Boolean isAbnormal;
    private String notes;
}
