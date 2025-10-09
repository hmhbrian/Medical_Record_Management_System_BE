package com.example.clinicbooking.DTO.Department;

import lombok.Data;

import java.util.Date;

@Data
public class DepartmentRequest {
    private String name;
    private String description;
    private String contact;
    private Date establishment_date;
    private int status; //0: inactive, 1: active
    private int head_doctor_id;
}
