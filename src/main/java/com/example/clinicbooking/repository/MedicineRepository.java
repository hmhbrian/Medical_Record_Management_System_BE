package com.example.clinicbooking.repository;

import com.example.clinicbooking.entity.Medicine;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Date;
import java.util.List;

public interface MedicineRepository extends JpaRepository<Medicine, Integer>, JpaSpecificationExecutor<Medicine> {
    List<Medicine> findByMedicineNameContainingIgnoreCase(String keyword);

    @Query("SELECT m FROM Medicine m WHERE m.expirationDate <= :threshold")
    List<Medicine> findExpiringSoon(@Param("threshold") Date threshold);

    @Query("SELECT m FROM Medicine m WHERE m.current_quantity <= :threshold")
    List<Medicine> findLowStockMedicines(@Param("threshold") int threshold);

    List<Medicine> findAllByOrderByStatusDesc();

}
