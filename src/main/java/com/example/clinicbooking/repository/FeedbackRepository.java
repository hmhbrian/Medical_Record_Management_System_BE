package com.example.clinicbooking.repository;

import com.example.clinicbooking.entity.Feedback;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface FeedbackRepository extends JpaRepository<Feedback, Integer> {
    List<Feedback> findByPatientId(int patientId);
}
