package com.example.clinicbooking.service;

import com.example.clinicbooking.entity.RoomTypes;
import com.example.clinicbooking.repository.RoomTypesRepository;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class RoomTypeService {
    private RoomTypesRepository roomTypesRepository;
    public RoomTypeService(RoomTypesRepository roomTypesRepository) {
        this.roomTypesRepository = roomTypesRepository;
    }

    public List<RoomTypes> findAll() {
        return roomTypesRepository.findAll(Sort.by(Sort.Direction.ASC, "id"));
    }
    public Optional<RoomTypes> findById(int id) {
        return roomTypesRepository.findById(id);
    }
    public RoomTypes save(RoomTypes roomTypes) {
        return roomTypesRepository.save(roomTypes);
    }
    public void deleteById(int id) {
        roomTypesRepository.deleteById(id);
    }
}
