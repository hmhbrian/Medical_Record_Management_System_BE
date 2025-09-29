package com.example.clinicbooking.DTO.Room;

import lombok.Data;

@Data
public class RoomRequest {
    private String name;
    private int departmentId;
    private int roomTypeId;
}
