package com.example.clinicbooking.repository;

import com.example.clinicbooking.entity.Icd10;
import com.example.clinicbooking.entity.Medical_Examination;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

public interface Icd10Repository extends JpaRepository<Icd10, Integer>, JpaSpecificationExecutor<Icd10> {
}
