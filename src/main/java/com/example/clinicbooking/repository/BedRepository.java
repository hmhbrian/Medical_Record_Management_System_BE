package com.example.clinicbooking.repository;

import com.example.clinicbooking.entity.Bed;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface BedRepository extends JpaRepository<Bed, Integer>, JpaSpecificationExecutor<Bed> {
    // Tổng hợp toàn hệ thống (native để đơn giản)
    @Query(value = """
        SELECT 
          COUNT(*)                                   AS total,
          SUM(CASE WHEN b.status = 1 THEN 1 ELSE 0 END) AS available,
          SUM(CASE WHEN b.status = 0 THEN 1 ELSE 0 END) AS occupied,
          SUM(CASE WHEN b.status = 2 AND b.status = 3 THEN 1 ELSE 0 END) AS maintenance
        FROM beds b
        """, nativeQuery = true)
    BedsTotalsAgg aggregateTotals();

    // Tổng hợp theo khoa
    @Query(value = """
        SELECT 
          d.id              AS deptId,
          d.name            AS deptName,
          COUNT(b.id)       AS total,
          SUM(CASE WHEN b.status = 0 THEN 1 ELSE 0 END) AS occupied
        FROM beds b
        JOIN room r ON r.id = b.room_id
        JOIN department d ON d.id = r.department_id
        GROUP BY d.id, d.name
        ORDER BY d.name
        """, nativeQuery = true)
    List<DepartmentAgg> aggregateByDepartment();

    // Projections
    interface BedsTotalsAgg {
        long getTotal();
        long getAvailable();
        long getOccupied();
        long getMaintenance();
    }

    interface DepartmentAgg {
        Integer getDeptId();
        String  getDeptName();
        long    getTotal();
        long    getOccupied();
    }
}
