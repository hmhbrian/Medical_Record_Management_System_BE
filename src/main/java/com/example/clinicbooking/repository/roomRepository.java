package com.example.clinicbooking.repository;

import com.example.clinicbooking.entity.Room;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface roomRepository extends JpaRepository<Room, Integer> {
    List<Room> findByDepartmentId(int departmentId);
}
