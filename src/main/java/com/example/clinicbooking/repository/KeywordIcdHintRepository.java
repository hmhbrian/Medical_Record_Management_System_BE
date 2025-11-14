package com.example.clinicbooking.repository;

import com.example.clinicbooking.entity.Keyword_IcdHint;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Set;

@Repository
public interface KeywordIcdHintRepository extends JpaRepository<Keyword_IcdHint, Integer> {
    // tìm kiếm từ khóa được bao bọc bởi khoảng trắng (' ' + keyword + ' ')
    //bên trong chuỗi tìm kiếm
    @Query("SELECT DISTINCT k.IcdPrefixHint " +
            "FROM Keyword_IcdHint k " +
            "WHERE :chiefComplaint LIKE CONCAT('% ', k.keyword, ' %')")
    Set<String> findDistinctIcdPrefixByKeywords(@Param("chiefComplaint") String chiefComplaint);
}
