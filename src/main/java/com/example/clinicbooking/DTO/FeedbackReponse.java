package com.example.clinicbooking.DTO;

import com.example.clinicbooking.entity.Patient;
import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class FeedbackReponse {
    private int id;
    private String patientName;
    private int rating;
    private String comment;
    private LocalDateTime createdAt;
}
