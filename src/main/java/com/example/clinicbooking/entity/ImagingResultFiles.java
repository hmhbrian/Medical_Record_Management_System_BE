package com.example.clinicbooking.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "imaging_result_files")
public class ImagingResultFiles {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private String name;
    @Column(name = "file_url", nullable = false)
    private String FilePath;
    @Column(name = "file_type", nullable = false)
    private String FileType;
    private String description;
    @Column(name = "is_main_image", nullable = false)
    private Boolean IsMainImage;
    @OneToOne
    @JoinColumn(name = "imaging_tests_id")
    private ImagingTests imagingTests;
}
