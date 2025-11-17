package com.example.clinicbooking.DTO.Notification;

import lombok.Data;
import org.hibernate.annotations.Immutable;

import java.util.Map;

@Data
public class NotificationRequest {
    private Integer userId;
    private String title;
    private String body;
    private Map<String, String> data;
    private String sentBy;
}
