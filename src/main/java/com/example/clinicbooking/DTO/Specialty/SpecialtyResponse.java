package com.example.clinicbooking.DTO.Specialty;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SpecialtyResponse {
    private int id;
    private String name;
    private String description;
    private String icon;
    private String departmentName;
    private int departmentId;
    private int numberOfDoctors;
}
