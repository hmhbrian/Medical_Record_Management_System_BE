package com.example.ClinicBooking.service;

import com.example.ClinicBooking.DTO.ShiftTypeResponse;
import com.example.ClinicBooking.entity.Shift_type;
import com.example.ClinicBooking.repository.ShiftTypeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ShiftTypeService {
    @Autowired
    private ShiftTypeRepository shiftTypeRepository;

    public List<ShiftTypeResponse> getAll() {
        return shiftTypeRepository.findAll().stream()
                .map(this::convertToResponse)
                .collect(Collectors.toList());
    }

    private ShiftTypeResponse convertToResponse(Shift_type shiftType) {
        ShiftTypeResponse response = new ShiftTypeResponse();
        response.id = shiftType.getId();
        response.name = shiftType.getName_type();
        return response;
    }
}
