package com.example.clinicbooking.DTO.Chat;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ChatResponse {
    private String message;
    private LocalDateTime timestamp;
    private List<DoctorSuggestion> suggestedDoctors;
    private Map<String, Object> metadata;
}
