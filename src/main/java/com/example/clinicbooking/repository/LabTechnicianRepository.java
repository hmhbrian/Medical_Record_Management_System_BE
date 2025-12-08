package com.example.clinicbooking.repository;

import com.example.clinicbooking.entity.LabTechnician;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface LabTechnicianRepository extends JpaRepository<LabTechnician, Integer> {
    @Query("""
                select l.id
                from Staff s
                join LabTechnician l on s.id = l.staff.id
                where s.user.id = :userId
            """)
    Integer findIdByUserId(Integer userId);

    @Query("""
                select l
                from Staff s
                join LabTechnician l on s.id = l.staff.id
                where s.user.id = :userId
            """)
    LabTechnician findByUserId(Integer userId);

    // Tìm LabTechnician theo staffId
    Optional<LabTechnician> findByStaffId(Integer staffId);
}
