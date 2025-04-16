package com.example.ClinicBooking.service;

import com.example.ClinicBooking.DTO.BaseUserRequest;

import java.util.List;

public interface IUserService <TResponse, TRequest> {
    TResponse create(TRequest request);
    List<TResponse> getAll();
}
