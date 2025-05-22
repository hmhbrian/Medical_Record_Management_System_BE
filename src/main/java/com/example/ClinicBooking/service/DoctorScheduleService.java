package com.example.ClinicBooking.service;

import com.example.ClinicBooking.DTO.DoctorScheduleRequest;
import com.example.ClinicBooking.DTO.DoctorScheduleResponse;
import com.example.ClinicBooking.entity.Doctor;
import com.example.ClinicBooking.entity.DoctorSchedules;
import com.example.ClinicBooking.repository.DoctorRepository;
import com.example.ClinicBooking.repository.DoctorSchedulesRepository;
import com.example.ClinicBooking.repository.ShiftTypeRepository;
import com.example.ClinicBooking.repository.roomRepository;
import org.springframework.beans.factory.annotation.Autowired;
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

    public DoctorSchedules assignSchedule(DoctorScheduleRequest request) {
        // Kiểm tra bác sĩ đã có lịch làm việc trong cùng ca chưa
        boolean doctorConflict = scheduleRepo.existsByDoctorAndDateAndShiftType(
                doctorRepo.findById(request.getDoctorId()).orElseThrow(() -> new RuntimeException("Không tìm thấy bác sĩ")),
                request.getDate(),
                shiftTypeRepo.findById(request.getShiftTypeId()).orElseThrow(() -> new RuntimeException("Không tìm thấy ca làm việc"))
        );
        if (doctorConflict) {
            throw new RuntimeException("Bác sĩ đã có lịch làm việc vào ca này.");
        }

        // Kiểm tra phòng đã được phân cho bác sĩ khác trong ca này chưa
        boolean roomConflict = scheduleRepo.existsByRoomAndDateAndShiftType(
                roomRepo.findById(request.getRoomId()).orElseThrow(() -> new RuntimeException("Không tìm thấy phòng")),
                request.getDate(),
                shiftTypeRepo.findById(request.getShiftTypeId()).orElseThrow(() -> new RuntimeException("Không tìm thấy ca làm việc"))
        );
        if (roomConflict) {
            throw new RuntimeException("Phòng này đã được phân cho bác sĩ khác trong ca này.");
        }

        DoctorSchedules schedule = new DoctorSchedules();
        schedule.setDoctor(doctorRepo.findById(request.getDoctorId())
                .orElseThrow(() -> new RuntimeException("Không tìm thấy bác sĩ")));
        schedule.setShiftType(shiftTypeRepo.findById(request.getShiftTypeId())
                .orElseThrow(() -> new RuntimeException("Không tìm thấy ca làm việc")));
        schedule.setRoom(roomRepo.findById(request.getRoomId())
                .orElseThrow(() -> new RuntimeException("Không tìm thấy phòng")));
        schedule.setDate(request.getDate());
        schedule.setStatus(request.getStatus());
        schedule.setMaxPatients(request.getMaxPatients());

        return scheduleRepo.save(schedule);
    }

    public List<DoctorScheduleResponse> getSchedulesBySpecialty(int specialtyId) {
        LocalDate today = LocalDate.now();
        List<Doctor> doctors = doctorRepo.findBySpecialtyId(specialtyId);
        if (doctors.isEmpty()) {
            throw new RuntimeException("No doctors found for this specialty");
        }

        // Lấy tất cả lịch làm việc của các bác sĩ trong chuyên khoa
        return doctors.stream()
                .flatMap(doctor -> scheduleRepo.findByDoctorIdAndDateAfterOrderByDateAsc(doctor.getId(),today).stream())
                .map(this::convertToResponse)
                .collect(Collectors.toList());
    }

    public List<DoctorScheduleResponse> getScheduleByDoctorId(int doctorId) {
        LocalDate today = LocalDate.now();
        List<DoctorSchedules> schedules = scheduleRepo.findByDoctorIdAndDateAfterOrderByDateAsc(doctorId,today);
        return schedules.stream().map(this::convertToResponse).collect(Collectors.toList());
    }

    public List<DoctorScheduleResponse> getSchedulesByDoctorAndWeek(int doctorId, LocalDate startDate) {
        LocalDate endDate = startDate.plusDays(6);

        // Lấy lịch làm việc trong khoảng thời gian
        List<DoctorSchedules> schedules = scheduleRepo.findByDoctorIdAndDateBetween(doctorId, startDate, endDate);
        return schedules.stream()
                .map(this::convertToResponse)
                .collect(Collectors.toList());
    }

    private DoctorScheduleResponse convertToResponse(DoctorSchedules schedule) {
        DoctorScheduleResponse response = new DoctorScheduleResponse();
        response.setId(schedule.getId());
        response.setDate(schedule.getDate());
        response.setStatus(schedule.getStatus());
        response.setShift(schedule.getShiftType().getName_type());
        response.setLocation(schedule.getRoom().getName());
        response.setMaxPatients(schedule.getMaxPatients());
        response.setBookedPatients(schedule.getBookedPatients());
        response.setStart_time(schedule.getShiftType().getStart_time());
        response.setEnd_time(schedule.getShiftType().getEnd_time());
        return response;
    }

}
