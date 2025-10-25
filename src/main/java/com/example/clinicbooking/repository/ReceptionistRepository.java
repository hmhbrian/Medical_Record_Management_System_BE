package com.example.clinicbooking.repository;

import com.example.clinicbooking.entity.Receptionist;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ReceptionistRepository extends JpaRepository<Receptionist, Integer> {
}
