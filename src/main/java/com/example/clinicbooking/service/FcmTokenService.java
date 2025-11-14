package com.example.clinicbooking.service;

import com.example.clinicbooking.DTO.ApiResponse;
import com.example.clinicbooking.DTO.FcmToken.FcmTokenRequest;
import com.example.clinicbooking.entity.FcmToken;
import com.example.clinicbooking.entity.Feedback;
import com.example.clinicbooking.entity.User;
import com.example.clinicbooking.exceptions.InvalidInputException;
import com.example.clinicbooking.repository.FcmTokenRepository;
import com.example.clinicbooking.repository.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@AllArgsConstructor
public class FcmTokenService {
    private final FcmTokenRepository fcmTokenRepo;
    private final UserRepository userRepo;

    public ApiResponse<?> saveToken(FcmTokenRequest request) {
        User user = userRepo.findById(request.getUserId()).orElseThrow(() -> new InvalidInputException("User không tồn tại với id: " + request.getUserId()));
        FcmToken fcmToken = new FcmToken();
        fcmToken.setUser(user);
        fcmToken.setToken(request.getFcmToken());
        fcmToken.setUpdatedAt(LocalDateTime.now());
        fcmToken.setDeviceType(request.getDeviceType());
        fcmToken.setActive(true);
        fcmTokenRepo.save(fcmToken);

        return new ApiResponse<>(true, "Lưu FCM token thành công", null);
    }
}
