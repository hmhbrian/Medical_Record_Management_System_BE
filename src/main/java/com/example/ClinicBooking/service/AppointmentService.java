package com.example.ClinicBooking.service;

import com.example.ClinicBooking.DTO.AppointmentDTO;
import com.example.ClinicBooking.DTO.AppointmentRequest;
import com.example.ClinicBooking.DTO.DoctorResponse;
import com.example.ClinicBooking.entity.*;
import com.example.ClinicBooking.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
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
    private final DoctorRepository doctorRepository;

    public AppointmentDTO bookAppointment(AppointmentRequest request) {
        Patient patient = patientRepository.findById(request.getPatientId())
                .orElseThrow(() -> new RuntimeException("Patient not found"));
        DoctorSchedules schedule = doctorScheduleRepository.findById(request.getDoctorScheduleId())
                .orElseThrow(() -> new RuntimeException("Schedule not found"));
        Doctor doctor = doctorRepository.findById(request.getDoctorId())
                .orElseThrow(() -> new RuntimeException("Doctor not found"));

        if (schedule.getBookedPatients() >= schedule.getMaxPatients()) {
            throw new RuntimeException("No available slots");
        }

        Appointment appointment = new Appointment();
        appointment.setPatient(patient);
        appointment.setDoctor(doctor);
        appointment.setDoctorSchedule(schedule);
        appointment.setAppointmentTime(request.getAppointmentTime());
        appointment.setPresentTime(LocalDateTime.now());

        Appointment savedAppointment = appointmentRepository.save(appointment);

        AppointmentStatus status = new AppointmentStatus();
        status.setAppointment(savedAppointment);
        status.setStatus("Chờ xác nhận");
        status.setUpdateAt(LocalDateTime.now());
        status.setUpdate_by(patient.getUser());
        appointmentStatusRepository.save(status);

        doctorScheduleRepository.save(schedule);

        return covertToResponse(savedAppointment);
    }

    public void updateAppointmentStatus(int appointmentId, String statusValue, int updatedByUserId, String reason) {
        Appointment appointment = appointmentRepository.findById(appointmentId)
                .orElseThrow(() -> new RuntimeException("Appointment not found"));

        // Tạo mới trạng thái
        AppointmentStatus status = new AppointmentStatus();
        status.setAppointment(appointment);
        status.setStatus(statusValue);
        status.setReason(reason);
        status.setUpdateAt(LocalDateTime.now());

        User updatedBy = new User();
        updatedBy.setId(updatedByUserId);
        status.setUpdate_by(updatedBy);

        appointmentStatusRepository.save(status);

        // Nếu xác nhận thì tăng số lượng bệnh nhân đã đặt
        if ("Xác nhận".equalsIgnoreCase(statusValue)) {
            DoctorSchedules schedule = appointment.getDoctorSchedule();
            if (schedule.getBookedPatients() >= schedule.getMaxPatients()) {
                throw new RuntimeException("Doctor schedule is fully booked");
            }
            schedule.setBookedPatients(schedule.getBookedPatients() + 1);
            doctorScheduleRepository.save(schedule);
        }
    }

    public List<AppointmentDTO> getAppointmentsByPatient(int patientId) {
        return appointmentRepository.findByPatientId(patientId)
                .stream()
                .map(this::covertToResponse)
                .collect(Collectors.toList());
    }

    public List<AppointmentDTO> getAppointmentsByDoctorSchedule(int doctorScheduleId) {
        return appointmentRepository.findByDoctorScheduleId(doctorScheduleId)
                .stream()
                .map(this::covertToResponse)
                .collect(Collectors.toList());
    }

    public List<AppointmentDTO> getAppointmentsByStatus(String status) {
        return appointmentRepository.findByStatus(status)
                .stream()
                .map(this::covertToResponse)
                .collect(Collectors.toList());
    }

    public List<AppointmentDTO> getAppointmentsByWeek(LocalDate startDate) {
        LocalDate endDate = startDate.plusDays(6);
        return appointmentRepository.findByAppointmentDateBetween(startDate, endDate)
                .stream()
                .map(this::covertToResponse)
                .collect(Collectors.toList());
    }

    public List<AppointmentDTO> getAppointmentsByDoctor(int doctorId) {
        return appointmentRepository.findByDoctorId(doctorId)
                .stream()
                .map(this::covertToResponse)
                .collect(Collectors.toList());
    }

    private AppointmentDTO covertToResponse(Appointment appointment) {
        AppointmentDTO dto = new AppointmentDTO();
        dto.setId(appointment.getId());
        dto.setDoctorId(appointment.getDoctor().getId());
        dto.setDoctorName(appointment.getDoctor().getStaff().getUser().getFullname());
        dto.setPatientId(appointment.getPatient().getId());
        dto.setPatientName(appointment.getPatient().getUser().getFullname());
        dto.setPresentTime(appointment.getPresentTime());
        dto.setRoomName(appointment.getDoctorSchedule().getRoom().getName());
        dto.setAppointmentDate(appointment.getDoctorSchedule().getDate().toString());
        dto.setAppointmentTime(appointment.getAppointmentTime());
        dto.setDoctorScheduleId(appointment.getDoctorSchedule().getId());
        Optional<AppointmentStatus> statusOpt = appointmentStatusRepository
                .findTopByAppointmentIdOrderByUpdateAtDesc(appointment.getId());

        dto.setStatus(statusOpt.map(AppointmentStatus::getStatus).orElse("Không rõ"));
        return dto;
    }
}
