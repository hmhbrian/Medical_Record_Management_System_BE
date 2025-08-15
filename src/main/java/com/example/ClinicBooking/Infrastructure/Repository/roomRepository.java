package com.example.ClinicBooking.Infrastructure.Repository;

import com.example.ClinicBooking.Domain.Entities.Room;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface roomRepository extends JpaRepository<Room, Integer> {
    List<Room> findByDepartmentId(int departmentId);
}
