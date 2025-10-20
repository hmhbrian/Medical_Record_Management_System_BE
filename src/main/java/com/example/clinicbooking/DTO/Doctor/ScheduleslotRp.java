package com.example.clinicbooking.DTO.Doctor;

import lombok.Data;

@Data
public class ScheduleslotRp {
    public Integer scheduleSlotId;
    public String startTime;
    public String endTime;
}
