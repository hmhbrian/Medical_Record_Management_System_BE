package com.example.ClinicBooking.repository;

import com.example.ClinicBooking.entity.Shift_type;
import com.example.ClinicBooking.entity.Specialty;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ShiftTypeRepository extends JpaRepository<Shift_type, Integer> {

}
