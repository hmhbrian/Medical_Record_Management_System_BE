package com.example.clinicbooking.repository;

import com.example.clinicbooking.entity.ImagingTests;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ImagingTestsRepository extends JpaRepository<ImagingTests, Integer> {
}
