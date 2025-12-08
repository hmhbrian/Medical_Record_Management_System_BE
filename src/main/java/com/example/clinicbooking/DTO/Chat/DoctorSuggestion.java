package com.example.clinicbooking.DTO.Chat;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class DoctorSuggestion {
    private Integer doctorId;
    private String doctorName;
    private String specialty;
    private String doctorAvatarUrl;
    private Integer experienceYears;
    private Boolean isAvailable;
}
