package com.example.clinicbooking.DTO.FcmToken;

import lombok.Data;

@Data
public class FcmTokenRequest {
    private Integer userId;
    private String fcmToken;
    private String deviceType;
}
