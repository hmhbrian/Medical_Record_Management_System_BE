package com.example.clinicbooking.DTO.LabParameter;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class LabParameterDTO {
    private Integer parameterId;
    private String name;
    private String unit;
    private String minReference;
    private String maxReference;
}
