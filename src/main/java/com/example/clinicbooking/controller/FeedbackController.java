package com.example.clinicbooking.controller;

import com.example.clinicbooking.entity.Feedback;
import com.example.clinicbooking.service.FeedbackService;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@Tag(name = "Feedback", description = "Quản lý phản hồi từ bệnh nhân")
@RestController
@RequestMapping("/api/feedbacks")
public class FeedbackController {
    @Autowired
    private FeedbackService feedbackService;

    @PostMapping
    public Feedback submitFeedback(@RequestBody Feedback feedback) {
        return feedbackService.saveFeedback(feedback);
    }

    // Get all feedbacks
    @GetMapping
    public List<Feedback> getAllFeedbacks() {
        return feedbackService.getAllFeedbacks();
    }

    // Get feedbacks by patient
    @GetMapping("/patient/{patientId}")
    public List<Feedback> getFeedbacksByPatient(@PathVariable int patientId) {
        return feedbackService.getFeedbacksByPatient(patientId);
    }
}
