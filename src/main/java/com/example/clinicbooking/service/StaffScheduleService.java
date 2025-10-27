package com.example.clinicbooking.service;

import com.example.clinicbooking.DTO.ApiResponse;
import com.example.clinicbooking.DTO.StaffSchedule.StaffScheduleRequest;
import com.example.clinicbooking.DTO.StaffSchedule.StaffScheduleResponse;
import com.example.clinicbooking.DTO.StaffSchedule.StaffsScheduleRequest;
import com.example.clinicbooking.entity.*;
import com.example.clinicbooking.repository.ShiftTypeRepository;
import com.example.clinicbooking.repository.StaffRepository;
import com.example.clinicbooking.repository.StaffSchedulesRepository;
import com.example.clinicbooking.repository.roomRepository;
import com.example.clinicbooking.security.CustomUserDetails;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class StaffScheduleService {
    @Autowired
    private StaffSchedulesRepository staffSchedulesRepo;
    @Autowired
    private roomRepository roomRepo;
    @Autowired
    private ShiftTypeRepository shiftTypeRepo;
    @Autowired
    private StaffRepository staffRepo;

    //Thêm lịch làm việc mới cho 1 nhân viên sau khi kiểm tra công suất phòng.
    public ApiResponse<?> addSingleSchedule(StaffScheduleRequest request) {
        Integer staffId = request.getStaffId();
        Integer shiftTypeId = request.getShiftTypeId();
        Integer roomId = request.getRoomId();
        LocalDate date = request.getDate();

        // 1. Kiểm tra Xung đột Cá nhân
        long existingScheduleCount = staffSchedulesRepo
                .countByStaffIdAndDateAndShiftTypeId(staffId, date, shiftTypeId);

        if (existingScheduleCount > 0) {
            return new ApiResponse<>(false, "Nhân viên đã có lịch làm việc trong ca này (" + date + ") rồi.", null);
        }

        // 2. Kiểm tra Công suất Phòng
        Room room = roomRepo.findById(roomId)
                .orElseThrow(() -> new IllegalArgumentException("Không tìm thấy phòng với ID: " + roomId));

        int capacity = room.getCapacity();

        // Đếm số nhân viên hiện tại trong phòng/ca/ngày này
        long currentStaffCount = staffSchedulesRepo
                .countByRoomIdAndDateAndShiftTypeId(roomId, date, shiftTypeId);

        if (currentStaffCount >= capacity) {
            return new ApiResponse<>(false, "Lỗi: Phòng " + room.getName() + " (ID: " + roomId + ") đã đủ " + capacity +
                    " nhân viên được phân công trong ca này. Vui lòng chọn phòng khác.", null);
        }

        // Lấy thông tin liên quan đến ca làm việc
        Shift_type shiftType = shiftTypeRepo.findById(shiftTypeId)
                .orElseThrow(() -> new IllegalArgumentException("Không tìm thấy ca làm việc với ID: " + shiftTypeId));
        Staff staff = staffRepo.findById(staffId)
                .orElseThrow(() -> new IllegalArgumentException("Không tìm thấy nhân viên với ID: " + staffId));

        // 3. Thêm Lịch
        StaffSchedules newSchedule = new StaffSchedules();
        // Giả định StaffSchedule có các setters cho các trường (hoặc dùng constructors/builders)
        newSchedule.setStaff(staff);
        newSchedule.setShiftType(shiftType);
        newSchedule.setRoom(room);
        newSchedule.setDate(date);
        newSchedule.setStatus(request.getStatus()); // Đặt trạng thái mặc định

        staffSchedulesRepo.save(newSchedule);

        return new ApiResponse<>(true,
                "Thêm lịch làm việc thành công cho nhân viên " + staff.getUser().getFullname() + " tại phòng " + room.getName() + ".",
                null);
    }

    //Thêm lịch làm việc mới cho 1 nhân viên sau khi kiểm tra công suất phòng.
    public ApiResponse<?> addBulkSchedule(StaffsScheduleRequest request) {
        List<Integer> staffIds = request.getStaffId();
        Integer shiftTypeId = request.getShiftTypeId();
        Integer roomId = request.getRoomId();
        LocalDate date = request.getDate();

        // Danh sách lưu trữ kết quả
        List<Integer> addedStaffIds = new ArrayList<>();
        List<Integer> failedStaffIds = new ArrayList<>();
        int totalRequested = staffIds.size();

        // 1. Kiểm tra Công suất Phòng
        Room room = roomRepo.findById(roomId)
                .orElseThrow(() -> new IllegalArgumentException("Không tìm thấy phòng với ID: " + roomId));

        int capacity = room.getCapacity();

        // Đếm số nhân viên hiện tại trong phòng/ca/ngày này
        Integer currentStaffCount = staffSchedulesRepo
                .countByRoomIdAndDateAndShiftTypeId(roomId, date, shiftTypeId);

        // Tổng số nhân viên sau khi thêm = Số cũ + Số mới
        long totalStaffAfterAddition = currentStaffCount + totalRequested;

        if (totalStaffAfterAddition > capacity) {
            return new ApiResponse<>(false, "Lỗi: Phòng " + room.getName() + " (ID: " + roomId + ") chứa tối đa " + capacity +
                    " nhân viên. Hiện đã có " + currentStaffCount.toString() + " người. Vui lòng thay đổi.", null);
        }

        // 2. Kiểm tra Xung đột Cá nhân
        for (Integer staffId : staffIds) {
            long existingScheduleCount = staffSchedulesRepo
                    .countByStaffIdAndDateAndShiftTypeId(staffId, date, shiftTypeId);

            if (existingScheduleCount > 0) {
                failedStaffIds.add(staffId);
            }
            else {
                // Lấy thông tin liên quan đến ca làm việc
                Shift_type shiftType = shiftTypeRepo.findById(shiftTypeId)
                        .orElseThrow(() -> new IllegalArgumentException("Không tìm thấy ca làm việc với ID: " + shiftTypeId));
                Staff staff = staffRepo.findById(staffId)
                        .orElseThrow(() -> new IllegalArgumentException("Không tìm thấy nhân viên với ID: " + staffId));

                // 3. Thêm Lịch
                StaffSchedules newSchedule = new StaffSchedules();
                // Giả định StaffSchedule có các setters cho các trường (hoặc dùng constructors/builders)
                newSchedule.setStaff(staff);
                newSchedule.setShiftType(shiftType);
                newSchedule.setRoom(room);
                newSchedule.setDate(date);
                newSchedule.setStatus(request.getStatus()); // Đặt trạng thái mặc định

                staffSchedulesRepo.save(newSchedule);
                addedStaffIds.add(staffId);
            }
        }

        String message = "Thêm lịch làm việc thành công cho " + addedStaffIds.size() + " nhân viên.";
        if (!failedStaffIds.isEmpty()) {
            for (Integer staffId : failedStaffIds) {
                message += "\n";
                User user = staffRepo.findById(staffId)
                        .orElseThrow(() -> new IllegalArgumentException("Không tìm thấy nhân viên với ID: " + staffId))
                        .getUser();
                message += " Nhân viên " + user.getFullname() + " đã có lịch trong ca này.";
            }
        }

        return new ApiResponse<>(true,message, null);
    }

    /**
     * Lấy chi tiết lịch làm việc của một nhân viên trong phạm vi ngày.
     */
    public List<StaffScheduleResponse> getStaffSchedules(Integer staffId, LocalDate startDate) {
        LocalDate endDate = startDate.plusDays(6);

        List<StaffSchedules> schedules = staffSchedulesRepo
                .findByStaffIdAndDateBetweenOrderByDateAsc(staffId, startDate, endDate);

        // Ánh xạ (Mapping) từ Entity sang DTO
        return schedules.stream()
                .map(this::convertToDetailDTO)
                .collect(Collectors.toList());
    }

    public List<StaffScheduleResponse> getSchedulesOfStaffLogin(LocalDate startDate) {
        LocalDate endDate = startDate.plusDays(6);

        //Lấy id nhân viên từ user đang đăng nhập
        var auth = SecurityContextHolder.getContext().getAuthentication();
        if (!(auth.getPrincipal() instanceof CustomUserDetails cud)) {
            throw new AccessDeniedException("Unauthorized");
        }

        // Tìm nhân viên theo User ID
        Staff staff = staffRepo.findByUserId(cud.getId())
                .orElseThrow(() -> new IllegalArgumentException("Không tìm thấy nhân viên với User ID: " + cud.getId()));

        // Lấy lịch làm việc của nhân viên đang đăng nhập trong khoảng thời gian
        List<StaffSchedules> schedules = staffSchedulesRepo
                .findByStaffIdAndDateBetweenOrderByDateAsc(staff.getId(), startDate, endDate);

        // Ánh xạ (Mapping) từ Entity sang DTO
        return schedules.stream()
                .map(this::convertToDetailDTO)
                .collect(Collectors.toList());
    }

    // Hàm phụ trợ để ánh xạ và lấy thông tin chi tiết từ các bảng khác
    private StaffScheduleResponse convertToDetailDTO(StaffSchedules schedule) {
        StaffScheduleResponse dto = new StaffScheduleResponse();

        dto.setId(schedule.getId());
        dto.setDate(schedule.getDate());
        dto.setStatus(schedule.getStatus());
        dto.setLocation(schedule.getRoom().getName());
        dto.setShift(schedule.getShiftType().getName_type());
        dto.setStart_time(schedule.getShiftType().getStart_time());
        dto.setEnd_time(schedule.getShiftType().getEnd_time());

        return dto;
    }
}
