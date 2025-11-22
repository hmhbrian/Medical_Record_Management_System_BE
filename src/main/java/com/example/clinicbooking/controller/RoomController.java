package com.example.clinicbooking.controller;

import com.example.clinicbooking.DTO.ApiResponse;
import com.example.clinicbooking.DTO.Room.RoomRequest;
import com.example.clinicbooking.DTO.Room.RoomResponse;
import com.example.clinicbooking.DTO.Room.RoomUpdateRequest;
import com.example.clinicbooking.service.RoomService;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;


@Tag(name = "Room", description = "Quản lý các phòng. Status: AVAILABLE, OCCUPIED, MAINTENANCE,INACTIVE")
@RestController
@RequestMapping("/api/rooms")
public class RoomController {
    @Autowired
    private RoomService roomService;

    @PostMapping
    public ResponseEntity<ApiResponse<?>> createRoom(@RequestBody RoomRequest roomRq) {
        return ResponseEntity.ok(roomService.Create(roomRq));
    }

    @GetMapping
    public ResponseEntity<Map<String, Object>> getAllRooms(
            // Phân trang
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,

            // Lọc
            @RequestParam(required = false) Integer departmentId,
            @RequestParam(required = false) Integer roomTypeId,
            @RequestParam(required = false) String status,
            @RequestParam(required = false) String search // Tìm kiếm theo roomNumber/name
    ) {
        Page<RoomResponse> roomPage = roomService.getAllRooms(
                page, size, departmentId, roomTypeId, status, search
        );

        // Xây dựng cấu trúc response chuẩn cho phân trang
        Map<String, Object> response = new HashMap<>();
        response.put("rooms", roomPage.getContent());
        response.put("currentPage", roomPage.getNumber());
        response.put("totalItems", roomPage.getTotalElements());
        response.put("totalPages", roomPage.getTotalPages());
        response.put("pageSize", roomPage.getSize());

        return ResponseEntity.ok(response);
    }

    @GetMapping("/specialty/{specialtyId}")
    public ResponseEntity<ApiResponse<List<RoomResponse>>> getRoomsBySpecialty(@PathVariable int specialtyId) {
        List<RoomResponse> rooms = roomService.getRoomsBySpecialty(specialtyId);
        if(rooms.isEmpty()){
            return ResponseEntity.ok(new ApiResponse<>(false, "Không tìm thấy phòng nào trong chuyên khoa này", null));
        }
        return ResponseEntity.ok(new ApiResponse<>(true, "Lấy danh sách phòng theo chuyên khoa thành công", rooms));
    }

    @GetMapping("/department/{departmentId}")
    public ResponseEntity<ApiResponse<List<RoomResponse>>> getRoomsByDepartmentForDoctor(@PathVariable int departmentId, @RequestParam String StaffPosition) {
        List<RoomResponse> rooms = roomService.getRoomsByDepartment(departmentId,StaffPosition);
        if(rooms.isEmpty()){
            return ResponseEntity.ok(new ApiResponse<>(false, "Không tìm thấy phòng nào trong khoa này", null));
        }
        return ResponseEntity.ok(new ApiResponse<>(true, "Lấy danh sách phòng theo khoa thành công", rooms));
    }

    @PutMapping("/{roomId}")
    public ResponseEntity<ApiResponse<RoomResponse>> updateRoom(
            @PathVariable int roomId,
            @RequestBody RoomUpdateRequest request) {

        //RoomResponse updatedRoom = roomService.updateRoom(roomId, request);
        return ResponseEntity.ok(roomService.updateRoom(roomId, request));
    }
}
