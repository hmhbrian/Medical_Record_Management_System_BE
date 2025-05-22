package com.example.ClinicBooking.DTO;

import com.example.ClinicBooking.entity.Medicine;
import lombok.Data;

import java.util.Date;

@Data
public class MedicineResponse {
    public Integer id;
    public String medicineName;
    public String unit;
    public Integer stockQuantity;
    public Date expirationDate;
    public Double price;

    public static MedicineResponse fromEntity(Medicine m) {
        MedicineResponse res = new MedicineResponse();
        res.id = m.getId();
        res.medicineName = m.getMedicineName();
        res.unit = m.getUnit();
        res.stockQuantity = m.getStockQuantity();
        res.expirationDate = m.getExpirationDate();
        res.price = m.getPrice();
        return res;
    }
}
