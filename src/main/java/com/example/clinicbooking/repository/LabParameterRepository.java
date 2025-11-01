package com.example.clinicbooking.repository;

import com.example.clinicbooking.entity.LabParameter;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface LabParameterRepository extends JpaRepository<LabParameter, Integer> {
}
