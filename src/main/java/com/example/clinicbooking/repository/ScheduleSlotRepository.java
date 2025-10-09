package com.example.clinicbooking.repository;

import com.example.clinicbooking.entity.ScheduleSlot;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ScheduleSlotRepository extends JpaRepository<ScheduleSlot, Integer> {
}
