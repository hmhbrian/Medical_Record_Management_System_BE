package com.example.clinicbooking.DTO.Room;

import lombok.Data;

@Data
public class RoomResponse {
    private int id;
    private String name;
    private String roomNumber;
    private String description;
    private String roomStatus;
    private int departmentId;
    private String departmentName;
    private int roomTypeId;
    private String roomTypeName;
    private int roomCapacity;
}
