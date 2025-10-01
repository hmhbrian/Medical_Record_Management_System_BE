package com.example.clinicbooking.DTO.Department;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class DepartmentResponse {
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
}
