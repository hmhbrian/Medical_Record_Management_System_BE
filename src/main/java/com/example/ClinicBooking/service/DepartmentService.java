package com.example.ClinicBooking.service;

import com.example.ClinicBooking.entity.Department;
import com.example.ClinicBooking.repository.DepartmentRepository;
import lombok.AccessLevel;
import lombok.Setter;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class DepartmentService {
    private final DepartmentRepository repo;
    public DepartmentService(DepartmentRepository repo) {
        this.repo = repo;
    }

    public List<Department> findAll() {
        return repo.findAll();
    }

    public Optional<Department> findById(int id) {
        return repo.findById(id);
    }

    public Department save(Department department) {
        return repo.save(department);
    }

    public void delete(int id) {
        repo.deleteById(id);
    }
}
