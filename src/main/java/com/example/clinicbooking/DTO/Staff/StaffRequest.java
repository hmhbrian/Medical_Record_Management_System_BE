package com.example.clinicbooking.DTO.Staff;

import com.example.clinicbooking.DTO.User.BaseUserRequest;
import lombok.Data;

import java.time.LocalDate;

@Data
public class StaffRequest extends BaseUserRequest {
    // Staff
    public int departmentId;
    public int positionId;
    // nurse, receptionist, technician,...
    public int experienceYears;;
}
