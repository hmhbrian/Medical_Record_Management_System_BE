package com.example.clinicbooking.DTO.Department;

import com.example.clinicbooking.DTO.Specialty.SpecialtyResponse;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class DepartmentRpDetail {
    private int id;
    private String name;
    private String description;
    private String contact;
    private Date establishment_date;
    private int status; //0: inactive, 1: active
    private String head_doctor_name;
    private int number_of_staff;
    private int number_of_specialties;
    private int number_of_rooms;
    private List<SpecialtyResponse> specialties;
}
