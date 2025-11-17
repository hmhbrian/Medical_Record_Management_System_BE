package com.example.clinicbooking.service;

import com.example.clinicbooking.DTO.Appointment.DoctorFilterByComplaintRequest;
import com.example.clinicbooking.DTO.Doctor.DoctorByComplaintResponse;
import com.example.clinicbooking.DTO.Doctor.DoctorRequest;
import com.example.clinicbooking.DTO.Doctor.DoctorResponse;
import com.example.clinicbooking.DTO.Doctor.DoctorScheduleResponse;
import com.example.clinicbooking.entity.Doctor;
import com.example.clinicbooking.entity.DoctorSchedules;
import com.example.clinicbooking.entity.Staff;
import com.example.clinicbooking.entity.User;
import com.example.clinicbooking.exceptions.InvalidInputException;
import com.example.clinicbooking.repository.*;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class DoctorService implements IDoctorService {
    private final DoctorRepository doctorRepo;
    private final SpecialtyRepository specialtyRepo;
    private final KeywordIcdHintRepository keywordIcdHintRepo;
    private final IcdSpecialtyRepository icdSpecialtyRepo;
    private final DoctorSchedulesRepository doctorSchedulesRepo;
    private StaffCreationService staffCreationService;

    private final Logger logger = LoggerFactory.getLogger(DoctorService.class);

    //Thêm mới bác sĩ
    @Override
    @Transactional
    public DoctorResponse create(DoctorRequest request){
        User user = staffCreationService.createUser(request);
        Staff staff = staffCreationService.createStaff(user, request.getDepartmentId(), request.getPositionId());

        Doctor doctor = new Doctor();
        doctor.setStaff(staff);
        doctor.setSpecialty(specialtyRepo.findById(request.specialtyId)
                .orElseThrow(() -> new InvalidInputException("Không tìm thấy chuyên khoa")));
        doctor.setExperienceYears(request.experienceYears);
        doctor.setCertificationName(request.certificationName);
        doctor.setIssuedBy(request.issuedBy);
        doctor.setIssueDate(request.issueDate);

        doctorRepo.save(doctor);
        return covertToResponse(doctor);
    }

    @Override
    public List<DoctorResponse> getAll() {
        List<Doctor> doctors = doctorRepo.findAllWithDetails();
        if (doctors.isEmpty()) {
            return Collections.emptyList(); // Trả về danh sách rỗng thay vì null
        }
        return doctors.stream()
                .map(this::covertToResponse)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public DoctorResponse getbyUserId(Integer userId){
        if (userId == null) {
            throw new InvalidInputException("User ID cannot be null");
        }

        try {
            // Use Optional directly instead of stream
            Doctor doctor = doctorRepo.findById(userId)
                    .orElseThrow(() -> new InvalidInputException("Doctor not found with ID: " + userId));
            return covertToResponse(doctor);
        } catch (Exception e) {
            e.printStackTrace();
            throw new InvalidInputException("Lỗi khi lấy bác sĩ");
        }
    }

    private DoctorResponse covertToResponse(Doctor doctor) {
        if (doctor == null) {
            return null;
        }

        DoctorResponse dto = new DoctorResponse();

        // Sử dụng Optional để bọc đối tượng Staff và tránh NPE
        Optional<Staff> staffOptional = Optional.ofNullable(doctor.getStaff());

        // Sử dụng Optional để bọc đối tượng User
        Optional<User> userOptional = staffOptional.map(Staff::getUser);

        // Truy cập các trường của User một cách an toàn
        if (userOptional.isPresent()) {
            User user = userOptional.get();
            dto.setFullname(user.getFullname());
            dto.setEmail(user.getEmail());
            dto.setAddress(user.getAddress());
            dto.setPhoneNumber(user.getPhoneNumber());
            dto.setDateOfBirth(user.getDateOfBirth());
            dto.setAvatar_url(user.getAvatar_url());
            dto.setGender(user.getGender());
        } else {
            // Thiết lập giá trị mặc định nếu User không tồn tại (Quan trọng để tránh lỗi 500)
            dto.setFullname("Tên không xác định");
            dto.setEmail(null);
            dto.setAddress(null);
            dto.setPhoneNumber(null);
            dto.setDateOfBirth(null);
            dto.setAvatar_url(null);
            dto.setGender(1);
        }

        //Xử lý an toàn cho StaffPosition ---
        staffOptional.map(Staff::getStaff_position)
                .ifPresent(position -> {
                    dto.setPositionId(position.getId());
                    dto.setPosition(position.getPosition());
                });

        //Ánh xạ các trường của Doctor---
        dto.setId(doctor.getId());
        dto.setDoctorcode(doctor.getDoctorcode());
        dto.setExperienceYears(doctor.getExperienceYears());
        dto.setCertificationName(doctor.getCertificationName());
        dto.setIssuedBy(doctor.getIssuedBy());
        dto.setIssueDate(doctor.getIssueDate());

        //Xử lý an toàn cho Specialty ---
        if(doctor.getSpecialty() != null) {
            dto.setSpecialtyId(doctor.getSpecialty().getId());
            dto.setSpecialty(doctor.getSpecialty().getName());
        } else {
            dto.setSpecialtyId(null);
            dto.setSpecialty("N/A");
        }

        // Xử lý an toàn cho Department (Thông qua Staff) ---
        staffOptional.map(Staff::getDepartment)
                .ifPresentOrElse(department -> {
                    dto.setDepartmentId(department.getId());
                    dto.setDepartment(department.getName());
                }, () -> {
                    dto.setDepartmentId(null);
                    dto.setDepartment("N/A");
                });

        return dto;
    }

    @Override
    public List<DoctorResponse> getDoctorsBySpecialtyId(Integer specialtyId) {
        try {
            return doctorRepo.findBySpecialtyId(specialtyId).stream()
                    .map(this::covertToResponse)
                    .collect(Collectors.toList());
        } catch (Exception e) {
            e.printStackTrace(); // hoặc dùng logger.error(...)
            throw new InvalidInputException("Lỗi khi lấy danh sách bác sĩ theo chuyên khoa");
        }
    }

    @Override
    public DoctorResponse getDoctorsById(Integer doctorId) {
        if (doctorId == null) {
            throw new InvalidInputException("Doctor ID cannot be null");
        }

        try {
            // Use Optional directly instead of stream
            Doctor doctor = doctorRepo.findByIdWithDetails(doctorId)
                    .orElseThrow(() -> new InvalidInputException("Doctor not found with ID: " + doctorId));
            return covertToResponse(doctor);
        } catch (InvalidInputException e) {
            throw new InvalidInputException("Doctor không tồn tại." + e.getMessage());
        }catch (Exception e) {
            e.printStackTrace();
            throw new InvalidInputException("Lỗi xử lý dữ liệu bác sĩ (ID: " + doctorId + "). Vui lòng kiểm tra log.");
        }
    }

    @Override
    @Transactional
    public DoctorResponse update(Integer id, DoctorRequest request) {
        Doctor doctor = doctorRepo.findById(id)
                .orElseThrow(() -> new InvalidInputException("Doctor not found"));

        User user = doctor.getStaff().getUser();
        Staff staff = doctor.getStaff();

        //cập nhật user & staff
        staffCreationService.updateUserAndStaff(user, staff, request, request.getDepartmentId(), request.getPositionId());

        // Cập nhật doctor
        doctor.setSpecialty(specialtyRepo.findById(request.specialtyId)
                .orElseThrow(() -> new InvalidInputException("Không tìm thấy chuyên khoa")));
        doctor.setExperienceYears(request.experienceYears);
        doctor.setCertificationName(request.certificationName);
        doctor.setIssuedBy(request.issuedBy);
        doctor.setIssueDate(request.issueDate);
        doctorRepo.save(doctor);
        return covertToResponse(doctor);
    }

    @Transactional
    public void delete(Integer id) {
        Doctor doctor = doctorRepo.findById(id)
                .orElseThrow(() -> new InvalidInputException("Doctor not found"));

        Staff staff = doctor.getStaff();

        // Xóa doctor
        doctorRepo.delete(doctor);

        // xóa user và staff
        staffCreationService.deleteUserAndStaff(staff);
    }

    public List<DoctorByComplaintResponse> getAvailableDoctorsByComplaint(DoctorFilterByComplaintRequest request){
        // 0.Chuẩn bị
        // Làm sạch và chuẩn hóa chief complaint
        String cleanedComplaint = request.getChiefComplaint()
                .toLowerCase()
                .replaceAll("[.,;:\\-]", " ") // Thay thế dấu câu bằng khoảng trắng
                .replaceAll("\\s+", " ") // Thay thế nhiều khoảng trắng thành một khoảng trắng duy nhất
                .trim();

        //Thêm khoảng trắng vào ĐẦU VÀ CUỐI chuỗi để tạo ranh giới từ ảo
        String boundedComplaint = " " + cleanedComplaint + " "; // Ví dụ: " đau đầu chóng mặt "

        logger.info("Chief Complaint gốc: {}", request.getChiefComplaint());
        logger.info("Chuỗi đã chuẩn hóa (có ranh giới): '{}'", boundedComplaint);

        LocalDate searchDate = LocalDate.parse(request.getDate(), DateTimeFormatter.ISO_LOCAL_DATE);

        // 1.Phân tích Từ khóa và Tìm ICD Hint
        Set<String> icdPrefixes = keywordIcdHintRepo.findDistinctIcdPrefixByKeywords(boundedComplaint);

        if (icdPrefixes.isEmpty()) {
            // Nếu không tìm thấy gợi ý nào, mặc định trả về bác sĩ của Ngoại tổng quát (SpecialtyID = 3) hoặc Nội tổng quát.
            icdPrefixes.add("Z"); // Gán tiền tố cho Khám Tổng quát
            logger.warn("Không tìm thấy ICD Prefix nào cho chuỗi: {}", request.getChiefComplaint());
        }else {
            logger.info("ICD Prefixes tìm thấy: {}", icdPrefixes);
        }

        // 2.Truy vấn bảng ICD_Specialty_Map
        Set<Integer> specialtyIds = icdSpecialtyRepo.findDistinctSpecialtyIdsByIcdPrefixIn(icdPrefixes);

        if (specialtyIds.isEmpty()) {
            logger.warn("Không tìm thấy Specialty ID nào sau khi ánh xạ từ ICD Prefixes.");
            // Trường hợp không có mapping, trả về danh sách rỗng hoặc chuyên khoa mặc định
            return Collections.emptyList();
        }else {
            logger.info("Specialty IDs được chọn: {}", specialtyIds);
        }

        // 3. Lấy tất cả Doctor có specialty ID liên quan
        List<Doctor> doctors = doctorRepo.findBySpecialtyIdIn(specialtyIds);

        if (doctors.isEmpty()) {
            logger.error("KHÔNG TÌM ĐƯỢC BÁC SĨ THỎA MÃN.");
        } else {
            // Chỉ in ra ID hoặc Tên bác sĩ để tránh log quá dài
            String doctorNames = doctors.stream()
                    .map(Doctor::getDoctorcode) // Giả sử Doctor có getName()
                    .collect(Collectors.joining(", "));

            logger.info("✅ Kết quả: {} Bác sĩ được đề xuất: {}", doctors.size(), doctorNames);
        }

        // 4. Lọc lịch làm việc của các bác sĩ này trong ngày hôm nay
        List<DoctorByComplaintResponse> result = new ArrayList<>();

        for (Doctor doctor : doctors) {
            // Tìm tất cả ca làm việc trong ngày và còn chỗ (booked_patients < max_patients)
            List<DoctorSchedules> schedules = doctorSchedulesRepo.findAvailableSchedules(
                    doctor.getId(),
                    searchDate
            );

            if (!schedules.isEmpty()) {
                // Chuyển đổi Doctor và Schedules thành DTO trả về
                DoctorByComplaintResponse response = mapToResponse(doctor);
                response.setSchedules(mapSchedulesToDTO(schedules));
                result.add(response);
            }
        }

        return result;
    }

    // Hàm ánh xạ Doctor Entity sang DoctorByComplaintResponse DTO
    private DoctorByComplaintResponse mapToResponse(Doctor doctor) {
        DoctorByComplaintResponse response = new DoctorByComplaintResponse();

        // 1. Ánh xạ từ Doctor Entity
        response.setDoctorId(doctor.getId());

        // liên kết từ Doctor -> Staff -> User để lấy tên
        // Lấy tên bác sĩ từ User Entity (liên kết qua Staff)
        if (doctor.getStaff() != null && doctor.getStaff().getUser() != null) {
            response.setDoctorName(doctor.getStaff().getUser().getFullname());
        } else {
            response.setDoctorName("N/A");
        }

        // 2. Ánh xạ từ Specialty Entity
        if (doctor.getSpecialty() != null) {
            response.setSpecialtyName(doctor.getSpecialty().getName());
        } else {
            response.setSpecialtyName("Chuyên khoa không xác định");
        }

        // List<DoctorScheduleDTO> availableSlots sẽ được set trong hàm Service chính sau khi gọi doctorSchedulesRepository

        return response;
    }

    // Hàm ánh xạ danh sách DoctorSchedules sang danh sách DoctorScheduleResponse DTO
    private List<DoctorScheduleResponse> mapSchedulesToDTO(List<DoctorSchedules> schedules) {
        return schedules.stream()
                .map(ds -> {
                    DoctorScheduleResponse dto = new DoctorScheduleResponse();
                    dto.setId(ds.getId());
                    dto.setMaxPatients(ds.getMaxPatients());
                    dto.setBookedPatients(ds.getBookedPatients());
                    dto.setLocation(ds.getRoom().getRoomNumber());

                    // Giả định bạn có liên kết từ DoctorSchedules -> ShiftType (chứa StartTime, EndTime, ShiftName)
                    if (ds.getShiftType() != null) {
                        dto.setShift(ds.getShiftType().getName_type());
                        dto.setStart_time(ds.getShiftType().getStart_time());
                        dto.setEnd_time(ds.getShiftType().getEnd_time());
                    }
                    return dto;
                })
                .collect(Collectors.toList());
    }
}
