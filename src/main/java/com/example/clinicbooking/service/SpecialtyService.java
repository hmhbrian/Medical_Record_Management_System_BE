package com.example.clinicbooking.service;

import com.example.clinicbooking.DTO.SpecialtyResponse;
import com.example.clinicbooking.entity.Specialty;
import com.example.clinicbooking.repository.SpecialtyRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class SpecialtyService {
    private SpecialtyRepository specialtyRepository;

    public SpecialtyService(SpecialtyRepository specialtyRepository) {
        this.specialtyRepository = specialtyRepository;
    }

//    public List<Specialty> findAll() {
//        return specialtyRepository.findAll();
//    }

    public List<SpecialtyResponse> getAll() {
        return specialtyRepository.findAll().stream()
                .map(this::covertToResponse)
                .collect(Collectors.toList());
    }

    private SpecialtyResponse covertToResponse(Specialty specialty) {
        SpecialtyResponse dto = new SpecialtyResponse();
        dto.setId(specialty.getId());
        dto.setName(specialty.getName());
        dto.setDescription(specialty.getDescription());
        dto.setIcon(specialty.getIcon());
        return dto;
    }

    public Optional<Specialty> getSpecialtyById(int id) {
        return specialtyRepository.findById(id);
    }

    public List<Specialty> getSpecialtiesByDepartment(int departmentId) {
        return specialtyRepository.findSpecialtiesByDepartment_Id(departmentId);
    }

    public Specialty save(Specialty specialty) {
        return specialtyRepository.save(specialty);
    }

    public void deleteSpecialtyById(int id) {
        specialtyRepository.deleteById(id);
    }
}
