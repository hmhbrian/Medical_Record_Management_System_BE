package com.example.clinicbooking.service;

import com.example.clinicbooking.DTO.Patient.PatientRequest;
import com.example.clinicbooking.DTO.Patient.PatientResponse;

import java.util.List;

public interface IPatientService extends IUserService<PatientResponse, PatientRequest>{
    PatientResponse searchPatients(String keyword);
}
