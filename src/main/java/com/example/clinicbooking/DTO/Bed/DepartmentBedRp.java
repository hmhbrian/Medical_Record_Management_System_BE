package com.example.clinicbooking.DTO.Bed;

import lombok.Data;

@Data
public class DepartmentBedRp {
    private String departmentName;
    private int giuongDangSuDung;      // Occupied
    private int tongGiuongTrongKhoa;   // Total in department
    private double tyle;               // occupied / total * 100 (%)
}
