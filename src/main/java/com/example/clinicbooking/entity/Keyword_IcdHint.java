package com.example.clinicbooking.entity;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "keyword_icd_hint")
@Data
public class Keyword_IcdHint {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    @Column(name = "keyword")
    private String keyword;
    @Column(name = "icd_prefix_hint")
    private String IcdPrefixHint;
}
