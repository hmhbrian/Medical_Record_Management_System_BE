package com.example.clinicbooking.service;

import com.example.clinicbooking.DTO.FeedbackReponse;
import com.example.clinicbooking.DTO.Services.ImagingTypeResponse;
import com.example.clinicbooking.entity.Feedback;
import com.example.clinicbooking.entity.ImagingTypes;
import com.example.clinicbooking.repository.FeedbackRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class FeedbackService {
    @Autowired
    private FeedbackRepository feedbackRepository;

    public Feedback saveFeedback(Feedback feedback) {
        feedback.setCreatedAt(LocalDateTime.now());
        return feedbackRepository.save(feedback);
    }

    public List<FeedbackReponse> getAllFeedbacks() {
        return feedbackRepository.findAll()
                .stream()
                .map(this::covertToResponse)
                .collect(Collectors.toList());
    }

    public List<Feedback> getFeedbacksByPatient(int patientId) {
        return feedbackRepository.findByPatientId(patientId);
    }

    private FeedbackReponse covertToResponse(Feedback feedback) {
        FeedbackReponse dto = new FeedbackReponse();
        dto.setId(feedback.getId());
        dto.setPatientName(feedback.getPatient().getUser().getFullname());
        dto.setRating( feedback.getRating());
        dto.setComment(feedback.getComment());
        dto.setCreatedAt(feedback.getCreatedAt());
        return dto;
    }
}
