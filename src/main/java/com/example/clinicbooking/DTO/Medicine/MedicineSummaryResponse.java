package com.example.clinicbooking.DTO.Medicine;

import lombok.Data;

@Data
public class MedicineSummaryResponse {
    public Integer id;
    public String medicineName;
    public String concentration; // hàm lượng, nồng độ
    public String active_ingredient; // hoạt chất
    public String unit;
    public double current_quantity;
}
