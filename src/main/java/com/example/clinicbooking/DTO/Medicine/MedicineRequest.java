package com.example.clinicbooking.DTO.Medicine;

import lombok.Data;

import java.util.Date;

@Data
public class MedicineRequest {
    private String medicineName;
    private String unit;
    private double minimum_quantity;
    private double current_quantity;
    private Double price;
    private Date expirationDate;
    private Date productionDate;
    private String concentration;
    private String manufacturer;
    private String active_ingredient;
    private String dosage_form;
    private int status; // 0:còn hàng, 1:hết hàng, 2:Gần hết hạn, 3:Hết hạn
    private int drugtype_id;
}
