package com.example.clinicbooking.service;

import com.example.clinicbooking.DTO.Appointment.AppointmentDTO;
import com.example.clinicbooking.DTO.Appointment.AppointmentRequest;
import com.example.clinicbooking.DTO.Appointment.AppointmentSearchRequest;
import com.example.clinicbooking.DTO.Appointment.StatusHistoryItemDTO;
import com.example.clinicbooking.entity.*;
import com.example.clinicbooking.repository.*;
import jakarta.persistence.criteria.Join;
import jakarta.persistence.criteria.Root;
import jakarta.persistence.criteria.Subquery;
import jakarta.persistence.criteria.*;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.*;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AppointmentService {
    private final DoctorSchedulesRepository doctorScheduleRepository;
    private final PatientRepository patientRepository;
    private final AppointmentRepository appointmentRepository;
    private final AppointmentStatusRepository appointmentStatusRepository;
    private final ScheduleSlotRepository scheduleSlotRepo;
    private final DoctorRepository doctorRepository;
    private final UserRepository UserRepository;

    //đặt lịch khám mới của bệnh nhân
    public AppointmentDTO bookAppointment(AppointmentRequest request) {
        Patient patient = patientRepository.findById(request.getPatientId())
                .orElseThrow(() -> new RuntimeException("Patient not found"));
        DoctorSchedules schedule = doctorScheduleRepository.findById(request.getDoctorScheduleId())
                .orElseThrow(() -> new RuntimeException("Schedule not found"));
        Doctor doctor = doctorRepository.findById(request.getDoctorId())
                .orElseThrow(() -> new RuntimeException("Doctor not found"));
        ScheduleSlot slot = scheduleSlotRepo.findById(request.getScheduleSlotId())
                .orElseThrow(() -> new RuntimeException("Schedule slot not found"));

        if (schedule.getBookedPatients() >= schedule.getMaxPatients()) {
            throw new RuntimeException("No available slots");
        }

        Appointment appointment = new Appointment();
        appointment.setPatient(patient);
        appointment.setDoctor(doctor);
        appointment.setDoctorSchedule(schedule);
        appointment.setScheduleSlot(slot);
        appointment.setPresentTime(LocalDateTime.now());

        Appointment savedAppointment = appointmentRepository.save(appointment);

        AppointmentStatus status = new AppointmentStatus();
        status.setAppointment(savedAppointment);
        status.setStatus(1);
        status.setUpdateAt(LocalDateTime.now());
        status.setReason(request.getReason());
        status.setUpdate_by(patient.getUser());
        appointmentStatusRepository.save(status);

        //Tăng số bệnh nhân đã đặt trong DoctorSchedules
        schedule.setBookedPatients(schedule.getBookedPatients() + 1);
        doctorScheduleRepository.save(schedule);

        // Cập nhật trạng thái của ScheduleSlot thành đã được đặt
        slot.setIsBooked(true);
        scheduleSlotRepo.save(slot);

        return covertToResponse(savedAppointment);
    }

    //Cập nhật trạng thái lịch hẹn (VD:"Chờ xác nhận", “Đã xác nhận”, “Hoàn thành”, “Hủy”).
    public void ConfirmAppointment(int appointmentId, int updatedByUserId) {
        Appointment appointment = appointmentRepository.findById(appointmentId)
                .orElseThrow(() -> new RuntimeException("Appointment not found"));

        // Tạo mới trạng thái
        AppointmentStatus status = new AppointmentStatus();
        status.setAppointment(appointment);
        status.setStatus(2); // Đã xác nhận
        status.setUpdateAt(LocalDateTime.now());

        User updatedBy = UserRepository.findById(updatedByUserId)
                .orElseThrow(() -> new RuntimeException("User not found"));
        status.setUpdate_by(updatedBy);

        appointmentStatusRepository.save(status);
    }

    public void DeleteAppointment(int appointmentId, int updatedByUserId, String reason) {
        Appointment appointment = appointmentRepository.findById(appointmentId)
                .orElseThrow(() -> new RuntimeException("Appointment not found"));

        // Tạo mới trạng thái
        AppointmentStatus status = new AppointmentStatus();
        status.setAppointment(appointment);
        status.setStatus(4); // Hủy
        status.setReason(reason);
        status.setUpdateAt(LocalDateTime.now());

        User updatedBy = UserRepository.findById(updatedByUserId)
                .orElseThrow(() -> new RuntimeException("User not found"));
        status.setUpdate_by(updatedBy);

        //Giảm số bệnh nhân đã đặt trong DoctorSchedules
        DoctorSchedules schedule = appointment.getDoctorSchedule();
        schedule.setBookedPatients(schedule.getBookedPatients() - 1);
        doctorScheduleRepository.save(schedule);

        // Cập nhật trạng thái của ScheduleSlot thành chưa được đặt
        ScheduleSlot slot = appointment.getScheduleSlot();
        slot.setIsBooked(false);
        scheduleSlotRepo.save(slot);

        appointmentStatusRepository.save(status);
    }



    //Lấy danh sách tất cả các lịch hẹn của một bệnh nhân cụ thể.
    public List<AppointmentDTO> getAppointmentsByPatient(int patientId) {
        return appointmentRepository.findByPatientId(patientId)
                .stream()
                .map(this::covertToResponse)
                .collect(Collectors.toList());
    }

    //Lấy tất cả các lịch hẹn thuộc một ca làm việc cụ thể của bác sĩ.
    public List<AppointmentDTO> getAppointmentsByDoctorSchedule(int doctorScheduleId) {
        return appointmentRepository.findByDoctorScheduleId(doctorScheduleId)
                .stream()
                .map(this::covertToResponse)
                .collect(Collectors.toList());
    }

    //Lấy danh sách các lịch hẹn theo trạng thái hiện tại
    public List<AppointmentDTO> getAppointmentsByStatus(int status) {
        return appointmentRepository.findByStatus(status)
                .stream()
                .map(this::covertToResponse)
                .collect(Collectors.toList());
    }

    //Lấy tất cả các lịch hẹn trong một tuần tính từ startDate
    public List<AppointmentDTO> getAppointmentsByWeek(LocalDate startDate) {
        LocalDate endDate = startDate.plusDays(6);
        return appointmentRepository.findByAppointmentDateBetween(startDate, endDate)
                .stream()
                .map(this::covertToResponse)
                .collect(Collectors.toList());
    }

    //Lấy danh sách lịch hẹn của một bác sĩ cụ thể, không giới hạn thời gian.
    public List<AppointmentDTO> getAppointmentsByDoctor(int userId) {
        Doctor doctor = doctorRepository.findByStaff_User_Id(userId)
                .orElseThrow(() -> new RuntimeException("Doctor not found for user id: " + userId));

        return appointmentRepository.findByDoctorId(doctor.getId())
                .stream()
                .map(this::covertToResponse)
                .collect(Collectors.toList());
    }

    /** Phân trang + tìm kiếm + lọc động */
    public Page<AppointmentDTO> searchAppointments(AppointmentSearchRequest req) {
        // Parse sort
        Pageable pageable = PageRequest.of(
                Optional.ofNullable(req.getPage()).orElse(0),
                Optional.ofNullable(req.getSize()).orElse(10),
                Sort.by(Sort.Direction.DESC, "presentTime")
        );

        Specification<Appointment> spec = buildSearchSpec(
                req.getKeyword(),
                Optional.ofNullable(req.getStatus()).orElse(0),
                req.getDepartmentId(),
                req.getFromDate()
        );

        Page<Appointment> page = appointmentRepository.findAll(spec, pageable);
        List<AppointmentDTO> data = page.getContent().stream()
                .map(this::covertToResponse)
                .collect(Collectors.toList());

        return new PageImpl<>(data, pageable, page.getTotalElements());
    }

    /** Tạo Specification động theo filters */
    private Specification<Appointment> buildSearchSpec(
            String keyword,
            int status,
            Integer departmentId,
            LocalDate fromDate
    ) {
        return (root, query, cb) -> {
            List<Predicate> predicates = new ArrayList<>();

            // Join các cấu trúc liên quan
            // Appointment -> Patient -> User
            Join<Appointment, Patient> patientJoin = root.join("patient");
            Join<Patient, User> patientUserJoin = patientJoin.join("user");

            // Appointment -> Doctor -> Staff -> User
            Join<Appointment, Doctor> doctorJoin = root.join("doctor");
            Join<Doctor, Staff> staffJoin = doctorJoin.join("staff");
            Join<Staff, User> doctorUserJoin = staffJoin.join("user");

            // Appointment -> DoctorSchedule
            Join<Appointment, DoctorSchedules> scheduleJoin = root.join("doctorSchedule");

            // Appointment -> Doctor -> Specialty -> Department
            Join<Doctor, Specialty> specialtyJoin = doctorJoin.join("specialty");
            Join<Specialty, Department> deptJoin = specialtyJoin.join("department");

            // 1) Keyword: bệnh nhân (fullname), mã lịch hẹn (code), bác sĩ (fullname)
            if (keyword != null && !keyword.isBlank()) {
                String like = "%" + keyword.trim().toLowerCase() + "%";
                Predicate byPatientName = cb.like(cb.lower(patientUserJoin.get("fullname")), like);
                Predicate byCode        = cb.like(cb.lower(root.get("code")), like);
                Predicate byDoctorName  = cb.like(cb.lower(doctorUserJoin.get("fullname")), like);
                predicates.add(cb.or(byPatientName, byCode, byDoctorName));
            }

            // 2) Khoa (department)
            if (departmentId != null) {
                predicates.add(cb.equal(deptJoin.get("id"), departmentId));
            }

            // 3) fromDate: date của doctorSchedule >= fromDate
            if (fromDate != null) {
                predicates.add(cb.greaterThanOrEqualTo(scheduleJoin.get("date"), fromDate));
            }

            // 4) Status của bản ghi AppointmentStatus MỚI NHẤT
            if (status > 0) {
                // Subquery: lấy MAX(updateAt) theo appointment.id
                Subquery<LocalDateTime> subMaxUpdate = query.subquery(LocalDateTime.class);
                Root<AppointmentStatus> s1 = subMaxUpdate.from(AppointmentStatus.class);
                // Ép kiểu generic tại đây:
                Path<LocalDateTime> updateAtPath = s1.<LocalDateTime>get("updateAt");

                subMaxUpdate.select(cb.greatest(updateAtPath))
                        .where(cb.equal(s1.get("appointment").get("id"), root.get("id")));

                // Alias join AppointmentStatus để so sánh với subquery
                Subquery<Integer> existsLatest = query.subquery(Integer.class);
                Root<AppointmentStatus> s2 = existsLatest.from(AppointmentStatus.class);

                existsLatest.select(cb.literal(1))
                        .where(
                                cb.equal(s2.get("appointment").get("id"), root.get("id")),
                                cb.equal(s2.get("status"), status),                 // status là int
                                cb.equal(s2.get("updateAt"), subMaxUpdate)          // so khớp với MAX(updateAt)
                        );

                // Quan trọng: tránh nhân bản bản ghi do join thêm bảng s2
                predicates.add(cb.exists(existsLatest));
                query.distinct(true);
            }

            return cb.and(predicates.toArray(new Predicate[0]));
        };
    }

    /** parse "field,ASC|DESC;field2,DESC" -> Sort */
    private Sort parseSort(String sortStr) {
        if (sortStr == null || sortStr.isBlank()) {
            return Sort.by(Sort.Direction.DESC, "presentTime");
        }
        try {
            List<Sort.Order> orders = Arrays.stream(sortStr.split(";"))
                    .map(String::trim)
                    .filter(s -> !s.isBlank())
                    .map(s -> {
                        String[] parts = s.split(",");
                        String field = parts[0].trim();
                        Sort.Direction dir = (parts.length > 1 && "ASC".equalsIgnoreCase(parts[1].trim()))
                                ? Sort.Direction.ASC : Sort.Direction.DESC;
                        return new Sort.Order(dir, field);
                    })
                    .toList();
            if (orders.isEmpty()) return Sort.by(Sort.Direction.DESC, "presentTime");
            return Sort.by(orders);
        } catch (Exception e) {
            return Sort.by(Sort.Direction.DESC, "presentTime");
        }
    }

    private AppointmentDTO covertToResponse(Appointment appointment) {
        AppointmentDTO dto = new AppointmentDTO();
        //String appointmentTime = appointment.getScheduleSlot().getStartTime() + " - " + appointment.getScheduleSlot().getEndTime();
        // Null-safe cho slot
        ScheduleSlot slot = appointment.getScheduleSlot();
        String appointmentTime;
        if (slot != null && slot.getStartTime() != null && slot.getEndTime() != null) {
            appointmentTime = slot.getStartTime() + " - " + slot.getEndTime();
        } else if (appointment.getDoctorSchedule() != null
                && appointment.getDoctorSchedule().getShiftType() != null
                && appointment.getDoctorSchedule().getShiftType().getStart_time() != null
                && appointment.getDoctorSchedule().getShiftType().getEnd_time() != null) {
            // fallback theo giờ của ca/shift nếu muốn
            appointmentTime = appointment.getDoctorSchedule().getShiftType().getStart_time()
                    + " - "
                    + appointment.getDoctorSchedule().getShiftType().getEnd_time();
        } else {
            appointmentTime = "Chưa chọn khung giờ";
        }
        dto.setId(appointment.getId());
        dto.setCode(appointment.getCode());
        dto.setSpecialtyId(appointment.getDoctor().getSpecialty().getDepartment().getId());
        dto.setDoctorName(appointment.getDoctor().getStaff().getUser().getFullname());
        dto.setDoctorSpecialty(appointment.getDoctor().getSpecialty().getDepartment().getName());
        dto.setPatientName(appointment.getPatient().getUser().getFullname());
        dto.setPatientYearOfBirth(appointment.getPatient().getUser().getDateOfBirth());
        dto.setPatientPhone(appointment.getPatient().getUser().getPhoneNumber());
        dto.setPatientEmail(appointment.getPatient().getUser().getEmail());
        dto.setPatientGender(appointment.getPatient().getUser().getGender() == 1 ? "Nữ" : "Nam");
        dto.setPatientAge(appointment.getPatient().getUser().getAge());
        dto.setPresentTime(appointment.getPresentTime());
        dto.setRoomName(appointment.getDoctorSchedule().getRoom().getName());
        dto.setAppointmentDate(appointment.getDoctorSchedule().getDate().toString());
        dto.setAppointmentTime(appointmentTime);
        dto.setDoctorScheduleId(appointment.getDoctorSchedule().getId());

        Optional<AppointmentStatus> statusOpt = appointmentStatusRepository
                .findTopByAppointmentIdOrderByUpdateAtDesc(appointment.getId());
        int statusId = statusOpt.map(AppointmentStatus::getStatus).orElse(0);
        switch (statusId) {
            case 1 -> {
                dto.setStatus("Chờ xác nhận");
                dto.setStatusId(statusId);
            }
            case 2 -> {
                dto.setStatus("Đã xác nhận");
                dto.setStatusId(statusId);
            }
            case 3 -> {
                dto.setStatus("Hoàn thành");
                dto.setStatusId(statusId);
            }
            case 4 -> {
                dto.setStatus("Hủy");
                dto.setStatusId(statusId);
            }
            default -> {
                dto.setStatus("Không xác định");
                dto.setStatusId(0);
            }
        };

        //Reason của riêng trạng thái "Chờ xác nhận" (kể cả hiện tại đã đổi trạng thái)
        String pendingReason = appointmentStatusRepository
                .findByAppointmentIdAndStatus(appointment.getId(), 1)
                .map(AppointmentStatus::getReason)
                .orElse("");
        dto.setReason(pendingReason);

        //danh sách lịch sử trạng thái để hiển thị rõ ràng
        var history = appointmentStatusRepository
                .findByAppointmentIdOrderByUpdateAtDesc(appointment.getId())
                .stream()
                .map(this::mapStatusToHistoryItem)
                .toList();
        dto.setStatusHistory(history);

        return dto;
    }

    private StatusHistoryItemDTO mapStatusToHistoryItem(AppointmentStatus st) {
        String statusName = switch (st.getStatus()) {
            case 1 -> "Chờ xác nhận";
            case 2 -> "Đã xác nhận";
            case 3 -> "Hoàn thành";
            case 4 -> "Hủy";
            default -> "Không xác định";
        };
        Integer updatedById = (st.getUpdate_by() != null) ? st.getUpdate_by().getId() : null;
        String updatedByName = (st.getUpdate_by() != null)
                ? roleName(st.getUpdate_by().getRole())
                : null;

        return new StatusHistoryItemDTO(
                st.getStatus(),
                statusName,
                st.getReason(),
                st.getUpdateAt(),
                updatedById,
                updatedByName
        );
    }

    private static String roleName(Integer role) {
        if (role == null) return null;
        return switch (role) {
            case 0 -> "Admin";
            case 1 -> "Patient";
            case 2 -> "Doctor";
            default -> "Unknown";
        };
    }
}
