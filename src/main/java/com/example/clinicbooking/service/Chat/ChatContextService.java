package com.example.clinicbooking.service.Chat;

import com.example.clinicbooking.DTO.Chat.DoctorSuggestion;
import com.example.clinicbooking.entity.*;
import com.example.clinicbooking.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ChatContextService {

    private final AppointmentRepository appointmentRepository;
    private final MedicalRecordRepository medicalRecordRepository;
    private final PrescriptionRepository prescriptionRepository;
    private final KeywordIcdHintRepository keywordIcdHintRepository;
    private final IcdSpecialtyRepository icdSpecialtyRepository;
    private final DoctorRepository doctorRepository;
    private final PatientRepository patientRepository;

    /**
     * Lấy context của user để AI có thể trả lời câu hỏi về lịch hẹn, lịch sử khám
     */
    public String getUserContext(Integer userId) {
        StringBuilder context = new StringBuilder();

        try {
            // Tìm Patient từ userId
            Patient patient = patientRepository.findByUserId(userId).orElse(null);
            if (patient == null) {
                return "";
            }

            // Lấy lịch hẹn sắp tới
            List<Appointment> upcomingAppointments = appointmentRepository
                    .findByPatientId(patient.getId()).stream()
                    .filter(a -> a.getDoctorSchedule() != null &&
                            a.getDoctorSchedule().getDate().isAfter(LocalDate.now().minusDays(1)))
                    .limit(3)
                    .collect(Collectors.toList());

            if (!upcomingAppointments.isEmpty()) {
                context.append("Lịch hẹn sắp tới:\\n");
                for (Appointment apt : upcomingAppointments) {
                    context.append(String.format("- Ngày %s, Bác sĩ %s\\n",
                            apt.getDoctorSchedule().getDate(),
                            apt.getDoctor().getStaff().getUser().getFullname()));
                }
            }

            // Lấy lịch sử khám gần đây
            List<MedicalRecord> recentRecords = medicalRecordRepository
                    .findByPatientId(patient.getId()).stream()
                    .limit(3)
                    .collect(Collectors.toList());

            if (!recentRecords.isEmpty()) {
                context.append("\\nLịch sử khám gần đây:\\n");
                for (MedicalRecord record : recentRecords) {
                    context.append(String.format("- Chẩn đoán: %s, Bác sĩ: %s\\n",
                            record.getDiagnosis() != null ? record.getDiagnosis() : "Chưa có",
                            record.getDoctor().getStaff().getUser().getFullname()));
                }
            }

        } catch (Exception e) {
            System.err.println("Error getting user context: " + e.getMessage());
        }

        return context.toString();
    }

    /**
     * Tìm bác sĩ dựa trên triệu chứng mà bệnh nhân mô tả
     * Flow: Triệu chứng → Keyword ICD Hints → ICD Specialty → Doctors
     */
    public List<DoctorSuggestion> findDoctorsBySymptoms(String symptoms) {
        List<DoctorSuggestion> suggestions = new ArrayList<>();

        try {
            // Bước 1: Chuẩn hóa text triệu chứng (thêm khoảng trắng 2 đầu)
            String normalizedSymptoms = " " + symptoms.toLowerCase() + " ";

            // Bước 2: Tìm ICD prefix hints từ keywords
            Set<String> icdPrefixes = keywordIcdHintRepository
                    .findDistinctIcdPrefixByKeywords(normalizedSymptoms);

            if (icdPrefixes.isEmpty()) {
                return suggestions; // Không tìm thấy keyword match
            }

            // Bước 3: Từ ICD prefixes → Specialty IDs
            Set<Integer> specialtyIds = icdSpecialtyRepository
                    .findDistinctSpecialtyIdsByIcdPrefixIn(icdPrefixes);

            if (specialtyIds.isEmpty()) {
                return suggestions;
            }

            // Bước 4: Tìm bác sĩ theo specialty
            List<Doctor> doctors = doctorRepository.findBySpecialtyIdIn(specialtyIds);

            // Bước 5: Convert sang DTO
            for (Doctor doctor : doctors) {
                DoctorSuggestion suggestion = new DoctorSuggestion();
                suggestion.setDoctorId(doctor.getId());
                suggestion.setDoctorName(doctor.getStaff().getUser().getFullname());
                suggestion.setSpecialty(doctor.getSpecialty().getName());
                suggestion.setDoctorAvatarUrl(doctor.getStaff().getUser().getAvatar_url());
                suggestion.setExperienceYears(doctor.getExperienceYears());
                suggestion.setIsAvailable(true); // TODO: Check schedule availability
                suggestions.add(suggestion);
            }

        } catch (Exception e) {
            System.err.println("Error finding doctors by symptoms: " + e.getMessage());
        }

        return suggestions;
    }

    /**
     * Format lịch sử hồ sơ bệnh án để AI có thể đọc
     */
    public String formatMedicalRecordHistory(Integer patientId) {
        StringBuilder history = new StringBuilder();

        try {
            List<MedicalRecord> records = medicalRecordRepository.findByPatientId(patientId);

            if (records.isEmpty()) {
                return "Bệnh nhân chưa có lịch sử khám.";
            }

            history.append("Lịch sử khám bệnh:\\n");
            for (MedicalRecord record : records) {
                history.append(String.format("- Triệu chứng ban đầu: %s\\n",
                        record.getInitialSymptoms() != null ? record.getInitialSymptoms() : "N/A"));
                history.append(String.format("  Chẩn đoán: %s\\n",
                        record.getDiagnosis() != null ? record.getDiagnosis() : "Chưa có"));
                history.append(String.format("  Bác sĩ: %s\\n",
                        record.getDoctor().getStaff().getUser().getFullname()));

                // Lấy đơn thuốc nếu có
                prescriptionRepository.findByRecord(record).ifPresent(prescription -> {
                    history.append(String.format("  Đơn thuốc: Mã %s\\n", prescription.getCode()));
                });

                history.append("\\n");
            }

        } catch (Exception e) {
            System.err.println("Error formatting medical record history: " + e.getMessage());
        }

        return history.toString();
    }
}
