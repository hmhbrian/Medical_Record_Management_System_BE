package com.example.ClinicBooking.DTO;

import lombok.Data;

@Data
public class PrescriptionDetailRequest {
    private Integer medicineId;
    private int quantity;
    private String dosage;
    private String notes;
}
