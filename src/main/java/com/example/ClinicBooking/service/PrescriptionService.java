package com.example.ClinicBooking.service;

import com.example.ClinicBooking.DTO.PrescriptionDetailRequest;
import com.example.ClinicBooking.DTO.PrescriptionDetailResponse;
import com.example.ClinicBooking.DTO.PrescriptionRequest;
import com.example.ClinicBooking.DTO.PrescriptionResponse;
import com.example.ClinicBooking.Domain.Entities.Medicine;
import com.example.ClinicBooking.Domain.Entities.Prescription;
import com.example.ClinicBooking.Domain.Entities.PrescriptionDetail;
import com.example.ClinicBooking.Infrastructure.Repository.*;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class PrescriptionService {
    @Autowired
    private PrescriptionRepository prescriptionRepo;

    @Autowired
    private PrescriptionDetailRepository detailRepo;

    @Autowired
    private DoctorRepository doctorRepo;

    @Autowired
    private PharmacyRepository phaRepo;

    @Autowired
    private MedicineRepository medicineRepo;

    public PrescriptionResponse createPrescription(PrescriptionRequest request) {
        Prescription prescription = new Prescription();
        prescription.setRecordId(request.getRecordId());
        prescription.setInpatientRecordId(request.getInpatientRecordId());
        prescription.setDoctor(doctorRepo.findById(request.getDoctorId())
                .orElseThrow(() -> new RuntimeException("Doctor not found")));
        prescription.setPharmacist(phaRepo.findById(request.getPharmacistId())
                .orElse(null));
        prescription.setStatus("NEW");
        prescription.setPrescriptionDate(LocalDate.now());

        List<PrescriptionDetail> details = new ArrayList<>();
        for (PrescriptionDetailRequest detailReq : request.getDetails()) {
            PrescriptionDetail detail = new PrescriptionDetail();
            detail.setPrescription(prescription);

            Medicine medicine = medicineRepo.findById(detailReq.getMedicineId())
                    .orElseThrow(() -> new RuntimeException("Medicine not found"));

            detail.setMedicine(medicine);
            detail.setQuantity(detailReq.getQuantity());
            detail.setDosage(detailReq.getDosage());
            detail.setNotes(detailReq.getNotes());
            details.add(detail);
        }
        prescription.setDetails(details);

        Prescription saved = prescriptionRepo.save(prescription);
        return convertToResponse(saved);
    }

    public PrescriptionResponse getByRecordId(Integer recordId) {
        Prescription prescription = prescriptionRepo.findByRecordId(recordId)
                .orElseThrow(() -> new RuntimeException("Prescription not found for recordId: " + recordId));
        return convertToResponse(prescription);
    }

    @Transactional
    public PrescriptionResponse updatePrescription(Integer id, PrescriptionRequest request) {
        Prescription prescription = prescriptionRepo.findById(id)
                .orElseThrow(() -> new RuntimeException("Prescription not found"));

        // Update pharmacist & date
        prescription.setPharmacist(phaRepo.findById(request.getPharmacistId()).orElse(null));
        prescription.setPrescriptionDate(LocalDate.now());

        // Clear old details
        prescription.getDetails().clear();

        List<PrescriptionDetail> newDetails = request.getDetails().stream().map(detailReq -> {
            PrescriptionDetail detail = new PrescriptionDetail();
            detail.setPrescription(prescription);

            Medicine medicine = medicineRepo.findById(detailReq.getMedicineId())
                    .orElseThrow(() -> new RuntimeException("Medicine not found"));

            detail.setMedicine(medicine);
            detail.setQuantity(detailReq.getQuantity());
            detail.setDosage(detailReq.getDosage());
            detail.setNotes(detailReq.getNotes());
            return detail;
        }).collect(Collectors.toList());

        prescription.getDetails().addAll(newDetails);

        Prescription updated = prescriptionRepo.save(prescription);
        return convertToResponse(updated);
    }


    private PrescriptionResponse convertToResponse(Prescription prescription) {
        PrescriptionResponse response = new PrescriptionResponse();

        response.setId(prescription.getId());
        response.setRecordId(prescription.getRecordId());
        if (prescription.getInpatientRecordId() != null) {
            response.setInpatientRecordId(prescription.getInpatientRecordId());
        }
        response.setDoctorName(prescription.getDoctor().getStaff().getUser().getFullname());
        response.setDoctorId(prescription.getDoctor().getId());
        response.setPharmacistName(prescription.getPharmacist().getStaff().getUser().getFullname());
        response.setPharmacistId(prescription.getPharmacist().getId());
        response.setStatus(prescription.getStatus());
        response.setPrescriptionDate(prescription.getPrescriptionDate());

        List<PrescriptionDetailResponse> detailResponses = prescription.getDetails().stream().map(detail -> {
            PrescriptionDetailResponse d = new PrescriptionDetailResponse();
            d.setId(detail.getId());
            d.setMedicineId(detail.getMedicine().getId());
            d.setMedicineName(detail.getMedicine().getMedicineName()); // assuming getName exists
            d.setQuantity(detail.getQuantity());
            d.setDosage(detail.getDosage());
            d.setNotes(detail.getNotes());
            return d;
        }).collect(Collectors.toList());

        response.setDetails(detailResponses);
        return response;
    }
}
