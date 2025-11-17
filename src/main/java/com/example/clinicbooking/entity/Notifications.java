package com.example.clinicbooking.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;

@Entity
@Table(name = "notifications")
@Data
public class Notifications {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    @ManyToOne
    @JoinColumn(name = "receiver_id")
    private User user;
    @ManyToOne
    @JoinColumn(name = "message_id")
    private Message message;
    @Column(nullable = false, name= "is_read")
    private boolean isRead;
    @Column(nullable = false, name = "sent_at")
    private LocalDateTime sentAt;
}
