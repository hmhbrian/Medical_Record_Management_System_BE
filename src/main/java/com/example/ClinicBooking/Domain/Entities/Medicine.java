package com.example.ClinicBooking.Domain.Entities;

import jakarta.persistence.*;
import lombok.Data;

import java.util.Date;

@Entity
@Table(name = "medicines")
@Data
public class Medicine {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private String medicineName;
    private String unit;
    private Integer stockQuantity;

    @Temporal(TemporalType.DATE)
    private Date expirationDate;

    private Double price;
}
