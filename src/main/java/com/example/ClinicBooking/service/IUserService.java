package com.example.ClinicBooking.service;

import com.example.ClinicBooking.DTO.BaseUserRequest;
import com.example.ClinicBooking.DTO.PatientResponse;

import java.util.List;

public interface IUserService <TResponse, TRequest> {
    TResponse create(TRequest request);
    List<TResponse> getAll();
    TResponse getbyUserId(Integer userId);
    TResponse update(Integer id, TRequest request);
    void delete(Integer id);
}
