package com.example.clinicbooking.repository;

import com.example.clinicbooking.entity.ImagingStaff;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ImagingStaffRepository extends JpaRepository<ImagingStaff, Integer> {
    @Query("""
                select i
                from Staff s
                join ImagingStaff i on s.id = i.staff.id
                where s.user.id = :userId
            """)
    Optional<ImagingStaff> findIdByUserId(Integer userId);

    // Tìm ImagingStaff theo staffId
    Optional<ImagingStaff> findByStaffId(Integer staffId);
}
