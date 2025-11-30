package com.example.clinicbooking.repository;

import com.example.clinicbooking.entity.Order;
import com.example.clinicbooking.entity.OrderId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface OrderRepository extends JpaRepository<Order, OrderId>, JpaSpecificationExecutor<Order> {
}
