package com.example.clinicbooking.DTO.Medicine;

import com.example.clinicbooking.entity.DrugType;
import com.example.clinicbooking.entity.Medicine;
import lombok.Data;

import java.util.Date;

@Data
public class MedicineResponse {
    public Integer id;
    public String medicineName;
    public String unit;
    public double minimum_quantity;
    public double current_quantity;
    public Double price;
    public Date expirationDate;
    public Date productionDate;
    public String concentration;
    public String manufacturer;
    public String active_ingredient;
    public String dosage_form;
    public int status; //0:còn hàng, 1:hết hàng, 2:Gần hết hạn, 3:Hết hạn
    public DrugType drugType;

    public static MedicineResponse fromEntity(Medicine m) {
        MedicineResponse res = new MedicineResponse();
        res.id = m.getId();
        res.medicineName = m.getMedicineName();
        res.unit = m.getUnit();
        res.current_quantity = m.getCurrent_quantity();
        res.minimum_quantity = m.getMinimum_quantity();
        res.expirationDate = m.getExpirationDate();
        res.productionDate = m.getProductionDate();
        res.price = m.getPrice();
        res.concentration = m.getConcentration();
        res.manufacturer = m.getManufacturer();
        res.active_ingredient = m.getActive_ingredient();
        res.dosage_form = m.getDosage_form();
        res.status = m.getStatus();
        res.drugType = m.getDrugType();
        return res;
    }
}
