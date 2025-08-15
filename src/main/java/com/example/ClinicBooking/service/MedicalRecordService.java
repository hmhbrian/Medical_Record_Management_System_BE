package com.example.ClinicBooking.service;

import com.example.ClinicBooking.DTO.MedicalRecordReponse;
import com.example.ClinicBooking.DTO.MedicalRecordRequest;
import com.example.ClinicBooking.Domain.Entities.Appointment;
import com.example.ClinicBooking.Domain.Entities.Doctor;
import com.example.ClinicBooking.Domain.Entities.MedicalRecord;
import com.example.ClinicBooking.Domain.Entities.Patient;
import com.example.ClinicBooking.Infrastructure.Repository.AppointmentRepository;
import com.example.ClinicBooking.Infrastructure.Repository.DoctorRepository;
import com.example.ClinicBooking.Infrastructure.Repository.MedicalRecordRepository;
import com.example.ClinicBooking.Infrastructure.Repository.PatientRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class MedicalRecordService {
    @Autowired
    private MedicalRecordRepository recordRepo;

    @Autowired
    private PatientRepository patientRepo;

    @Autowired
    private DoctorRepository doctorRepo;

    @Autowired
    private AppointmentRepository appointmentRepo;

    public MedicalRecord createMedicalRecord(MedicalRecordRequest request) {
        MedicalRecord record = new MedicalRecord();

        Patient patient = patientRepo.findById(request.getPatientId())
                .orElseThrow(() -> new RuntimeException("Patient not found"));
        Doctor doctor = doctorRepo.findById(request.getDoctorId())
                .orElseThrow(() -> new RuntimeException("Doctor not found"));
        Appointment appointment = appointmentRepo.findById(request.getAppointmentId())
                .orElseThrow(() -> new RuntimeException("Appointment not found"));

        LocalDate today = LocalDate.now();
        int visitNumber = recordRepo.countVisitNumber(doctor.getId(), today) + 1;

        record.setPatient(patient);
        record.setDoctor(doctor);
        record.setAppointment(appointment);
        record.setVisitDate(today);
        record.setVisitNumber(visitNumber);
        record.setInitialSymptoms(request.getInitialSymptoms());
        record.setDiagnosis(request.getDiagnosis());

        return recordRepo.save(record);
    }

    public List<MedicalRecordReponse> getRecordsByPatientId(Integer patientId) {
        return recordRepo.findByPatientId(patientId)
                .stream()
                .map(this::covertToResponse)
                .collect(Collectors.toList());
    }


    public List<MedicalRecord> getRecordsByDoctorId(Integer doctorId) {
        return recordRepo.findByDoctorId(doctorId);
    }

    public List<MedicalRecord> getAllRecordsGroupedByPatient() {
        return recordRepo.findAllGroupedByPatient();
    }

    public Optional<MedicalRecord> getRecordById(Integer id) {
        return recordRepo.findById(id);
    }

    private MedicalRecordReponse covertToResponse(MedicalRecord medicalRecord) {
        MedicalRecordReponse dto = new MedicalRecordReponse();
        dto.setId(medicalRecord.getId());
        dto.setDoctorId(medicalRecord.getDoctor().getId());
        dto.setDoctorName(medicalRecord.getDoctor().getStaff().getUser().getFullname());
        dto.setPatientId(medicalRecord.getPatient().getId());
        dto.setPatientName(medicalRecord.getPatient().getUser().getFullname());
        dto.setDiagnosis(medicalRecord.getDiagnosis());
        dto.setInitialSymptoms(medicalRecord.getInitialSymptoms());
        dto.setVisitDate(medicalRecord.getVisitDate());
        dto.setVisitNumber(medicalRecord.getVisitNumber());
        return dto;
    }
}

