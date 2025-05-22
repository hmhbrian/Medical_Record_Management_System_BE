package com.example.ClinicBooking.service;

import com.example.ClinicBooking.DTO.RoomResponse;
import com.example.ClinicBooking.entity.Room;
import com.example.ClinicBooking.entity.Specialty;
import com.example.ClinicBooking.repository.SpecialtyRepository;
import com.example.ClinicBooking.repository.roomRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class RoomService {
    @Autowired
    private roomRepository roomRepository;
    @Autowired
    private SpecialtyRepository specialtyRepository;

    public List<RoomResponse> getRoomsBySpecialty(int specialtyId) {
        Specialty specialty = specialtyRepository.findById(specialtyId)
                .orElseThrow(() -> new RuntimeException("Specialty not found"));

        int departmentId = specialty.getDepartment().getId();

        List<Room> rooms = roomRepository.findByDepartmentId(departmentId);
        if (rooms.isEmpty()) {
            throw new RuntimeException("No rooms found for this specialty's department");
        }

        return rooms.stream()
                .map(this::convertToResponse)
                .collect(Collectors.toList());
    }

    public List<RoomResponse> getAllRooms() {
        return roomRepository.findAll().stream()
                .map(this::convertToResponse)
                .collect(Collectors.toList());
    }

    private RoomResponse convertToResponse(Room room) {
        RoomResponse response = new RoomResponse();
        response.setId(room.getId());
        response.setName(room.getName());
        response.setDepartmentName(room.getDepartment().getName());
        response.setRoomTypeName(room.getRoomType().getName());
        return response;
    }
}
