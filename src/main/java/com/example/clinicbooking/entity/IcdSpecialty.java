package com.example.clinicbooking.entity;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "icd_specialty")
@Data
public class IcdSpecialty {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    @ManyToOne
    @JoinColumn(name = "specialty_id")
    private Specialty specialty;
    @Column(name = "icd_prefix")
    private String icdPrefix;
}
