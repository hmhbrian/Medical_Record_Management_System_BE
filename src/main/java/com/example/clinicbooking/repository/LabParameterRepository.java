package com.example.clinicbooking.repository;

import com.example.clinicbooking.entity.LabParameter;
import com.example.clinicbooking.entity.TestTypes;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface LabParameterRepository extends JpaRepository<LabParameter, Integer> {
    List<LabParameter> findByTestTypes(TestTypes testTypes);
}
