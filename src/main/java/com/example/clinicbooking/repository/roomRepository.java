package com.example.clinicbooking.repository;

import com.example.clinicbooking.entity.Room;
import com.example.clinicbooking.entity.RoomStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface roomRepository extends JpaRepository<Room, Integer> {
    List<Room> findByDepartmentId(int departmentId);
    int countByDepartmentId(int departmentId);

    Boolean existsByName(String name);
    Boolean existsByRoomNumber(String roomNumber);

    /**
     Phương thức tìm kiếm và phân trang theo các tiêu chí lọc:
     - Tìm theo departmentId (thuộc Department)
     - Tìm theo roomTypeId (thuộc RoomTypes)
     - Tìm theo roomStatus
     - Tìm kiếm gần đúng (LIKE) theo roomNumber HOẶC name
     */
    Page<Room> findByDepartment_IdAndRoomType_IdAndStatusAndRoomNumberContainingOrNameContaining(
            Integer departmentId,
            Integer roomTypeId,
            RoomStatus roomStatus,
            String roomNumberSearch, // Dùng cho OR
            String nameSearch,       // Dùng cho OR
            Pageable pageable
    );

    // Tìm room theo trường hợp chỉ tìm kiếm bằng keyword (search) mà không có các bộ lọc khác.
    Page<Room> findByRoomNumberContainingOrNameContaining(String roomNumberSearch, String nameSearch, Pageable pageable);

    // Phương thức tìm kiếm theo các tiêu chí khác (ví dụ: chỉ theo trạng thái)
    Page<Room> findByStatus(RoomStatus roomStatus, Pageable pageable);
}
