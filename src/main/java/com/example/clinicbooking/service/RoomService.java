package com.example.clinicbooking.service;

import com.example.clinicbooking.DTO.ApiResponse;
import com.example.clinicbooking.DTO.Room.RoomRequest;
import com.example.clinicbooking.DTO.Room.RoomResponse;
import com.example.clinicbooking.DTO.Room.RoomUpdateRequest;
import com.example.clinicbooking.Utils.TextUtils;
import com.example.clinicbooking.entity.*;
import com.example.clinicbooking.exceptions.InvalidInputException;
import com.example.clinicbooking.repository.DepartmentRepository;
import com.example.clinicbooking.repository.RoomTypesRepository;
import com.example.clinicbooking.repository.SpecialtyRepository;
import com.example.clinicbooking.repository.roomRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class RoomService {
    @Autowired
    private roomRepository roomRepository;
    @Autowired
    private SpecialtyRepository specialtyRepository;
    @Autowired
    private DepartmentRepository departmentRepository;
    @Autowired
    private RoomTypesRepository roomTypesRepository;

    public List<RoomResponse> getRoomsBySpecialty(int specialtyId) {
        Specialty specialty = specialtyRepository.findById(specialtyId)
                .orElseThrow(() -> new InvalidInputException("Specialty not found"));

        int departmentId = specialty.getDepartment().getId();

        List<Room> rooms = roomRepository.findByDepartmentId(departmentId);
        if (rooms.isEmpty()) {
            throw new InvalidInputException("No rooms found for this specialty's department");
        }

        return rooms.stream()
                .map(this::convertToResponse)
                .collect(Collectors.toList());
    }

    public List<RoomResponse> getRoomsByDepartment(int departmentId, String StaffPosition) {
        Department department = departmentRepository.findById(departmentId)
                .orElseThrow(() -> new InvalidInputException("Department not found"));

        // Lấy tất cả các phòng thuộc Department
        List<Room> allRooms = roomRepository.findByDepartmentId(departmentId);

        //Chuẩn hóa StaffPosition
        String normalizedPosition = TextUtils.normalizeText(StaffPosition);

        //Lọc danh sách phòng theo vai trò
        List<Room> filteredRooms = allRooms.stream()
                .filter(room -> isRoomSuitableForPosition(room, normalizedPosition))
                .collect(Collectors.toList());

        //Chuyển đổi và trả về
        return filteredRooms.stream()
                .map(this::convertToResponse)
                .collect(Collectors.toList());
    }

    //Hàm kiểm tra phòng có phù hợp với vai trò nhân viên không
    private boolean isRoomSuitableForPosition(Room room, String normalizedPosition) {
        String roomType = room.getRoomType().getCode();

        switch (normalizedPosition) {
            case "doctor":
                // Bác sĩ chỉ được phân vào phòng khám
                return "PHONG_KHAM".equalsIgnoreCase(roomType);
            case "nurse":
                // Y tá có thể được phân vào phòng khám hoặc phòng điều trị
                return "PHONG_KHAM".equalsIgnoreCase(roomType) ||
                        "PHONG_THUONG".equalsIgnoreCase(roomType) ||
                        "PHONG_VIP".equalsIgnoreCase(roomType);
            case "Lab Technician":
                // Kỹ thuật viên xét nghiệm có thể được phân vào tất cả các loại phòng
                return "XET_NGHIEM".equalsIgnoreCase(roomType);

            case "Medical Imaging Technician":
                // Kỹ thuật viên hình ảnh y học chỉ được phân vào phòng chẩn đoán hình ảnh
                return "CHAN_DOAN_HINH_ANH".equalsIgnoreCase(roomType);

            case "Pharmacist":
                // Dược sĩ chỉ được phân vào phòng dược
                return "PHONG_THUOC".equalsIgnoreCase(roomType);

            case "Cashier":
                // Kỹ thuật viên xét nghiệm có thể được phân vào tất cả các loại phòng
                return "THU_NGAN".equalsIgnoreCase(roomType);

            case "Patient Receptionist":
                // Kỹ thuật viên xét nghiệm có thể được phân vào tất cả các loại phòng
                return "TIEP_NHAN".equalsIgnoreCase(roomType);
            default:
                // Các vai trò khác (ví dụ: Quản lý) có thể được phép thấy tất cả phòng,
                // hoặc không được phép thấy phòng nào.
                // Ví dụ: Cho phép tất cả phòng nếu vai trò không được định nghĩa
                return true; // Hoặc `false` nếu bạn muốn nghiêm ngặt hơn
        }
    }

//    public List<RoomResponse> getAllRooms() {
//        return roomRepository.findAll().stream()
//                .map(this::convertToResponse)
//                .collect(Collectors.toList());
//    }
    //Lấy danh sách phòng(Phân trang và lọc phòng)
    public Page<RoomResponse> getAllRooms(
            int page, int size,
            Integer departmentId,
            Integer roomTypeId,
            String status,
            String search
    ){
        Pageable pageable = PageRequest.of(page, size);
        RoomStatus roomStatus = null;
        Page<Room> roomPage;

        //Xử lý chuyển đổi Status String sang RoomStatus Enum
        if (status != null && !status.isEmpty()) {
            try {
                roomStatus = RoomStatus.valueOf(status.toUpperCase());
            } catch (IllegalArgumentException e) {
                throw new RuntimeException("Invalid status value: " + status);
            }
        }

        //Trường hợp đầy đủ các tham số lọc và tìm kiếm
        String searchTerm = search != null ? search : "";

        // Trường hợp đầy đủ các tham số lọc: Department, RoomType, RoomStatus VÀ Search
        if (departmentId != null && roomTypeId != null && roomStatus != null) {
            roomPage = roomRepository.findByDepartment_IdAndRoomType_IdAndStatusAndRoomNumberContainingOrNameContaining(
                    departmentId, roomTypeId, roomStatus, searchTerm, searchTerm, pageable
            );
        }
        // Trường hợp chỉ tìm kiếm theo từ khóa (search)
        else if (search != null && !search.isEmpty()) {
            roomPage = roomRepository.findByRoomNumberContainingOrNameContaining(searchTerm, searchTerm, pageable);
        }
        // Trường hợp chỉ lọc theo trạng thái (status)
        else if (roomStatus != null) {
            roomPage = roomRepository.findByStatus(roomStatus, pageable);
        }
        // Trường hợp không có bất kỳ bộ lọc nào (chỉ phân trang)
        else {
            roomPage = roomRepository.findAll(pageable);
        }

        // 3. Chuyển đổi Page<Room> thành Page<RoomResponse> (sử dụng map)
        return roomPage.map(this::convertToResponse);
    }

    public ApiResponse<RoomResponse> Create(RoomRequest newRoom) {
        Department department = departmentRepository.findById(newRoom.getDepartmentId())
                .orElseThrow(() -> new InvalidInputException("Department not found"));

        RoomTypes roomTypes = roomTypesRepository.findById(newRoom.getRoomTypeId())
                .orElseThrow(() -> new InvalidInputException("RoonType not found"));

        RoomStatus status = newRoom.getRoomStatus() != null ?
                RoomStatus.valueOf(newRoom.getRoomStatus().toUpperCase()) :
                RoomStatus.AVAILABLE;

        if(newRoom.getName() == null || roomRepository.existsByName(newRoom.getName())) {
            return new ApiResponse<>(false, "Room name is null or already exists: " + newRoom.getName(), null);
        }
        if(newRoom.getRoomNumber() == null || roomRepository.existsByRoomNumber(newRoom.getRoomNumber())) {
            return new ApiResponse<>(false, "Room number is null or already exists: " + newRoom.getRoomNumber(), null);
        }
        Room room = new Room();
        room.setName(newRoom.getName());
        room.setRoomNumber(newRoom.getRoomNumber());
        room.setDescription(newRoom.getDescription());
        room.setStatus(status);
        room.setDepartment(department);
        room.setRoomType(roomTypes);
        room.setCapacity(newRoom.getRoomCapacity());
        roomRepository.save(room);

        return new ApiResponse<>(true, "Thêm phòng thành công", convertToResponse(room));
    }

    public ApiResponse<RoomResponse> updateRoom(int roomId, RoomUpdateRequest request) {
        // 1. Tìm Room hiện tại (Nếu không tìm thấy thì ném ngoại lệ)
        Room existingRoom = roomRepository.findById(roomId)
                .orElseThrow(() -> new InvalidInputException("Room not found with ID: " + roomId));

        if (request.getName() != null) {
            if(roomRepository.existsByName(request.getName())) {
                return new ApiResponse<>(false, "Room name already exists: " + request.getName(), null);
                //throw new InvalidInputException("Room name already exists: " + request.getName());
            }
            existingRoom.setName(request.getName());
        }

        if(request.getRoomCapacity() != existingRoom.getCapacity() && request.getRoomCapacity() >= 0) {
            existingRoom.setCapacity(request.getRoomCapacity());
        }

        if (request.getRoomNumber() != null) {
            if(roomRepository.existsByRoomNumber(request.getRoomNumber())) {
                return new ApiResponse<>(false, "Room number already exists: " + request.getRoomNumber(), null);
                //throw new IllegalArgumentException("Room number already exists: " + request.getRoomNumber());
            }
            existingRoom.setRoomNumber(request.getRoomNumber());
        }

        if (request.getDescription() != null) {
            existingRoom.setDescription(request.getDescription());
        }

        // Cập nhật RoomStatus (Enum)
        if (request.getRoomStatus() != null) {
            try {
                RoomStatus newStatus = RoomStatus.valueOf(request.getRoomStatus().toUpperCase());
                existingRoom.setStatus(newStatus);
            } catch (IllegalArgumentException e) {
                return new ApiResponse<>(false, "Invalid status value: " + request.getRoomStatus(), null);
                //throw new IllegalArgumentException("Invalid status value: " + request.getRoomStatus());
            }
        }

        // Cập nhật Department
        if (request.getDepartmentId() != null) {
            Department newDepartment = departmentRepository.findById(request.getDepartmentId())
                    .orElseThrow(() -> new InvalidInputException("Department not found with ID: " + request.getDepartmentId()));
            existingRoom.setDepartment(newDepartment);
        }

        // Cập nhật RoomTypes
        if (request.getRoomTypeId() != null) {
            RoomTypes newRoomType = roomTypesRepository.findById(request.getRoomTypeId())
                    .orElseThrow(() -> new InvalidInputException("RoomType not found with ID: " + request.getRoomTypeId()));
            existingRoom.setRoomType(newRoomType);
        }

        //Lưu và trả về DTO
        Room updatedRoom = roomRepository.save(existingRoom);
        return new ApiResponse<>(true, "Cập nhật phòng thành công", convertToResponse(updatedRoom));

    }

    private RoomResponse convertToResponse(Room room) {
        RoomResponse response = new RoomResponse();
        response.setId(room.getId());
        response.setRoomNumber(room.getRoomNumber());
        response.setName(room.getName());
        response.setRoomStatus(room.getStatus() == null ? null : room.getStatus().toString());
        response.setDescription(room.getDescription());
        response.setRoomTypeId(room.getRoomType().getId());
        response.setRoomTypeName(room.getRoomType().getName());
        response.setDepartmentName(room.getDepartment().getName());
        response.setDepartmentId(room.getDepartment().getId());
        response.setRoomCapacity(room.getCapacity());
        return response;
    }
}
