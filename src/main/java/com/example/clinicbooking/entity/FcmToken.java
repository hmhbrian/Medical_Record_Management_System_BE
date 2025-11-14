package com.example.clinicbooking.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;

@Entity
@Table(name = "Fcm_tokens")
@Data
public class FcmToken {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;
    @Column(nullable = false, name = "token")
    private String token;
    @Column(nullable = false, name = "device_type")
    private String deviceType;
    @Column(nullable = false, name = "updated_at")
    private LocalDateTime updatedAt;
    @Column(name = "is_active")
    private boolean isActive;
}
