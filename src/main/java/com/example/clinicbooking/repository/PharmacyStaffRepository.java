package com.example.clinicbooking.repository;

import com.example.clinicbooking.entity.PharmacyStaff;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface PharmacyStaffRepository extends JpaRepository<PharmacyStaff, Integer> {
    @Query("""
                select p
                from Staff s
                join PharmacyStaff p on s.id = p.staff.id
                where s.user.id = :userId
            """)
    PharmacyStaff findByUserId(Integer userId);

    // Tìm PharmacyStaff theo staffId
    Optional<PharmacyStaff> findByStaffId(Integer staffId);
}
