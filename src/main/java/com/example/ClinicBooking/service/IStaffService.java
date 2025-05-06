package com.example.ClinicBooking.service;

import com.example.ClinicBooking.entity.Staff;
import com.example.ClinicBooking.entity.User;

public interface IStaffService<TResponse, TRequest> {
    Staff createStaff(User user, TRequest request);
}
