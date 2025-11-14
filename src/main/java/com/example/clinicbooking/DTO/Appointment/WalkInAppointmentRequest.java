package com.example.clinicbooking.DTO.Appointment;

import lombok.Data;

@Data
public class WalkInAppointmentRequest {
    private Integer patientId;
    private Integer doctorId;
    private Integer doctorScheduleId;
    private String chiefComplaint;
}
