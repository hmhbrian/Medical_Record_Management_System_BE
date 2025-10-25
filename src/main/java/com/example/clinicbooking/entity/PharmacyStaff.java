package com.example.clinicbooking.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "Pharmacy_Staff")
public class PharmacyStaff {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @OneToOne
    @JoinColumn(name = "staff_id")
    private Staff staff;
    @Column(name = "pha_scode")
    private String PharmacyCode;
    @Column(name = "experience_years")
    private int experienceYears;
}
