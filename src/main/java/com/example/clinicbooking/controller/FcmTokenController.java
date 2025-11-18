package com.example.clinicbooking.controller;

import com.example.clinicbooking.DTO.ApiResponse;
import com.example.clinicbooking.DTO.FcmToken.FcmTokenRequest;
import com.example.clinicbooking.service.FcmTokenService;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/fcm")
@Tag(name = "Fcm", description = "Quản lý FCM Token")
public class FcmTokenController {
    @Autowired
    private FcmTokenService fcmTokenService;
    @PostMapping("/token")
    public ResponseEntity<ApiResponse<?>> createToken(@RequestBody FcmTokenRequest fcmTokenRq) {
        return ResponseEntity.ok(fcmTokenService.saveToken(fcmTokenRq));
    }
}
