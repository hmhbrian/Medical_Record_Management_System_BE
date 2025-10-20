package com.example.clinicbooking.DTO.Room;

import lombok.Data;

@Data
public class RoomUpdateRequest {
    private String name;
    private String roomNumber;
    private String description;

    private String roomStatus;  //"AVAILABLE", "OCCUPIED"

    private Integer departmentId;
    private Integer roomTypeId;
}
