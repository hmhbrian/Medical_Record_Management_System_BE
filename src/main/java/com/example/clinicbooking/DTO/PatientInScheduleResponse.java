package com.example.clinicbooking.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PatientInScheduleResponse{
    int patientId;
    String patientName;
}
