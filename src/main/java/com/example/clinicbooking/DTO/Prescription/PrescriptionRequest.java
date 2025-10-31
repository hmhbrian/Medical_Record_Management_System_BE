package com.example.clinicbooking.DTO.Prescription;

import lombok.Data;

import java.util.List;

@Data
public class PrescriptionRequest {
    private Integer total_days;
    private boolean isSend;
    private List<PrescriptionDetailsRequest> prescriptionDetails;
}
