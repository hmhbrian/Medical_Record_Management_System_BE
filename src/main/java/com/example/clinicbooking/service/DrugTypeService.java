package com.example.clinicbooking.service;

import com.example.clinicbooking.entity.DrugType;
import com.example.clinicbooking.entity.RoomTypes;
import com.example.clinicbooking.repository.DrugTypeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class DrugTypeService {
    @Autowired
    private DrugTypeRepository drugTypeRepo;

    public List<DrugType> findAll() {
        return drugTypeRepo.findAll(Sort.by(Sort.Direction.ASC, "id"));
    }
    public Optional<DrugType> findById(int id) {
        return drugTypeRepo.findById(id);
    }
    public DrugType save(DrugType drugType) {
        return drugTypeRepo.save(drugType);
    }
    public void deleteById(int id) {
        drugTypeRepo.deleteById(id);
    }
}
