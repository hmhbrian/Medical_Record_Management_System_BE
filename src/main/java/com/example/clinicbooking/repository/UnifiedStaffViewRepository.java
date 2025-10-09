package com.example.clinicbooking.repository;

import com.example.clinicbooking.DTO.Staff.StaffResponse;
import com.example.clinicbooking.entity.Staff;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UnifiedStaffViewRepository extends JpaRepository<Staff, Integer> {

    // Lấy danh sách (không phân trang)
    @Query(value = "SELECT * FROM v_staff_unified", nativeQuery = true)
    List<StaffResponse> findAllUnified();

    // Tìm kiếm + phân trang
    @Query(
            value = """
            SELECT * FROM v_staff_unified
            WHERE (:roleType IS NULL OR roleType = :roleType)
              AND (:departmentId IS NULL OR departmentId = :departmentId)
              AND (:positionId IS NULL OR positionId = :positionId)
              AND (
                    :keyword IS NULL 
                 OR fullname   LIKE CONCAT('%', :keyword, '%')
                 OR email      LIKE CONCAT('%', :keyword, '%')
                 OR phoneNumber LIKE CONCAT('%', :keyword, '%')
                 OR code       LIKE CONCAT('%', :keyword, '%')
              )
            ORDER BY fullname ASC
            """,
            countQuery = """
            SELECT COUNT(*) FROM v_staff_unified
            WHERE (:roleType IS NULL OR roleType = :roleType)
              AND (:departmentId IS NULL OR departmentId = :departmentId)
              AND (:positionId IS NULL OR positionId = :positionId)
              AND (
                    :keyword IS NULL 
                 OR fullname   LIKE CONCAT('%', :keyword, '%')
                 OR email      LIKE CONCAT('%', :keyword, '%')
                 OR phoneNumber LIKE CONCAT('%', :keyword, '%')
                 OR code       LIKE CONCAT('%', :keyword, '%')
              )
            """,
            nativeQuery = true
    )
    Page<StaffResponse> search(
            @Param("roleType") String roleType,
            @Param("departmentId") Integer departmentId,
            @Param("positionId") Integer positionId,
            @Param("keyword") String keyword,
            Pageable pageable
    );
}
