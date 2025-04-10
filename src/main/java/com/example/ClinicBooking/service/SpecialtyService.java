package com.example.ClinicBooking.service;

import com.example.ClinicBooking.entity.Department;
import com.example.ClinicBooking.entity.Specialty;
import com.example.ClinicBooking.repository.SpecialtyRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class SpecialtyService {
    private SpecialtyRepository specialtyRepository;

    public SpecialtyService(SpecialtyRepository specialtyRepository) {
        this.specialtyRepository = specialtyRepository;
    }

    public List<Specialty> findAll() {
        return specialtyRepository.findAll();
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
