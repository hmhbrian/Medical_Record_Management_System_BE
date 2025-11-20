package com.example.clinicbooking.repository;

import com.example.clinicbooking.entity.ImagingResultFiles;
import com.example.clinicbooking.entity.ImagingTests;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ImagingResultFilesRepository extends JpaRepository<ImagingResultFiles, Integer> {
    List<ImagingResultFiles> findAllByImagingTests(ImagingTests imagingTests);

    Optional<ImagingResultFiles> findByImagingTestsAndName(ImagingTests imagingTests, String name);
}
