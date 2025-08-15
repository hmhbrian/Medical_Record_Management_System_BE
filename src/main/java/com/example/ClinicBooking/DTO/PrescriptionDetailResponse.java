package com.example.ClinicBooking.DTO;

import lombok.Data;

@Data
public class PrescriptionDetailResponse {
    private int id;
    private int medicineId;
    private String medicineName;
    private int quantity;
    private String dosage;
    private String notes;
}
