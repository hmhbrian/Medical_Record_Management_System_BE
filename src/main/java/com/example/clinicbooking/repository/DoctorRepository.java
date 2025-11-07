package com.example.clinicbooking.repository;

import com.example.clinicbooking.entity.Doctor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface DoctorRepository extends JpaRepository<Doctor, Integer> {
    List<Doctor> findBySpecialtyId(Integer specialty);

    @Query("""
        SELECT d FROM Doctor d 
        JOIN FETCH d.staff s 
        LEFT JOIN FETCH s.user 
        LEFT JOIN FETCH s.staff_position p
        LEFT JOIN FETCH s.department dept
    """)
    List<Doctor> findAllWithDetails();

    @Query("""
        SELECT d FROM Doctor d 
        JOIN FETCH d.staff s 
        JOIN FETCH s.user 
        WHERE d.id = :doctorId
    """)
    Optional<Doctor> findByIdWithDetails(Integer doctorId);

    @Query("""
        select d.id
        from Staff s
        join Doctor d on s.id = d.staff.id
        where s.user.id = :userId
    """)
    Integer findIdByUserId(Integer userId);

    Integer countBySpecialtyId(Integer specialtyId);

    Optional<Doctor> findByStaff_User_Id(Integer userId);

    @Query("""
        select s.department.id
        from Specialty s
        join Doctor d on s.id = d.specialty.id
        where d.id = :doctorId
    """)
    Integer findDepartmentIdByDoctorId(Integer doctorId);
}
