package com.example.clinicbooking.DTO.Specialty;

import com.example.clinicbooking.entity.Specialty;
import lombok.Data;

@Data
public class SpecialtyRequest {
    private String name;
    private String description;
    private String icon;
    private int departmentId;
}
