package com.example.ClinicBooking.service;

import com.example.ClinicBooking.DTO.BaseUserRequest;
import com.example.ClinicBooking.entity.Department;
import com.example.ClinicBooking.entity.Staff;
import com.example.ClinicBooking.entity.User;
import com.example.ClinicBooking.entity.staff_position;
import com.example.ClinicBooking.repository.*;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class StaffCreationService {
    private final UserRepository userRepo;
    private final StaffRepository staffRepo;
    private final DepartmentRepository departmentRepo;
    private final StaffPositionRepository staffPositionRepo;
    private final PasswordEncoder passwordEncoder;

    public StaffCreationService(UserRepository userRepo,
                         StaffRepository staffRepo,
                         PasswordEncoder passwordEncoder,
                         DepartmentRepository departmentRepo,
                         StaffPositionRepository staffPositionRepo) {
        this.userRepo = userRepo;
        this.staffRepo = staffRepo;
        this.passwordEncoder = passwordEncoder;
        this.departmentRepo = departmentRepo;
        this.staffPositionRepo = staffPositionRepo;
    }

    public User createUser(BaseUserRequest request) {
        if (userRepo.findByEmail(request.email).isPresent()) {
            throw new RuntimeException("Email đã tồn tại");
        }

        User user = new User();
        user.setFullname(request.fullname);
        user.setEmail(request.email);
        user.setPhoneNumber(request.phoneNumber);
        user.setAddress(request.address);
        user.setDateOfBirth(request.dateOfBirth);
        user.setGender(request.gender);
        user.setPass(passwordEncoder.encode(request.password));
        user.setRole(2);
        return userRepo.save(user);
    }


    public Staff createStaff(User user, int departmentId, int positionId) {
        Department dept = departmentRepo.findById(departmentId)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy khoa"));
        staff_position pos = staffPositionRepo.findById(positionId)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy vị trí"));

        Staff staff = new Staff();
        staff.setUser(user);
        staff.setDepartment(dept);
        staff.setPosition(pos);
        return staffRepo.save(staff);
    }
}
