package com.example.clinicbooking.DTO.Appointment;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ReExaminationRequest {
    private Integer patientId;
    private Integer doctorScheduleId;
    private Integer scheduleSlotId;
    private String reason = "Tái khám";
}
