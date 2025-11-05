package com.example.clinicbooking.entity;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "invoice_sequence")
@Data
public class InvoiceSequence {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    @Column(name ="year")
    private String Year;
    @Column(name ="serial")
    private String Serial;
    @Column(name = "current_number")
    private Integer CurrentNumber;
}
