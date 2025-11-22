package com.example.clinicbooking.DTO.Prescription;

import com.example.clinicbooking.DTO.Prescription.Detail.PrescriptionDetailsRequest;
import lombok.Data;

import java.util.List;

@Data
public class PrescriptionRequest {
    private Integer total_days;
    private boolean isSend;
    private List<PrescriptionDetailsRequest> prescriptionDetails;
}
