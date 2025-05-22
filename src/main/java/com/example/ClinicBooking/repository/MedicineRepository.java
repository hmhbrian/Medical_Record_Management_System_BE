package com.example.ClinicBooking.repository;

import com.example.ClinicBooking.entity.Medicine;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import java.util.Date;
import java.util.List;

public interface MedicineRepository extends JpaRepository<Medicine, Integer> {
    List<Medicine> findByMedicineNameContainingIgnoreCase(String keyword);

    @Query("SELECT m FROM Medicine m WHERE m.expirationDate <= :threshold")
    List<Medicine> findExpiringSoon(@Param("threshold") Date threshold);

    @Query("SELECT m FROM Medicine m WHERE m.stockQuantity <= :threshold")
    List<Medicine> findLowStockMedicines(@Param("threshold") int threshold);

}
