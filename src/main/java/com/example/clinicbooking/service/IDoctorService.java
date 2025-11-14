package com.example.clinicbooking.service;

import com.example.clinicbooking.DTO.Appointment.DoctorFilterByComplaintRequest;
import com.example.clinicbooking.DTO.Doctor.DoctorByComplaintResponse;
import com.example.clinicbooking.DTO.Doctor.DoctorRequest;
import com.example.clinicbooking.DTO.Doctor.DoctorResponse;

import java.util.List;

public interface IDoctorService extends IUserService<DoctorResponse, DoctorRequest>{
    List<DoctorResponse> getDoctorsBySpecialtyId(Integer specialtyId);
    DoctorResponse getDoctorsById(Integer doctorId);
    DoctorResponse update(Integer id, DoctorRequest request);
    void delete(Integer id);
    List<DoctorByComplaintResponse> getAvailableDoctorsByComplaint(DoctorFilterByComplaintRequest request);
}
