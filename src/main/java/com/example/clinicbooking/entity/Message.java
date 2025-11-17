package com.example.clinicbooking.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;

@Entity
@Table(name = "messages")
@Data
public class Message {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    @Column(nullable = false)
    private String title;
    @Column(nullable = false, columnDefinition = "TEXT")
    private String body;
    @Column(nullable = false, columnDefinition = "TEXT")
    private String data;
    @Column(nullable = false, name = "send_type")
    private String sendType;
    @Column(nullable = false, name = "send_by")
    private String sendBy;
    @Column(nullable = false, name = "created_at")
    private LocalDateTime createdAt;
}
