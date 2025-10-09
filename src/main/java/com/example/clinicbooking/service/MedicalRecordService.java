package com.example.clinicbooking.service;

import com.example.clinicbooking.DTO.MedicalRecord.MedicalRecordRequest;
import com.example.clinicbooking.DTO.MedicalRecord.MedicalRecordResponse;
import com.example.clinicbooking.entity.Appointment;
import com.example.clinicbooking.entity.Doctor;
import com.example.clinicbooking.entity.MedicalRecord;
import com.example.clinicbooking.entity.Patient;
import com.example.clinicbooking.repository.AppointmentRepository;
import com.example.clinicbooking.repository.DoctorRepository;
import com.example.clinicbooking.repository.MedicalRecordRepository;
import com.example.clinicbooking.repository.PatientRepository;
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

    public List<MedicalRecordResponse> getRecordsByPatientId(Integer patientId) {
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

    private MedicalRecordResponse covertToResponse(MedicalRecord medicalRecord) {
        MedicalRecordResponse dto = new MedicalRecordResponse();
        dto.setId(medicalRecord.getId());
        dto.setDoctorId(medicalRecord.getDoctor().getId());
        dto.setDoctorName(medicalRecord.getDoctor().getStaff().getUser().getFullname());
        dto.setPatientId(medicalRecord.getPatient().getId());
        dto.setPatientName(medicalRecord.getPatient().getUser().getFullname());
        dto.setInitialSymptoms(medicalRecord.getInitialSymptoms());
        dto.setDiagnosis(medicalRecord.getDiagnosis());
        dto.setVisitNumber(medicalRecord.getVisitNumber());
        dto.setVisitDate(medicalRecord.getVisitDate().toString());
        dto.setAppointmentId(medicalRecord.getAppointment().getId());
        return dto;
    }
}

