package com.example.clinicbooking.repository;

import com.example.clinicbooking.entity.MessageLog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MessageLogRepository extends JpaRepository<MessageLog, Integer> {
}
