package com.example.clinicbooking.repository;

import com.example.clinicbooking.entity.FcmToken;
import com.example.clinicbooking.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface FcmTokenRepository extends JpaRepository<FcmToken, Integer> {
    @Query("SELECT t FROM FcmToken t WHERE t.user = :user AND t.isActive = :active")
    List<FcmToken> findAllByUserAndIsActive(@Param("user") User user, @Param("active") boolean active);
}
