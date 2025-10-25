package com.example.clinicbooking.entity;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "Cashier")
public class Cashier {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @OneToOne
    @JoinColumn(name = "staff_id")
    private Staff staff;
    @Column(name = "cas_scode")
    private String CashierCode;
    @Column(name = "experience_years")
    private int experienceYears;
}
