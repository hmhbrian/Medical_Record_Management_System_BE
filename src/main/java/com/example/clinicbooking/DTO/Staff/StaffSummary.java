package com.example.clinicbooking.DTO.Staff;

import java.time.LocalDate;

public interface StaffSummary {
    Integer getId();            // id của Doctor/Nurse/Lab/Imaging
    String  getCode();
    Integer getExperienceYears();
    String  getFullname();
    String  getEmail();
    String  getPhoneNumber();
    LocalDate getDateOfBirth();
    Integer getGender();
    String  getAddress();
    String  getAvatarUrl();
    Integer getStaffId();       // id ở bảng Staff
    Integer getDepartmentId();
    String  getDepartment();
    String  getPosition();
}
