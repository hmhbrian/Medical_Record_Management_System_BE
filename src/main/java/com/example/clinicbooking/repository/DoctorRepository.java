package com.example.clinicbooking.repository;

import com.example.clinicbooking.entity.Doctor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface DoctorRepository extends JpaRepository<Doctor, Integer> {
    List<Doctor> findBySpecialtyId(Integer specialty);
    Optional<Doctor> findById(Integer doctorId);
    @Query("""
        select d.id
        from Staff s
        join Doctor d on s.id = d.staff.id
        where s.user.id = :userId
    """)
    Integer findIdByUserId(Integer userId);
    Integer countBySpecialtyId(Integer specialtyId);
}
