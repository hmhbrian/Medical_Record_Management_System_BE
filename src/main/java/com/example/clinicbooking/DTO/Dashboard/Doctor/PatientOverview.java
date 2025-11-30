package com.example.clinicbooking.DTO.Dashboard.Doctor;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
@AllArgsConstructor
@RequiredArgsConstructor
public class PatientOverview {
    private Integer totalPatients;          // Tổng số bệnh nhân (hôm nay)
    private Integer totalPatientsForMe;     // Tổng số bệnh nhân của bác sĩ (hôm nay)
    private Integer totalAppointmentsForMe; // Tổng số cuộc hẹn của bác sĩ (hôm nay)
    private Integer totalCompletedForMe;    // Tổng số cuộc hẹn đã hoàn thành của bác sĩ (hôm nay)
}
