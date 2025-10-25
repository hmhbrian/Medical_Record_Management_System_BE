package com.example.clinicbooking.DTO.Room;

import lombok.Data;

@Data
public class RoomRequest {
    private String roomNumber;
    private String name;
    private int departmentId;
    private int roomTypeId;
    private String description;
    private String roomStatus;
    private int roomCapacity;
}
