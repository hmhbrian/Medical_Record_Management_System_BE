package com.example.clinicbooking.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;

@Entity
@Table(name = "messagelogs")
@Data
public class MessageLog {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    @ManyToOne
    @JoinColumn(name = "message_id")
    private Message message;
    @ManyToOne
    @JoinColumn(name = "token_id")
    private FcmToken token;
    @Column(nullable = false)
    private String status;
    @Column(nullable = true, name = "error_message")
    private String errorMessage;
    @Column(nullable = false, name = "sent_at")
    private LocalDateTime sentAt;
    @Column(nullable = true, name = "received_at")
    private LocalDateTime receivedAt;
}
