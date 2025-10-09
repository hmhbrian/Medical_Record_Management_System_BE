package com.example.clinicbooking.service;

import com.example.clinicbooking.DTO.Room.RoomRequest;
import com.example.clinicbooking.DTO.Room.RoomResponse;
import com.example.clinicbooking.entity.Department;
import com.example.clinicbooking.entity.Room;
import com.example.clinicbooking.entity.RoomTypes;
import com.example.clinicbooking.entity.Specialty;
import com.example.clinicbooking.repository.DepartmentRepository;
import com.example.clinicbooking.repository.RoomTypesRepository;
import com.example.clinicbooking.repository.SpecialtyRepository;
import com.example.clinicbooking.repository.roomRepository;
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
    @Autowired
    private DepartmentRepository departmentRepository;
    @Autowired
    private RoomTypesRepository roomTypesRepository;

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

    public Room Create(RoomRequest newRoom) {
        Department department = departmentRepository.findById(newRoom.getDepartmentId())
                .orElseThrow(() -> new RuntimeException("Department not found"));

        RoomTypes roomTypes = roomTypesRepository.findById(newRoom.getRoomTypeId())
                .orElseThrow(() -> new RuntimeException("RoonType not found"));

        Room room = new Room();
        room.setName(newRoom.getName());
        room.setDepartment(department);
        room.setRoomType(roomTypes);

        return roomRepository.save(room);
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
