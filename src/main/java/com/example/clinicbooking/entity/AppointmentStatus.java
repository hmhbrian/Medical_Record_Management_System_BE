package com.example.clinicbooking.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;

@Entity
@Table(name = "appointment_status")
@Data
public class AppointmentStatus {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @ManyToOne
    @JoinColumn(name = "appointment_id")
    private Appointment appointment;

    @ManyToOne
    @JoinColumn(name = "update_by")
    private User update_by;

    private String reason;
    @Column(name = "update_at")
    private LocalDateTime updateAt;
    private int status; //1: Chờ xác nhận, 2: Đã xác nhận,3: CHờ khám, 4:đang khám, 5:Hoàn thành, 6: Hủy.
}
