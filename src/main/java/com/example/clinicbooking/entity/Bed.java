package com.example.clinicbooking.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "beds")
public class Bed {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name = "bed_number",nullable = false)
    private String bedNumber;
    private Integer status; // 1: Available, 0: Occupied, 2: Maintenance
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;
    @Column(name = "bed_fee", nullable = false)
    private double bedFee;
    @ManyToOne
    @JoinColumn(name = "room_id", nullable = false)
    private Room room;
}
