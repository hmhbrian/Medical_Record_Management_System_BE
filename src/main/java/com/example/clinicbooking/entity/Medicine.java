package com.example.clinicbooking.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Entity
@Table(name = "medicines")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Medicine {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private String medicineName;
    private String unit;
    private double minimum_quantity;
    private double current_quantity;
    private double reserved_quantity;
    private Double price;
    @Temporal(TemporalType.DATE)
    private Date expirationDate;
    @Temporal(TemporalType.DATE)
    private Date productionDate;
    private String concentration;
    private String manufacturer;
    private String active_ingredient;
    private String dosage_form;
    private int status; //0:còn hàng, 1:hết hàng, 2:Gần hết hạn, 3:Hết hạn
    @ManyToOne
    @JoinColumn(name = "drugtype_id")
    private DrugType drugType;
}
