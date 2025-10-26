package com.example.clinicbooking.service;

import com.example.clinicbooking.entity.Staff;
import com.example.clinicbooking.entity.staff_position;
import com.example.clinicbooking.repository.StaffPositionRepository;
import com.example.clinicbooking.repository.StaffRepository;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class StaffPositionService {
    public static StaffRepository staffRepo;
    private StaffPositionRepository repo;

    public StaffPositionService(StaffPositionRepository repo, StaffRepository staffRepo) {
        this.repo = repo;
        this.staffRepo = staffRepo;
    }

    public static String getPositionByUserId(int userId) {
        Staff staff = staffRepo.findByUserId(userId)
                .orElseThrow(() -> new RuntimeException("Position not found for user ID: " + userId));
        String NamePosition = staff.getStaff_position().getPosition();
        if (NamePosition == null || NamePosition.isEmpty()) {
            throw new RuntimeException("Position name is empty for user ID: " + userId);
        }
        return NamePosition;
    }

    public List<staff_position> getAllPosition() {
        return repo.findAll(Sort.by(Sort.Direction.ASC, "id")).stream()
                .collect(Collectors.toList());
    }

    public List<staff_position> getAllNoDoctor() {
        return repo.findAllNoDoctor().stream()
                .collect(Collectors.toList());
    }
}
