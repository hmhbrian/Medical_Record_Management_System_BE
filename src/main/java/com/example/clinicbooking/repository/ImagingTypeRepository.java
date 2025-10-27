package com.example.clinicbooking.repository;

import com.example.clinicbooking.entity.ImagingTypes;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface ImagingTypeRepository extends JpaRepository<ImagingTypes, Integer>, JpaSpecificationExecutor<ImagingTypes> {
    long countByStatus(int status);

    @Query("select coalesce(sum(t.price), 0) from ImagingTypes  t")
    Double sumPrice();

    boolean existsByImagingCodeAndIdNot(String imagingCode, int id);
    boolean existsByImagingNameAndIdNot(String imagingName, int id);
}
