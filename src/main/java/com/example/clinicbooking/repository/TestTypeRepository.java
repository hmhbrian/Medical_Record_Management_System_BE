package com.example.clinicbooking.repository;

import com.example.clinicbooking.entity.TestTypes;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface TestTypeRepository extends JpaRepository<TestTypes, Integer>, JpaSpecificationExecutor<TestTypes> {
    long countByStatus(int status);

    @Query("select coalesce(sum(t.price), 0) from TestTypes t")
    Double sumPrice();
    boolean existsByTestCodeAndIdNot(String testCode, int id);
    boolean existsByTestNameAndIdNot(String testName, int id);
}
