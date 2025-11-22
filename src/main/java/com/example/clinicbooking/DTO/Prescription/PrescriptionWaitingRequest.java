package com.example.clinicbooking.DTO.Prescription;

import lombok.Data;

@Data
public class PrescriptionWaitingRequest {
    private Integer page = 0;
    private Integer size = 10;
    private String sortBy = "prescriptionDate";
    private String sortDir = "DESC";

    private String keyword;
    private Integer doctorId;
    private String findDate;
}
