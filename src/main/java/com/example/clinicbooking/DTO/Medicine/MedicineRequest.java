package com.example.clinicbooking.DTO.Medicine;

import lombok.Data;

import java.util.Date;

@Data
public class MedicineRequest {
    public String medicineName;
    public String unit;
    public Integer stockQuantity;
    public Date expirationDate;
    public Double price;
}
