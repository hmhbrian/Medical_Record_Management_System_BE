package com.example.ClinicBooking.service;

import com.example.ClinicBooking.DTO.DoctorScheduleRequest;
import com.example.ClinicBooking.DTO.DoctorScheduleResponse;
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

    public List<DoctorScheduleResponse> getScheduleByDoctorId(int doctorId) {
        List<DoctorSchedules> schedules = scheduleRepo.findByDoctorIdOrderByDateAsc(doctorId);
        return schedules.stream().map(schedule -> {
            DoctorScheduleResponse dto = new DoctorScheduleResponse();
            dto.setDate(schedule.getDate());
            dto.setStatus(schedule.getStatus());
            dto.setShiftName(schedule.getShiftType().getName_type());
            dto.setRoomName(schedule.getRoom().getName());
            dto.setMaxPatients(schedule.getMaxPatients());
            dto.setBookedPatients(schedule.getBookedPatients());
            dto.setStart_time(schedule.getShiftType().getStart_time());
            dto.setEnd_time(schedule.getShiftType().getEnd_time());
            return dto;
        }).collect(Collectors.toList());
    }

}
