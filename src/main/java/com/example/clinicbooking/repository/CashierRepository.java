package com.example.clinicbooking.repository;

import com.example.clinicbooking.entity.Cashier;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface CashierRepository extends JpaRepository<Cashier, Integer> {
    @Query("""
        select c.id
        from Staff s
        join Cashier c on s.id = c.staff.id
        where s.user.id = :userId
    """)
    Integer findIdByUserId(Integer userId);
}
