package com.example.clinicbooking.service.Appointment;

import com.example.clinicbooking.DTO.ApiResponse;
import com.example.clinicbooking.DTO.Appointment.*;
import com.example.clinicbooking.DTO.Dashboard.AppointmentOverviewDTO;
import com.example.clinicbooking.DTO.PaginatedResponseDTO;
import com.example.clinicbooking.entity.*;
import com.example.clinicbooking.exceptions.InvalidInputException;
import com.example.clinicbooking.repository.*;
import com.example.clinicbooking.security.CustomUserDetails;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.*;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

import static com.example.clinicbooking.service.Appointment.AppointmentSpecification.buildSearchAppointmentSpec;
import static com.example.clinicbooking.service.Appointment.QueueSpecification.filterQueue;

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
    private final StaffRepository StaffRepo;
    private final MedicalRecordRepository medicalRecordRepo;
    private final PaymentRepository paymentRepository;
    private final ScheduleSlotRepository scheduleSlotRepository;
    private final UserRepository userRepository;

    private static final Logger logger = LoggerFactory.getLogger(AppointmentService.class);

    // đặt lịch khám mới của bệnh nhân
    @Transactional
    public AppointmentDTO bookAppointment(AppointmentRequest request) {
        Patient patient = patientRepository.findById(request.getPatientId())
                .orElseThrow(() -> new InvalidInputException("Patient not found with id: " + request.getPatientId()));
        DoctorSchedules schedule = doctorScheduleRepository.findById(request.getDoctorScheduleId())
                .orElseThrow(() -> new InvalidInputException(
                        "Schedule not found with id: " + request.getDoctorScheduleId()));
        Doctor doctor = doctorRepository.findById(request.getDoctorId())
                .orElseThrow(() -> new InvalidInputException("Doctor not found with id: " + request.getDoctorId()));
        ScheduleSlot slot = scheduleSlotRepo.findById(request.getScheduleSlotId())
                .orElseThrow(() -> new InvalidInputException(
                        "Schedule slot not found with id: " + request.getScheduleSlotId()));

        if (schedule.getBookedPatients() >= schedule.getMaxPatients()) {
            throw new InvalidInputException("No available slots for the selected schedule.");
        }

        Appointment appointment = new Appointment();
        appointment.setPatient(patient);
        appointment.setDoctor(doctor);
        appointment.setDoctorSchedule(schedule);
        appointment.setScheduleSlot(slot);
        appointment.setPresentTime(LocalDateTime.now());
        appointment.setVisitType("scheduled");

        Appointment savedAppointment = appointmentRepository.save(appointment);

        AppointmentStatus status = new AppointmentStatus();
        status.setAppointment(savedAppointment);
        status.setStatus(1);
        status.setUpdateAt(LocalDateTime.now());
        status.setReason(request.getReason());
        status.setUpdate_by(patient.getUser());
        appointmentStatusRepository.save(status);

        // Tăng số bệnh nhân đã đặt trong DoctorSchedules
        schedule.setBookedPatients(schedule.getBookedPatients() + 1);
        doctorScheduleRepository.save(schedule);

        // Cập nhật trạng thái của ScheduleSlot thành đã được đặt
        slot.setIsBooked(true);
        scheduleSlotRepo.save(slot);

        return covertToResponse(savedAppointment);
    }

    // Xác nhận lịch hẹn
    @Transactional
    public Appointment ConfirmAppointment(int appointmentId, int updatedByUserId) {
        Appointment appointment = appointmentRepository.findByIdWithDetails(appointmentId)
                .orElseThrow(() -> new InvalidInputException("Appointment not found with id: " + appointmentId));

        // Kiểm tra trạng thái hiện tại
        AppointmentStatus appointmentStatus = appointmentStatusRepository
                .findTopByAppointmentIdOrderByUpdateAtDesc(appointmentId)
                .orElseThrow(() -> new InvalidInputException(
                        "Appointment status not found for appointment id: " + appointmentId));
        // Chỉ cho phép xác nhận nếu trạng thái hiện tại là "Chờ xác nhận" (1)
        if (appointmentStatus.getStatus() > 2)
            throw new InvalidInputException("Cuộc hẹn này đã qua giai đoạn xác nhận.");

        // Tạo mới trạng thái
        AppointmentStatus status = new AppointmentStatus();
        status.setAppointment(appointment);
        status.setStatus(2); // Đã xác nhận
        status.setUpdateAt(LocalDateTime.now());

        User updatedBy = UserRepository.findById(updatedByUserId)
                .orElseThrow(() -> new InvalidInputException("User not found with id: " + updatedByUserId));
        status.setUpdate_by(updatedBy);

        appointmentStatusRepository.save(status);

        return appointment;
    }

    // Hủy lịch hẹn
    @Transactional
    public Appointment DeleteAppointment(int appointmentId, int updatedByUserId, String reason) {
        Appointment appointment = appointmentRepository.findById(appointmentId)
                .orElseThrow(() -> new InvalidInputException("Appointment not found"));

        // Tạo mới trạng thái
        AppointmentStatus status = new AppointmentStatus();
        status.setAppointment(appointment);
        status.setStatus(6); // Hủy
        status.setReason(reason);
        status.setUpdateAt(LocalDateTime.now());

        User updatedBy = UserRepository.findById(updatedByUserId)
                .orElseThrow(() -> new InvalidInputException("User not found"));
        status.setUpdate_by(updatedBy);

        // Giảm số bệnh nhân đã đặt trong DoctorSchedules
        DoctorSchedules schedule = appointment.getDoctorSchedule();
        schedule.setBookedPatients(schedule.getBookedPatients() - 1);
        doctorScheduleRepository.save(schedule);

        // Cập nhật trạng thái của ScheduleSlot thành chưa được đặt
        ScheduleSlot slot = appointment.getScheduleSlot();
        slot.setIsBooked(false);
        scheduleSlotRepo.save(slot);

        appointmentStatusRepository.save(status);
        return appointment;
    }

    // Check-in lịch hẹn
    @Transactional
    public Appointment CheckInAppointment(int appointmentId, int updatedByUserId) {
        Appointment appointment = appointmentRepository.findByIdWithDetails(appointmentId)
                .orElseThrow(() -> new InvalidInputException("Appointment not found with id: " + appointmentId));

        AppointmentStatus appointmentStatus = appointmentStatusRepository
                .findTopByAppointmentIdOrderByUpdateAtDesc(appointmentId)
                .orElseThrow(() -> new InvalidInputException(
                        "Appointment status not found for appointment id: " + appointmentId));
        if (appointmentStatus.getStatus() != 2 && appointment.getVisitType().equals("scheduled")) {
            throw new InvalidInputException("Cuộc hẹn phải ở trạng thái 'Đã xác nhận' để có thể check-in.");
        }

        // Đếm số lượt khám hiện tại của bác sĩ trong ca làm việc hiện tại
        Integer currentVisitNumber = appointmentRepository.countVisitNumber(
                appointment.getDoctor().getId(),
                appointment.getDoctorSchedule().getId());
        if (currentVisitNumber == null) {
            currentVisitNumber = 0;
        }
        Integer newQueueNumber = currentVisitNumber + 1;

        // Cập nhật thông tin check-in
        appointment.setVisitDateTime(LocalDateTime.now());
        appointment.setVisitNumber(newQueueNumber);
        appointmentRepository.save(appointment);

        // Tạo mới trạng thái
        AppointmentStatus status = new AppointmentStatus();
        status.setAppointment(appointment);
        status.setStatus(3); // Đang chờ khám
        status.setUpdateAt(LocalDateTime.now());

        User updatedBy = UserRepository.findById(updatedByUserId)
                .orElseThrow(() -> new InvalidInputException("User not found with id: " + updatedByUserId));
        status.setUpdate_by(updatedBy);

        appointmentStatusRepository.save(status);

        // // Lấy lý do từ trạng thái đầu tiên
        // Optional<AppointmentStatus> statusFirst = appointmentStatusRepository
        // .findTopByAppointmentIdOrderByUpdateAtAsc(appointment.getId());
        // String reason = statusFirst.map(AppointmentStatus::getReason).orElse("No
        // reason provided");
        //
        // //Tự động tạo hồ sơ bệnh án khi check-in
        // MedicalRecord record = new MedicalRecord();
        // record.setPatient(appointment.getPatient());
        // record.setDoctor(appointment.getDoctor());
        // record.setAppointment(appointment);
        // record.setInitialSymptoms(reason);
        // record.setStatus(MedicalRecordStatus.WAITING);
        // medicalRecordRepo.save(record);

        return appointment;
    }

    // Tạo lịch hẹn khám bệnh dạng Walk-in
    public ApiResponse<?> createWalkInAppointment(WalkInAppointmentRequest request, Integer createdByUserId) {

        // 1. Chuẩn bị Dữ liệu & Kiểm tra Hợp lệ
        LocalDateTime now = LocalDateTime.now();

        // Lấy lịch làm việc và kiểm tra tính sẵn có
        DoctorSchedules schedule = doctorScheduleRepository.findById(request.getDoctorScheduleId())
                .orElseThrow(() -> new InvalidInputException("Lịch làm việc không tồn tại."));

        if (schedule.getBookedPatients() >= schedule.getMaxPatients()) {
            throw new InvalidInputException("Ca làm việc đã đầy, vui lòng chọn ca khác hoặc bác sĩ khác.");
        }

        // Lấy Doctor và Patient Entities (để đảm bảo tồn tại và gán tên)
        Doctor doctor = doctorRepository.findById(request.getDoctorId())
                .orElseThrow(() -> new InvalidInputException("Bác sĩ không tồn tại."));
        Patient patient = patientRepository.findById(request.getPatientId())
                .orElseThrow(() -> new InvalidInputException("Bệnh nhân không tồn tại."));

        // 2. Cấp Số Thứ tự (Queue Number)
        // Tìm số thứ tự lớn nhất cho bác sĩ này trong ngày hôm nay
        Integer maxQueueNumber = appointmentRepository.countVisitNumber(
                request.getDoctorId(),
                request.getDoctorScheduleId()); // Nếu chưa có ai, số lớn nhất là 0
        if (maxQueueNumber == null) {
            maxQueueNumber = 0;
        }
        Integer newQueueNumber = maxQueueNumber + 1;

        // 3. Tạo Bản ghi Appointment (Visit)
        Appointment appointment = new Appointment();
        appointment.setPatient(patient);
        appointment.setDoctor(doctor);
        appointment.setDoctorSchedule(schedule);
        appointment.setVisitType("walk-in"); // WALK_IN
        appointment.setVisitDateTime(now);
        appointment.setVisitNumber(newQueueNumber); // Sử dụng QueueNumber làm VisitNumber cho mục đích này
        appointment.setPresentTime(now);

        Appointment savedAppointment = appointmentRepository.save(appointment);

        // 4. Cập nhật Bảng Liên quan

        // 4a.Cập nhật DoctorSchedules: Tăng số lượng bệnh nhân đã đặt
        schedule.setBookedPatients(schedule.getBookedPatients() + 1);
        doctorScheduleRepository.save(schedule);

        // 4b.Tự động tạo Appointment Status (Trạng thái: Chờ khám)
        AppointmentStatus initialStatus = new AppointmentStatus();
        // Tìm người tạo cuộc hẹn
        User createdBy = UserRepository.findById(createdByUserId)
                .orElseThrow(() -> new InvalidInputException("User not found with id: " + createdByUserId));

        initialStatus.setAppointment(savedAppointment);
        initialStatus.setStatus(3); // 3: Chờ khám
        initialStatus.setReason(request.getChiefComplaint()); // Lưu lý do khám
        initialStatus.setUpdateAt(now);
        initialStatus.setUpdate_by(createdBy); // Thêm người cập nhật (Nhân viên tiếp nhận)
        appointmentStatusRepository.save(initialStatus);

        logger.info("✅ Walk-in Appointment tạo thành công. ID: {}, Queue: {}",
                savedAppointment.getId(), newQueueNumber);

        // 5. Trả về Response
        return new ApiResponse<>(true, "Lich hẹn khám bệnh đã được tạo thành công.", null);
    }

    // Tạo lịch hẹn tái khám cho bệnh nhân
    @Transactional
    public ApiResponse<?> createReExamination(ReExaminationRequest request, Integer doctorUserId) {
        // 1. Kiểm tra tồn tại các thành phần
        Patient patient = patientRepository.findById(request.getPatientId())
                .orElseThrow(() -> new InvalidInputException("Bệnh nhân không tồn tại."));


        Doctor doctor = doctorRepository.findByUserId(doctorUserId);
        if (doctor == null) {
            throw new InvalidInputException("Bác sĩ không tồn tại.");
        }

        DoctorSchedules schedule = doctorScheduleRepository.findById(request.getDoctorScheduleId())
                .orElseThrow(() -> new InvalidInputException("Lịch làm việc không tồn tại."));

        ScheduleSlot slot = scheduleSlotRepository.findById(request.getScheduleSlotId())
                .orElseThrow(() -> new InvalidInputException("Khung giờ không tồn tại."));

        User doctorUser = userRepository.findById(doctorUserId)
                .orElseThrow(() -> new InvalidInputException("Người dùng (Bác sĩ) không tồn tại."));

        // 2. Kiểm tra tính sẵn sàng của lịch
        if (schedule.getBookedPatients() >= schedule.getMaxPatients()) {
            throw new InvalidInputException("Lịch làm việc đã đầy.");
        }

        if (Boolean.TRUE.equals(slot.getIsBooked())) {
            throw new InvalidInputException("Khung giờ này đã được đặt.");
        }

        // 3. Tạo Appointment
        Appointment appointment = new Appointment();
        appointment.setPatient(patient);
        appointment.setDoctor(doctor);
        appointment.setDoctorSchedule(schedule);
        appointment.setScheduleSlot(slot);
        appointment.setPresentTime(LocalDateTime.now());
        appointment.setVisitType("scheduled"); // Loại tái khám


        Appointment savedAppointment = appointmentRepository.save(appointment);

        // 4. Tạo AppointmentStatus đầu tiên (Đã xác nhận - 2)
        AppointmentStatus status = new AppointmentStatus();
        status.setAppointment(savedAppointment);
        status.setStatus(2); // Đã xác nhận
        status.setReason(request.getReason() != null ? request.getReason() : "Tái khám");
        status.setUpdateAt(LocalDateTime.now());
        status.setUpdate_by(doctorUser);
        appointmentStatusRepository.save(status);

        // 5. Cập nhật số lượng booked trong schedule
        schedule.setBookedPatients(schedule.getBookedPatients() + 1);
        doctorScheduleRepository.save(schedule);

        // 6. Cập nhật trạng thái slot
        slot.setIsBooked(true);
        scheduleSlotRepository.save(slot);

        return new ApiResponse<>(true, "Hẹn lịch tái khám thành công.", null);
    }

    /**
     * Lấy tổng quan quản lý lịch hẹn cho Admin/Lễ tân
     * Bao gồm các thống kê:
     * - Tổng số cuộc hẹn (tất cả thời gian)
     * - Áp lực phòng chờ (theo ngày): người có mặt, đang chờ, đang khám
     * - Số lượng chờ xác nhận (tất cả thời gian)
     * - Số lượng đã hủy (tất cả thời gian)
     * - Đã hoàn thành trong ngày
     * - Thống kê loại bệnh nhân: hẹn trước / vãng lai (theo ngày)
     *
     * @param currentDateStr Ngày cần thống kê (định dạng yyyy-MM-dd), null = ngày
     *                       hiện tại
     * @return AppointmentOverviewDTO chứa các thống kê tổng quan
     */
    public AppointmentOverviewDTO getAppointmentOverview(String currentDateStr) {
        // --- 1. Xác định ngày thống kê ---
        LocalDate searchDate = LocalDate.now();
        if (currentDateStr != null && !currentDateStr.isEmpty()) {
            searchDate = LocalDate.parse(currentDateStr, DateTimeFormatter.ISO_LOCAL_DATE);
        }

        // --- 2. Truy vấn dữ liệu ---

        // 2.1. Tổng số cuộc hẹn (tất cả thời gian)
        Long totalAppointments = appointmentRepository.count();

        // 2.2. Áp lực phòng chờ (theo ngày)
        // Tổng số người có mặt tại bệnh viện (đã check-in)
        Long totalPresentAtHospital = appointmentRepository.countPresentTodayByDate(searchDate);
        if (totalPresentAtHospital == null)
            totalPresentAtHospital = 0L;

        // Số người đang chờ khám (status = 3)
        Long waitingCount = appointmentRepository.countByCurrentStatusAndDate(3, searchDate);
        if (waitingCount == null)
            waitingCount = 0L;

        // Số người đang khám (status = 4)
        Long inProgressCount = appointmentRepository.countByCurrentStatusAndDate(4, searchDate);
        if (inProgressCount == null)
            inProgressCount = 0L;

        AppointmentOverviewDTO.WaitingRoomPressure waitingRoomPressure = new AppointmentOverviewDTO.WaitingRoomPressure(
                totalPresentAtHospital,
                waitingCount,
                inProgressCount);

        // 2.3. Số lượng lịch hẹn chờ xác nhận (status = 1, tất cả thời gian)
        Long pendingConfirmation = appointmentRepository.countByCurrentStatus(1);
        if (pendingConfirmation == null)
            pendingConfirmation = 0L;

        // 2.4. Số lượng lịch hẹn đã hủy (status = 6, tất cả thời gian)
        Long cancelledCount = appointmentRepository.countByCurrentStatus(6);
        if (cancelledCount == null)
            cancelledCount = 0L;

        // 2.5. Số lượng đã hoàn thành trong ngày (status = 5)
        Long completedToday = appointmentRepository.countByCurrentStatusAndDate(5, searchDate);
        if (completedToday == null)
            completedToday = 0L;

        // 2.6. Thống kê loại bệnh nhân trong ngày
        // Khách hẹn trước (visitType = 'scheduled')
        Long scheduledCount = appointmentRepository.countByVisitTypeAndDate("scheduled", searchDate);
        if (scheduledCount == null)
            scheduledCount = 0L;

        // Khách vãng lai (visitType = 'walk-in')
        Long walkInCount = appointmentRepository.countByVisitTypeAndDate("walk-in", searchDate);
        if (walkInCount == null)
            walkInCount = 0L;

        AppointmentOverviewDTO.PatientTypeToday patientTypeToday = new AppointmentOverviewDTO.PatientTypeToday(
                scheduledCount, walkInCount);

        // --- 3. Đóng gói và trả về DTO ---
        return new AppointmentOverviewDTO(
                totalAppointments,
                waitingRoomPressure,
                pendingConfirmation,
                cancelledCount,
                completedToday,
                patientTypeToday);
    }

    // Lấy danh sách tất cả các lịch hẹn của một bệnh nhân cụ thể.
    public List<AppointmentDTO> getAppointmentsByPatient(int patientId) {
        return appointmentRepository.findByPatientId(patientId)
                .stream()
                .sorted(Comparator.comparing(
                        appointment -> appointment.getDoctorSchedule(),
                        Comparator.comparing(DoctorSchedules::getDate).reversed() // Sắp xếp theo date giảm dần
                ))
                .map(this::covertToResponse)
                .collect(Collectors.toList());
    }

    public AppointmentDTO getAppointmentDetails(int appointmentId) {
        Appointment appointment = appointmentRepository.findById(appointmentId).orElseThrow(
                () -> new InvalidInputException("Appointment not found with id: " + appointmentId));
        return covertToResponse(appointment);
    }

    // Lấy tất cả các lịch hẹn thuộc một ca làm việc cụ thể của bác sĩ.
    public List<AppointmentDTO> getAppointmentsByDoctorSchedule(int doctorScheduleId) {
        return appointmentRepository.findByDoctorScheduleId(doctorScheduleId)
                .stream()
                .map(this::covertToResponse)
                .collect(Collectors.toList());
    }

    // Lấy danh sách các lịch hẹn theo trạng thái hiện tại
    public List<AppointmentDTO> getAppointmentsByStatus(int status) {
        return appointmentRepository.findByStatus(status)
                .stream()
                .map(this::covertToResponse)
                .collect(Collectors.toList());
    }

    // Lấy tất cả các lịch hẹn trong một tuần tính từ startDate
    public List<AppointmentDTO> getAppointmentsByWeek(LocalDate startDate) {
        LocalDate endDate = startDate.plusDays(6);
        return appointmentRepository.findByAppointmentDateBetween(startDate, endDate)
                .stream()
                .map(this::covertToResponse)
                .collect(Collectors.toList());
    }

    // Lấy danh sách lịch hẹn của một bác sĩ cụ thể, không giới hạn thời gian.
    public List<QueueResponse> getAppointmentsByDoctor(LocalDate fromDate) {
        // Lấy id doctor từ user đang đăng nhập
        var auth = SecurityContextHolder.getContext().getAuthentication();
        if (!(auth.getPrincipal() instanceof CustomUserDetails cud)) {
            throw new AccessDeniedException("Unauthorized");
        }
        Integer doctorId = doctorRepository.findIdByUserId(cud.getId());

        return appointmentRepository
                .findByDoctorIdAndDoctorSchedule_DateEqualsOrderByScheduleSlotAsc(doctorId, fromDate)
                .stream()
                .map(this::covertToQueueResponse)
                .collect(Collectors.toList());
    }

    // lấy danh sách lịch hẹn với các tiêu chí tìm kiếm và phân trang
    public PaginatedResponseDTO<AppointmentDTO> searchAppointments(AppointmentSearchRequest req) {
        // Parse sort
        // Pageable pageable = PageRequest.of(
        // Optional.ofNullable(req.getPage()).orElse(0),
        // Optional.ofNullable(req.getSize()).orElse(10),
        // Sort.by(Sort.Direction.DESC, "presentTime")
        // );
        Sort sort = Sort.by(Sort.Direction.fromString(req.getSortDir()), req.getSortBy());
        Pageable pageable = PageRequest.of(req.getPage(), req.getSize(), sort);

        Specification<Appointment> spec = buildSearchAppointmentSpec(
                req.getKeyword(),
                Optional.ofNullable(req.getStatus()).orElse(0),
                req.getDepartmentId(),
                req.getFromDate());

        Page<Appointment> page = appointmentRepository.findAll(spec, pageable);

        List<AppointmentDTO> data = page.getContent().stream()
                .map(this::covertToResponse)
                .collect(Collectors.toList());

        // return new PageImpl<>(data, pageable, page.getTotalElements());
        return new PaginatedResponseDTO<AppointmentDTO>(
                page.getNumber(),
                page.getSize(),
                page.getTotalElements(),
                page.getTotalPages(),
                data);
    }

    // lấy danh sách hàng đợi khám bệnh với các tiêu chí tìm kiếm và phân trang
    public PaginatedResponseDTO<QueueResponse> getQueueAppointments(QueueSearchRequest request) {
        // Chuẩn bị phân trang và sắp xếp
        Sort sort = Sort.by(Sort.Direction.fromString(request.getSortDir()), request.getSortBy());
        Pageable pageable = PageRequest.of(request.getPage(), request.getSize(), sort);

        // Xây dựng Specification
        Specification<Appointment> spec = filterQueue(
                request.getKeyword(),
                request.getSpecialtyId(),
                Optional.ofNullable(request.getStatus()).orElse(0),
                request.getFindDate(),
                request.getPatientType());

        // Thực hiện truy vấn với phân trang
        Page<Appointment> queuepage = appointmentRepository.findAll(spec, pageable);

        // Chuyển đổi kết quả sang DTO
        List<QueueResponse> responseQueue = queuepage.getContent().stream()
                .map(this::covertToQueueResponse)
                .collect(Collectors.toList());

        // Trả về Paginated Response
        return new PaginatedResponseDTO<QueueResponse>(
                queuepage.getNumber(),
                queuepage.getSize(),
                queuepage.getTotalElements(),
                queuepage.getTotalPages(),
                responseQueue);
    }

    // Chuyển đổi entity Appointment sang DTO AppointmentDTO
    private AppointmentDTO covertToResponse(Appointment appointment) {
        AppointmentDTO dto = new AppointmentDTO();
        // String appointmentTime = appointment.getScheduleSlot().getStartTime() + " - "
        // + appointment.getScheduleSlot().getEndTime();
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
        dto.setDoctorId(appointment.getDoctor().getId());
        dto.setDoctorName(appointment.getDoctor().getStaff().getUser().getFullname());
        dto.setDoctorSpecialty(appointment.getDoctor().getSpecialty().getDepartment().getName());
        dto.setPatientId(appointment.getPatient().getId());
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
        dto.setAppointmentType(appointment.getVisitType());
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
                dto.setStatus("Chờ Khám");
                dto.setStatusId(statusId);
            }
            case 4 -> {
                dto.setStatus("Đang Khám");
                dto.setStatusId(statusId);
            }
            case 5 -> {
                dto.setStatus("Hoàn thành");
                dto.setStatusId(statusId);
            }
            case 6 -> {
                dto.setStatus("Hủy");
                dto.setStatusId(statusId);
            }
            default -> {
                dto.setStatus("Không xác định");
                dto.setStatusId(0);
            }
        }
        ;

        // Reason của riêng trạng thái "Chờ xác nhận" đối với scheduled và "Chờ khám"
        // đối với walk-in
        AppointmentStatus latestStatus;
        if (appointment.getVisitType().equals("scheduled")) {
            latestStatus = appointmentStatusRepository
                    .findByAppointmentIdAndStatus(appointment.getId(), 1).orElse(null);
        } else {
            latestStatus = appointmentStatusRepository
                    .findByAppointmentIdAndStatus(appointment.getId(), 3).orElse(null);
        }

        if (latestStatus != null)
            dto.setReason(latestStatus.getReason());
        else
            dto.setReason("");

        // danh sách lịch sử trạng thái để hiển thị rõ ràng
        var history = appointmentStatusRepository
                .findByAppointmentIdOrderByUpdateAtDesc(appointment.getId())
                .stream()
                .map(this::mapStatusToHistoryItem)
                .toList();
        dto.setStatusHistory(history);

        // Tính tổng hóa đơn của cuộc hẹn (qua MedicalRecord -> Payment)
        dto.setTotalPrice(paymentRepository.sumTotalPaymentByAppointmentId(appointment.getId()));
        dto.setInsurancePrice(paymentRepository.sumInsurancePaymentByAppointmentId(appointment.getId()));
        dto.setPatientPrice(paymentRepository.sumPatientPaymentByAppointmentId(appointment.getId()));

        return dto;
    }

    private StatusHistoryItemDTO mapStatusToHistoryItem(AppointmentStatus st) {
        String statusName = switch (st.getStatus()) {
            case 1 -> "Chờ xác nhận";
            case 2 -> "Đã xác nhận";
            case 3 -> "Chờ Khám";
            case 4 -> "Đang Khám";
            case 5 -> "Hoàn thành";
            case 6 -> "Hủy";
            default -> "Không xác định";
        };
        Integer updatedById = (st.getUpdate_by() != null) ? st.getUpdate_by().getId() : null;
        String updatedByRole = (st.getUpdate_by() != null)
                ? roleName(st.getUpdate_by().getId(), st.getUpdate_by().getRole())
                : null;
        String updatedByName = (st.getUpdate_by() != null)
                ? st.getUpdate_by().getFullname()
                : "System";

        return new StatusHistoryItemDTO(
                st.getStatus(),
                statusName,
                st.getReason(),
                st.getUpdateAt(),
                updatedById,
                updatedByName,
                updatedByRole);
    }

    private String roleName(Integer userId, Integer role) {
        if (role == null)
            return null;
        return switch (role) {
            case 0 -> "Admin";
            case 1 -> "Patient";
            case 2 -> {
                Staff staff = StaffRepo.findByUserId(userId)
                        .orElseThrow(() -> new InvalidInputException("Staff not found for userId: " + userId));
                if (staff.getStaff_position().getId() == 1) {
                    yield "Doctor";
                } else if (staff.getStaff_position().getId() == 7) {
                    yield "Receptionist";
                }  else if (staff.getStaff_position().getId() == 6) {
                    yield "Cashier";
                } else {
                    yield "Staff";
                }
            }
            default -> "Unknown";
        };
    }

    // Chuyển đổi entity Appointment sang DTO QueueDTO
    private QueueResponse covertToQueueResponse(Appointment appointment) {
        QueueResponse dto = new QueueResponse();
        // String appointmentTime = appointment.getScheduleSlot().getStartTime() + " - "
        // + appointment.getScheduleSlot().getEndTime();
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
            appointmentTime = "Không có khung giờ";
        }
        dto.setAppointmentId(appointment.getId());
        dto.setAppointmentCode(appointment.getCode());

        dto.setDoctorId(appointment.getDoctor().getId());
        dto.setDoctorName(appointment.getDoctor().getStaff().getUser().getFullname());
        dto.setDoctorSpecialty(appointment.getDoctor().getSpecialty().getName());

        dto.setPatientId(appointment.getPatient().getId());
        dto.setPatientName(appointment.getPatient().getUser().getFullname());
        dto.setPatientYearOfBirth(appointment.getPatient().getUser().getDateOfBirth());
        dto.setPatientGender(appointment.getPatient().getUser().getGender() == 1 ? "Nữ" : "Nam");
        dto.setPatientAge(appointment.getPatient().getUser().getAge());
        dto.setPatientPhone(appointment.getPatient().getUser().getPhoneNumber());

        dto.setRoomName(appointment.getDoctorSchedule().getRoom().getName());
        dto.setAppointmentTime(appointmentTime);
        dto.setPatientType(appointment.getVisitType());
        if (appointment.getVisitDateTime() != null && appointment.getVisitNumber() != null) {
            dto.setVisitDateTime(appointment.getVisitDateTime());
            dto.setVisitNumber(appointment.getVisitNumber());
        }
        dto.setShift(appointment.getDoctorSchedule().getShiftType().getName_type());

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
                dto.setStatus("Chờ Khám");
                dto.setStatusId(statusId);
            }
            case 4 -> {
                dto.setStatus("Đang Khám");
                dto.setStatusId(statusId);
            }
            case 5 -> {
                dto.setStatus("Hoàn thành");
                dto.setStatusId(statusId);
            }
            case 6 -> {
                dto.setStatus("Hủy");
                dto.setStatusId(statusId);
            }
            default -> {
                dto.setStatus("Không xác định");
                dto.setStatusId(0);
            }
        }
        ;

        // Reason của riêng trạng thái "Chờ xác nhận" đối với scheduled và "Chờ khám"
        // đối với walk-in
        AppointmentStatus latestStatus;
        if (appointment.getVisitType().equals("scheduled")) {
            latestStatus = appointmentStatusRepository
                    .findByAppointmentIdAndStatus(appointment.getId(), 1).orElse(null);
        } else {
            latestStatus = appointmentStatusRepository
                    .findByAppointmentIdAndStatus(appointment.getId(), 3).orElse(null);
        }

        if (latestStatus != null)
            dto.setReason(latestStatus.getReason());
        else
            dto.setReason("");

        return dto;
    }
}
