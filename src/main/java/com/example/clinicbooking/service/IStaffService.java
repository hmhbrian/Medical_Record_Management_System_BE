package com.example.clinicbooking.service;

import com.example.clinicbooking.DTO.ApiResponse;
import com.example.clinicbooking.DTO.Staff.StaffRequest;
import com.example.clinicbooking.DTO.Staff.StaffResponse;
import com.example.clinicbooking.DTO.Staff.StaffSummary;
import com.example.clinicbooking.entity.Staff;
import com.example.clinicbooking.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface IStaffService extends IUserService<String, StaffRequest>{
    public List<StaffResponse> findAll();
    public Page<StaffResponse> search(
            String roleType, Integer departmentId, Integer positionId, String keyword, Pageable pageable);
    public ApiResponse<List<StaffSummary>> findStaffByPosition(int positionId);
}
