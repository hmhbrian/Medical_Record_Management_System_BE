package com.example.ClinicBooking.service;

import com.example.ClinicBooking.Domain.Entities.RoomTypes;
import com.example.ClinicBooking.Infrastructure.Repository.RoomTypesRepository;
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
        return roomTypesRepository.findAll();
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
