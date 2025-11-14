package com.example.clinicbooking.controller;

import com.example.clinicbooking.DTO.ApiResponse;
import com.example.clinicbooking.DTO.Appointment.*;
import com.example.clinicbooking.DTO.Notification.NotificationRequest;
import com.example.clinicbooking.DTO.PaginatedResponseDTO;
import com.example.clinicbooking.entity.Appointment;
import com.example.clinicbooking.security.CustomUserDetails;
import com.example.clinicbooking.service.Appointment.AppointmentService;
import com.example.clinicbooking.service.FCMService;
import com.google.firebase.messaging.FirebaseMessagingException;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/appointments")
@Tag(name = "Appointment", description = "Quản lý lịch hẹn khám bệnh(1: Chờ xác nhận, 2: Đã xác nhận, 3:Chờ khám, 4:Đang khám, 5:Hoàn thành, 6: Hủy.)")
@RequiredArgsConstructor
public class AppointmentController {
    private final AppointmentService appointmentService;
    private final FCMService fcmService;

    //Lấy danh sách lịch hẹn với phân trang và lọc
    @GetMapping("/find-all")
    public ResponseEntity<ApiResponse<Page<AppointmentDTO>>> searchAppointments(
            @RequestParam(required = false) String keyword,
            @RequestParam(required = false) Integer status,
            @RequestParam(required = false) Integer departmentId,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fromDate,
            @RequestParam(defaultValue = "0") Integer page,
            @RequestParam(defaultValue = "10") Integer size
    ) {
        AppointmentSearchRequest req = new AppointmentSearchRequest();
        req.setKeyword(keyword);
        req.setStatus(status);
        req.setDepartmentId(departmentId);
        req.setFromDate(fromDate);
        req.setPage(page);
        req.setSize(size);

        //return ResponseEntity.ok(appointmentService.searchAppointments(req));
        Page<AppointmentDTO> result = appointmentService.searchAppointments(req);
        return ResponseEntity.ok(new ApiResponse<>(true, "Lấy danh sách lịch hẹn thành công!", result));

    }

    @GetMapping("/queues")
    public ResponseEntity<ApiResponse<PaginatedResponseDTO<QueueResponse>>> searchQueues(
            @RequestParam(required = false) String keyword,
            @RequestParam(required = false) Integer status,
            @RequestParam(required = false) Integer specialtyId,
            @RequestParam(required = false) String patientType,
            @RequestParam(required = false) String fromDate,
            @RequestParam(defaultValue = "0") Integer page,
            @RequestParam(defaultValue = "10") Integer size
    ) {
        QueueSearchRequest req = new QueueSearchRequest();
        req.setKeyword(keyword);
        req.setStatus(status);
        req.setSpecialtyId(specialtyId);
        req.setFindDate(fromDate);
        req.setPatientType(patientType);
        req.setPage(page);
        req.setSize(size);

        //return ResponseEntity.ok(appointmentService.searchAppointments(req));
        PaginatedResponseDTO<QueueResponse> result = appointmentService.getQueueAppointments(req);
        return ResponseEntity.ok(new ApiResponse<>(true, "Lấy danh sách chờ thành công!", result));

    }

    // Đặt lịch hẹn mới(patient)
    @PostMapping
    public ResponseEntity<AppointmentDTO> bookAppointment(@RequestBody AppointmentRequest request) {
        return ResponseEntity.ok(appointmentService.bookAppointment(request));
    }

    // Lấy danh sách lịch hẹn của bệnh nhân
    @GetMapping("/patient/{patientId}")
    public ResponseEntity<List<AppointmentDTO>> getAppointments(@PathVariable int patientId) {
        return ResponseEntity.ok(appointmentService.getAppointmentsByPatient(patientId));
    }

    // Lấy danh sách lịch hẹn của bác sĩ theo lịch làm việc
    @GetMapping("/schedule/{doctorScheduleId}")
    public ResponseEntity<List<AppointmentDTO>> getAppointmentsBySchedule(@PathVariable int doctorScheduleId) {
        return ResponseEntity.ok(appointmentService.getAppointmentsByDoctorSchedule(doctorScheduleId));
    }

    // Xác nhận lịch hẹn (doctor)
    @PutMapping("/{appointmentId}/confirm")
    public ResponseEntity<ApiResponse<?>> ConfirmAppointment(@PathVariable int appointmentId, @RequestBody int UpdatedByUserId) {
        // Xác nhận lịch hẹn
        Appointment appointment = appointmentService.ConfirmAppointment(
                appointmentId,
                UpdatedByUserId
        );

        //Chuẩn bị request thông báo
        NotificationRequest request = new NotificationRequest();
        request.setUserId(appointment.getPatient().getUser().getId());
        request.setTitle("Lịch hẹn của bạn đã được xác nhận!");
        request.setBody("Bác sĩ " + appointment.getDoctor().getStaff().getUser().getFullname() + " đã chấp nhận lịch hẹn.");

        Map<String, String> data = new HashMap<>();
        data.put("type", "APPOINTMENT_CONFIRMED");
        data.put("id",String.valueOf(appointmentId));
        request.setData(data);
        request.setSentBy(appointment.getDoctor().getStaff().getUser().getFullname());

        //Gửi thông báo (Gửi vào queue hoặc chạy async nếu cần)
        try {
            fcmService.sendAppointmentConfirmation(request);
        } catch (FirebaseMessagingException e) {
            System.err.println("Failed to send notification: " + e.getMessage());
        }

        return ResponseEntity.ok(new ApiResponse<>(true, "Xác nhận lịch hẹn thành công!", null));
    }

    // Hủy lịch hẹn (doctor)
    @PutMapping("/{appointmentId}/cancel")
    public ResponseEntity<ApiResponse<?>> CancelAppointment(@PathVariable int appointmentId, @RequestBody AppointmentStatusUpdateRequest rq) {
        Appointment appointment = appointmentService.DeleteAppointment(
                appointmentId,
                rq.getUpdatedByUserId(),
                rq.getReason()
        );

        //Chuẩn bị request thông báo
        NotificationRequest request = new NotificationRequest();
        request.setUserId(appointment.getPatient().getUser().getId());
        request.setTitle("Lịch hẹn của bạn đã bị hủy!");
        request.setBody("Bác sĩ " + appointment.getDoctor().getStaff().getUser().getFullname() + " đã từ chối lịch hẹn.");

        Map<String, String> data = new HashMap<>();
        data.put("type", "APPOINTMENT_CANCELED");
        data.put("id",String.valueOf(appointmentId));
        request.setData(data);
        request.setSentBy(appointment.getDoctor().getStaff().getUser().getFullname());

        //Gửi thông báo (Gửi vào queue hoặc chạy async nếu cần)
        try {
            fcmService.sendAppointmentConfirmation(request);
        } catch (FirebaseMessagingException e) {
            System.err.println("Failed to send notification: " + e.getMessage());
        }

        return ResponseEntity.ok(new ApiResponse<>(true, "Hủy lịch hẹn thành công!", null));
    }

    // Hủy lịch hẹn (patient)
    @PutMapping("/{appointmentId}/patient-cancel")
    public ResponseEntity<ApiResponse<?>> CancelAppointment_Patient(@PathVariable int appointmentId, @RequestBody AppointmentStatusUpdateRequest rq) {
        Appointment appointment = appointmentService.DeleteAppointment(
                appointmentId,
                rq.getUpdatedByUserId(),
                rq.getReason()
        );

        return ResponseEntity.ok(new ApiResponse<>(true, "Hủy lịch hẹn thành công!", null));
    }

    // Check-in lịch hẹn (receptionist)
    @PutMapping("/{appointmentId}/check-in")
    public ResponseEntity<ApiResponse<?>> CheckInAppointment(@PathVariable int appointmentId) {
        var auth = SecurityContextHolder.getContext().getAuthentication();
        if (!(auth.getPrincipal() instanceof CustomUserDetails cud)) {
            throw new AccessDeniedException("Unauthorized");
        }
        Integer currentUserId = cud.getId();

        // Check-in lịch hẹn
        Appointment appointment = appointmentService.CheckInAppointment(
                appointmentId,
                currentUserId
        );

        return ResponseEntity.ok(new ApiResponse<>(true, "Check-in lịch hẹn thành công!", null));
    }

    @PostMapping("/walk-in")
    public ResponseEntity<ApiResponse<?>> CreateWalkInAppointment(@RequestBody WalkInAppointmentRequest request) {
        var auth = SecurityContextHolder.getContext().getAuthentication();
        if (!(auth.getPrincipal() instanceof CustomUserDetails cud)) {
            throw new AccessDeniedException("Unauthorized");
        }
        Integer currentUserId = cud.getId();

        //Tạo lịch khám mới cho bệnh nhân đến khám không hẹn trước (walk-in) và check-in
        return ResponseEntity.ok(appointmentService.createWalkInAppointment(request, currentUserId));
    }

    // Lấy danh sách lịch hẹn theo trạng thái
    @GetMapping("/status/{status}")
    public ResponseEntity<List<AppointmentDTO>> getAppointmentsByStatus(@PathVariable int status) {
        return ResponseEntity.ok(appointmentService.getAppointmentsByStatus(status));
    }

    // Lấy danh sách lịch hẹn trong tuần bắt đầu từ ngày cho trước
    @GetMapping("/week")
    public ResponseEntity<List<AppointmentDTO>> getAppointmentsByWeek(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate) {
        return ResponseEntity.ok(appointmentService.getAppointmentsByWeek(startDate));
    }

    // Lấy danh sách lịch hẹn của bác sĩ trong ngày
    @GetMapping("/doctor")
    public ResponseEntity<List<QueueDoctorResponse>> getAppointmentsByDoctor(String FindDate) {
        LocalDate Date = LocalDate.parse(FindDate, DateTimeFormatter.ISO_LOCAL_DATE);
        return ResponseEntity.ok(appointmentService.getAppointmentsByDoctor(Date));
    }
}
