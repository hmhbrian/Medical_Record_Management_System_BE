package com.example.clinicbooking.DTO.Bed;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class BedResponse {
    private int id;
    private String bed_number;
    private String departmentName;
    private String room_name; // ví dụ: "Bed A01"
    private String bedType_name; // tên room type
    private String bedStatus; // "Available" | "Occupied" | "Maintenance"
    private String patient_name;
    private String patient_code;
    private LocalDateTime updated_at;
    private String bed_fee;
}
