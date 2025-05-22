package com.example.ClinicBooking.repository;

import com.example.ClinicBooking.entity.Feedback;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface FeedbackRepository extends JpaRepository<Feedback, Integer> {
    List<Feedback> findByPatientId(int patientId);
}
