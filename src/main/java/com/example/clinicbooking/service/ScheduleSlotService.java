package com.example.clinicbooking.service;

import com.example.clinicbooking.DTO.Doctor.ScheduleslotRp;
import com.example.clinicbooking.entity.ScheduleSlot;
import com.example.clinicbooking.repository.ScheduleSlotRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ScheduleSlotService {
    @Autowired
    private ScheduleSlotRepository scheduleSlotRepo;

    public List<ScheduleslotRp> findByScheduleDoctorId(int scheduleId) {
        List<ScheduleSlot> scheduleSlots = scheduleSlotRepo.findByDoctorScheduleIdAndIsBookedFalse(scheduleId);
        return scheduleSlots
                .stream()
                .map(this::covertToResponse)
                .collect(Collectors.toList());
    }

    private ScheduleslotRp covertToResponse(ScheduleSlot scheduleSlot) {
        ScheduleslotRp dto = new ScheduleslotRp();
        dto.setScheduleSlotId(scheduleSlot.getId());
        dto.setStartTime(scheduleSlot.getStartTime().toString());
        dto.setEndTime(scheduleSlot.getEndTime().toString());
        return dto;
    }
}
