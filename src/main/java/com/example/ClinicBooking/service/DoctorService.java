package com.example.ClinicBooking.service;

import com.example.ClinicBooking.DTO.DoctorRequest;
import com.example.ClinicBooking.DTO.DoctorResponse;
import com.example.ClinicBooking.entity.Doctor;
import com.example.ClinicBooking.entity.Patient;
import com.example.ClinicBooking.entity.Staff;
import com.example.ClinicBooking.entity.User;
import com.example.ClinicBooking.repository.*;
import jakarta.transaction.Transactional;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class DoctorService implements IUserService<DoctorResponse, DoctorRequest> {
    private final UserRepository userRepo;
    private final StaffRepository staffRepo;
    private final DoctorRepository doctorRepo;
    private final DepartmentRepository departmentRepo;
    private final SpecialtyRepository specialtyRepo;
    private final StaffPositionRepository staffPositionRepo;
    private final PasswordEncoder passwordEncoder;

    public DoctorService(UserRepository userRepo,
                         StaffRepository staffRepo,
                         DoctorRepository doctorRepo,
                         PasswordEncoder passwordEncoder,
                         DepartmentRepository departmentRepo,
                         SpecialtyRepository specialtyRepo,
                         StaffPositionRepository staffPositionRepo) {
        this.userRepo = userRepo;
        this.staffRepo = staffRepo;
        this.doctorRepo = doctorRepo;
        this.passwordEncoder = passwordEncoder;
        this.departmentRepo = departmentRepo;
        this.specialtyRepo = specialtyRepo;
        this.staffPositionRepo = staffPositionRepo;
    }

    @Override
    @Transactional
    public DoctorResponse create(DoctorRequest request){
        if(userRepo.findByEmail(request.getEmail()).isPresent()){
            throw new RuntimeException("Email đã tồn tại");
        }

        User user = new User();
        user.setFullname(request.fullname);
        user.setEmail(request.email);
        user.setAddress(request.address);
        user.setPhoneNumber(request.phoneNumber);
        user.setDateOfBirth(request.dateOfBirth);
        user.setGender(request.gender);
        user.setRole(2);
        user.setPass(passwordEncoder.encode(request.password));
        userRepo.save(user);

        Staff staff = new Staff();
        staff.setUser(user);
        staff.setDepartment(departmentRepo.findById(request.getDepartmentId()).
                orElseThrow(() -> new RuntimeException("Không tìm thấy khoa")));
        staff.setPosition(staffPositionRepo.findById(request.getPositionId()).
                orElseThrow(() -> new RuntimeException("Không tìm thấy vị trí làm việc")));
        staffRepo.save(staff);

        Doctor doctor = new Doctor();
        doctor.setStaff(staff);
        doctor.setSpecialty(specialtyRepo.findById(request.specialtyId)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy chuyên khoa")));
        doctor.setExperienceYears(request.experienceYears);
        doctor.setCertificationName(request.certificationName);
        doctor.setIssuedBy(request.issuedBy);
        doctor.setIssueDate(request.issueDate);

        doctorRepo.save(doctor);
        return covertToResponse(doctor);
    }

    @Override
    public List<DoctorResponse> getAll() {
        return doctorRepo.findAll().stream()
                .map(this::covertToResponse)
                .collect(Collectors.toList());
    }

    private DoctorResponse covertToResponse(Doctor doctor) {
        DoctorResponse dto = new DoctorResponse();
        dto.setDoctorcode(doctor.getDoctorcode());
        dto.setFullname(doctor.getStaff().getUser().getFullname());
        dto.setEmail(doctor.getStaff().getUser().getEmail());
        dto.setAddress(doctor.getStaff().getUser().getAddress());
        dto.setPhoneNumber(doctor.getStaff().getUser().getPhoneNumber());
        dto.setDateOfBirth(doctor.getStaff().getUser().getDateOfBirth());
        dto.setGender(doctor.getStaff().getUser().getGender());
        dto.setDepartment(doctor.getStaff().getDepartment().getName());
        dto.setSpecialty(doctor.getSpecialty().getName());
        dto.setExperienceYears(doctor.getExperienceYears());
        dto.setCertificationName(doctor.getCertificationName());
        dto.setIssuedBy(doctor.getIssuedBy());
        dto.setIssueDate(doctor.getIssueDate());
        return dto;
    }
}
