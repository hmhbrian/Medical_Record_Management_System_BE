package com.example.clinicbooking.repository;

import com.example.clinicbooking.entity.Notifications;
import com.example.clinicbooking.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface NotificationRepository extends JpaRepository<Notifications, Integer> {
    List<Notifications> findAllByUser(User user);
    Integer countByUserAndIsReadFalse(User user);
}
