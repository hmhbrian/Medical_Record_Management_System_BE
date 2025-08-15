package com.example.ClinicBooking.DTO;


import lombok.Data;

import java.time.LocalDate;

@Data
public class MedicalRecordReponse {
    private Integer id;
    private String patientName;
    private Integer patientId;
    private String doctorName;
    private Integer doctorId;
    private Integer appointmentId;
    private String initialSymptoms;
    private String diagnosis;
    private LocalDate visitDate;
    private Integer visitNumber;
}
