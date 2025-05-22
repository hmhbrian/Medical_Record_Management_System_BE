package com.example.ClinicBooking.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "Users")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private String fullname;
    private String email;
    private String phoneNumber;
    private LocalDate dateOfBirth;
    private int gender; // 0: male, 1: female, 2: other
    private String address;
    private String pass;
    private int role; // 0: admin, 1: patient, 2: staff
    private LocalDateTime createdAt = LocalDateTime.now();
    @Column(name = "avartar_url")
    private String avatar_url;
}
