package com.example.ClinicBooking.service;

import com.example.ClinicBooking.DTO.DoctorRequest;
import com.example.ClinicBooking.DTO.DoctorResponse;

import java.util.List;

public interface IDoctorService extends IUserService<DoctorResponse, DoctorRequest>{
    List<DoctorResponse> getDoctorsBySpecialtyId(Integer specialtyId);
}
