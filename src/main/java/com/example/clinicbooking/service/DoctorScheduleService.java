package com.example.clinicbooking.service;

import com.example.clinicbooking.DTO.Doctor.DoctorScheduleRequest;
import com.example.clinicbooking.DTO.Doctor.DoctorScheduleResponse;
import com.example.clinicbooking.DTO.Doctor.DrScheduleSummaryRp;
import com.example.clinicbooking.DTO.PatientInScheduleResponse;
import com.example.clinicbooking.entity.Doctor;
import com.example.clinicbooking.entity.DoctorSchedules;
import com.example.clinicbooking.exceptions.InvalidInputException;
import com.example.clinicbooking.repository.*;
import com.example.clinicbooking.security.CustomUserDetails;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class DoctorScheduleService {
    @Autowired
    private DoctorSchedulesRepository scheduleRepo;
    @Autowired
    private ShiftTypeRepository shiftTypeRepo;
    @Autowired
    private DoctorRepository doctorRepo;
    @Autowired
    private roomRepository roomRepo;
    @Autowired
    private  AppointmentRepository appointmentRepo;

    public DoctorSchedules assignSchedule(DoctorScheduleRequest request) {
        // Kiểm tra bác sĩ đã có lịch làm việc trong cùng ca chưa
        boolean doctorConflict = scheduleRepo.existsByDoctorAndDateAndShiftType(
                doctorRepo.findById(request.getDoctorId()).orElseThrow(() -> new InvalidInputException("Không tìm thấy bác sĩ")),
                request.getDate(),
                shiftTypeRepo.findById(request.getShiftTypeId()).orElseThrow(() -> new InvalidInputException("Không tìm thấy ca làm việc"))
        );
        if (doctorConflict) {
            throw new InvalidInputException("Bác sĩ đã có lịch làm việc vào ca này.");
        }

        // Kiểm tra phòng đã được phân cho bác sĩ khác trong ca này chưa
        boolean roomConflict = scheduleRepo.existsByRoomAndDateAndShiftType(
                roomRepo.findById(request.getRoomId()).orElseThrow(() -> new InvalidInputException("Không tìm thấy phòng")),
                request.getDate(),
                shiftTypeRepo.findById(request.getShiftTypeId()).orElseThrow(() -> new InvalidInputException("Không tìm thấy ca làm việc"))
        );
        if (roomConflict) {
            throw new InvalidInputException("Phòng này đã được phân cho bác sĩ khác trong ca này.");
        }

        DoctorSchedules schedule = new DoctorSchedules();
        schedule.setDoctor(doctorRepo.findById(request.getDoctorId())
                .orElseThrow(() -> new InvalidInputException("Không tìm thấy bác sĩ")));
        schedule.setShiftType(shiftTypeRepo.findById(request.getShiftTypeId())
                .orElseThrow(() -> new InvalidInputException("Không tìm thấy ca làm việc")));
        schedule.setRoom(roomRepo.findById(request.getRoomId())
                .orElseThrow(() -> new InvalidInputException("Không tìm thấy phòng")));
        schedule.setDate(request.getDate());
        schedule.setStatus(request.getStatus());
        schedule.setMaxPatients(request.getMaxPatients());

        return scheduleRepo.save(schedule);
    }

    public List<DoctorScheduleResponse> getSchedulesBySpecialty(int specialtyId) {
        LocalDate today = LocalDate.now();
        List<Doctor> doctors = doctorRepo.findBySpecialtyId(specialtyId);
        if (doctors.isEmpty()) {
            throw new InvalidInputException("No doctors found for this specialty");
        }

        // Lấy tất cả lịch làm việc của các bác sĩ trong chuyên khoa
        return doctors.stream()
                .flatMap(doctor -> scheduleRepo.findByDoctorIdAndDateAfterOrderByDateAsc(doctor.getId(),today).stream())
                .map(this::convertToResponse)
                .collect(Collectors.toList());
    }

    public DrScheduleSummaryRp getSchedulesByDoctorAndWeek(int doctorId, LocalDate startDate) {
        LocalDate endDate = startDate.plusDays(6);

        // Lấy lịch làm việc trong khoảng thời gian
        List<DoctorSchedules> schedules = scheduleRepo.findByDoctorIdAndDateBetweenOrderByShiftTypeIdAsc(doctorId, startDate, endDate);
        List<DoctorScheduleResponse> responses = schedules.stream()
                                                .map(this::convertToResponse)
                                                .collect(Collectors.toList());

        // Tính toán số liệu tổng
        int numberSchedules = responses.size();
        int numberPatients = responses.stream().mapToInt(DoctorScheduleResponse::getBookedPatients).sum();
        int totalCapacity = responses.stream().mapToInt(DoctorScheduleResponse::getMaxPatients).sum();
        double usageRate = totalCapacity > 0 ? (double) numberPatients / totalCapacity : 0.0;

        DrScheduleSummaryRp summary = new DrScheduleSummaryRp();
        summary.setNumber_schedules(numberSchedules);
        summary.setNumber_patients(numberPatients);
        summary.setTotal_capacity(totalCapacity);
        summary.setUsage_rate(usageRate);
        summary.setSchedules(responses);

        return summary;
    }

    public DrScheduleSummaryRp getSchedulesOfDoctorAndWeek(LocalDate startDate) {
        LocalDate endDate = startDate.plusDays(6);

        //Lấy id doctor từ user đang đăng nhập
        var auth = SecurityContextHolder.getContext().getAuthentication();
        if (!(auth.getPrincipal() instanceof CustomUserDetails cud)) {
            throw new AccessDeniedException("Unauthorized");
        }
        Integer doctorId = doctorRepo.findIdByUserId(cud.getId());

        // Lấy lịch làm việc trong khoảng thời gian
        List<DoctorSchedules> schedules = scheduleRepo.findByDoctorIdAndDateBetweenOrderByShiftTypeIdAsc(doctorId, startDate, endDate);
        List<DoctorScheduleResponse> responses = schedules.stream()
                .map(this::convertToResponse)
                .collect(Collectors.toList());

        // Tính toán số liệu tổng
        int numberSchedules = responses.size();
        int numberPatients = responses.stream().mapToInt(DoctorScheduleResponse::getBookedPatients).sum();
        int totalCapacity = responses.stream().mapToInt(DoctorScheduleResponse::getMaxPatients).sum();
        double usageRate = totalCapacity > 0 ? (double) numberPatients / totalCapacity : 0.0;

        DrScheduleSummaryRp summary = new DrScheduleSummaryRp();
        summary.setNumber_schedules(numberSchedules);
        summary.setNumber_patients(numberPatients);
        summary.setTotal_capacity(totalCapacity);
        summary.setUsage_rate(usageRate);
        summary.setSchedules(responses);

        return summary;
    }

    @Transactional
    public List<PatientInScheduleResponse> GetPatientOfDoctorSchedule(int scheduleId){
        // Lấy id user đang đăng nhập (đã có CustomUserDetails)
        var auth = SecurityContextHolder.getContext().getAuthentication();
        if (!(auth.getPrincipal() instanceof CustomUserDetails cud)) {
            throw new AccessDeniedException("Unauthorized");
        }
        Integer doctorid = doctorRepo.findIdByUserId(cud.getId());
        if (doctorid == null) {
            throw new InvalidInputException("Không tìm thấy bác sĩ. Vui lòng kiểm tra lại thông tin đăng nhập.");
        }
        boolean owned = scheduleRepo.existsByIdAndDoctorId(scheduleId, doctorid);
        if (!owned) {
            throw new InvalidInputException("Bạn không có quyền xem lịch này");
        }

        return appointmentRepo.findPatientByDoctorScheduleId(scheduleId);
    }

    public void deleteSchedule(int scheduleId) {
        if(!scheduleRepo.findById(scheduleId).isPresent()) {
            throw new InvalidInputException("Không tìm thấy lịch làm việc");
        }
        scheduleRepo.deleteById(scheduleId);
    }

//    public List<DoctorScheduleResponse> getScheduleByDoctorId(int doctorId) {
//        List<DoctorSchedules> schedules = scheduleRepo.findByDoctorIdAndDateAfterOrderByDateAsc(doctorId, LocalDate.now());
//        return schedules.stream()
//                .map(this::convertToResponse)
//                .collect(Collectors.toList());
//    }

    private DoctorScheduleResponse convertToResponse(DoctorSchedules schedule) {
        DoctorScheduleResponse response = new DoctorScheduleResponse();
        response.setId(schedule.getId());
        response.setShift(schedule.getShiftType().getName_type());
        response.setLocation(schedule.getRoom().getName());
        response.setMaxPatients(schedule.getMaxPatients());
        response.setBookedPatients(schedule.getBookedPatients());
        response.setStart_time(schedule.getShiftType().getStart_time());
        response.setEnd_time(schedule.getShiftType().getEnd_time());
        return response;
    }

}
