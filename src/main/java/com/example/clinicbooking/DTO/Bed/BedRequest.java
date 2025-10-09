package com.example.clinicbooking.DTO.Bed;

import lombok.Data;

@Data
public class BedRequest {
    private int room_id;
    private String bed_number;
    private double bed_fee;
    private Integer status; // 1: Available, 0: Occupied, 2: Maintenance
}
