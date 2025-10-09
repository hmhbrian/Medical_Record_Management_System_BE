package com.example.clinicbooking.DTO.Bed;

import lombok.Data;

import java.util.List;

@Data
public class OverViewResponse {
    private int sumBed;
    private int availableBeds;      // (GiuongTrong)
    private int occupiedBeds;       // (GiuongDangSuDung)
    private int maintenanceBeds;    // (GiuongDangBaoTri)
    private List<DepartmentBedRp> departmentBed;
}
