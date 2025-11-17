package com.example.clinicbooking.repository;

import com.example.clinicbooking.entity.Notifications;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface NotificationRepository extends JpaRepository<Notifications, Integer> {
}
