package com.example.clinicbooking.repository;

import com.example.clinicbooking.entity.IcdSpecialty;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Set;

@Repository
public interface IcdSpecialtyRepository extends JpaRepository<IcdSpecialty, Integer> {
    // Lấy tập hợp các specialtyId duy nhất từ bảng IcdSpecialty dựa trên tập hợp icdPrefix
    @Query("SELECT DISTINCT i.specialty.id FROM IcdSpecialty i WHERE i.icdPrefix IN :icdPrefixes")
    Set<Integer> findDistinctSpecialtyIdsByIcdPrefixIn(@Param("icdPrefixes") Set<String> icdPrefixes);}
