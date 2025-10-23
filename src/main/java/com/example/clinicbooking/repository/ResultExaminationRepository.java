package com.example.clinicbooking.repository;

import com.example.clinicbooking.entity.MedicalRecord;
import com.example.clinicbooking.entity.ResultExamination;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ResultExaminationRepository extends JpaRepository<ResultExamination, Integer> {
    Optional<ResultExamination> findByRecord(MedicalRecord record);
}
