package com.example.clinicbooking.DTO.LabParameter;

import lombok.Data;

import java.util.List;

@Data
public class TestTypeParameterDetailDTO {
    private Integer testTypeId;
    private String testName;
    private List<LabParameterDTO> parameters;
}
