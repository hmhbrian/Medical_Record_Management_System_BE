package com.example.ClinicBooking.DTO;

import lombok.Data;

@Data
public class RoomResponse {
    private int id;
    private String name;
    private String departmentName;
    private String roomTypeName;
}
