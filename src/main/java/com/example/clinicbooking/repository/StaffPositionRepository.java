package com.example.clinicbooking.repository;

import com.example.clinicbooking.entity.Staff;
import com.example.clinicbooking.entity.staff_position;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface StaffPositionRepository extends JpaRepository<staff_position, Integer> {
    @Query("SELECT sp FROM staff_position sp WHERE sp.id > 1 order by sp.id ASC")
    List<staff_position> findAllNoDoctor();
}
