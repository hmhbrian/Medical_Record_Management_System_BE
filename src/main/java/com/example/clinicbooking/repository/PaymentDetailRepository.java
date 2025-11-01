package com.example.clinicbooking.repository;

import com.example.clinicbooking.entity.PaymentDetail;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface PaymentDetailRepository extends JpaRepository<PaymentDetail, Integer> {
    Optional<PaymentDetail> findById(Integer integer);
}
