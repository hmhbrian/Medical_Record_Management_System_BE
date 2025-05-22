package com.example.ClinicBooking.controller;

import com.example.ClinicBooking.entity.Feedback;
import com.example.ClinicBooking.service.FeedbackService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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
