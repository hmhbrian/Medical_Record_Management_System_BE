package com.example.clinicbooking.DTO.Staff;

import com.example.clinicbooking.DTO.User.BaseUserResponse;
import lombok.Data;

import java.time.LocalDate;


public interface StaffResponse {
    Integer getId();            // id của Doctor/Nurse/Lab/Imaging
    String  getRoleType();      // DOCTOR | NURSE | LAB | IMAGING
    String  getCode();
    Integer getDepartmentId();
    String  getDepartment();
    Integer getPositionId();
    String  getPosition();
    Integer getSpecialtyId();   // có thể null
    String  getSpecialty();     // có thể null
    Integer getExperienceYears();
    String  getFullname();
    String  getEmail();
    String  getPhoneNumber();
    LocalDate getDateOfBirth();
    Integer getGender();
    String  getAddress();
    String  getAvatarUrl();
    Integer getStaffId();       // id ở bảng Staff
}

