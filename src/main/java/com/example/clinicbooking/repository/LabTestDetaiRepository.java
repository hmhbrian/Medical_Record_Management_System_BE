package com.example.clinicbooking.repository;

import com.example.clinicbooking.entity.LabTestDetail;
import com.example.clinicbooking.entity.LabTests;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface LabTestDetaiRepository extends JpaRepository<LabTestDetail, Integer> {
    List<LabTestDetail> findAllByLabTests(LabTests labTests);
}
