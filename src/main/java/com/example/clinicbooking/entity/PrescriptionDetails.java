package com.example.clinicbooking.entity;


import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;

@Table(name = "prescriptiondetails")
@Entity
@Data
public class PrescriptionDetails {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    @ManyToOne
    @JoinColumn(name = "prescription_id", nullable = true)
    private Prescriptions prescription;
    @ManyToOne
    @JoinColumn(name = "medicine_id", nullable = false)
    private Medicine medicine;
    @Column(name = "dosage", nullable = false)
    private String dosage;
    @Column(name = "notes", nullable = false)
    private String notes;
    private Integer quantity;
    @Column(name="daily_quantity")
    private Integer dailyQuantity;
    private boolean is_substitutable;
}
