package com.example.clinicbooking.repository;

import com.example.clinicbooking.entity.InvoiceSequence;
import jakarta.persistence.LockModeType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface InvoiceSequenceRepository extends JpaRepository<InvoiceSequence, Integer> {
    // Sử dụng LockModeType.PESSIMISTIC_WRITE để khóa bản ghi trong CSDL
    // khi đọc. Bản ghi này chỉ được mở khóa khi Transaction hoàn tất.
    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("SELECT s FROM InvoiceSequence s WHERE s.Year = :year AND s.Serial = :serial")
    Optional<InvoiceSequence> findAndLockByYearAndSerial(String year, String serial);
}
