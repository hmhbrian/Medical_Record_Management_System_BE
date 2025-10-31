package com.example.clinicbooking.service;

import com.example.clinicbooking.DTO.ApiResponse;
import com.example.clinicbooking.DTO.MedicalRecord.ServiceData.ServiceDetail;
import com.example.clinicbooking.DTO.Prescription.PrescriptionDetailsRequest;
import com.example.clinicbooking.DTO.Prescription.PrescriptionDetailsResponse;
import com.example.clinicbooking.DTO.Prescription.PrescriptionRequest;
import com.example.clinicbooking.DTO.Prescription.PrescriptionResponse;
import com.example.clinicbooking.entity.*;
import com.example.clinicbooking.exceptions.InvalidInputException;
import com.example.clinicbooking.repository.*;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PrescriptionService {
    private final PrescriptionRepository prescriptionRepo;
    private final PrescriptionDetailRepository detailRepo;
    private final MedicalRecordRepository recordRepo;
    private final MedicineRepository medicineRepo;
    private final ResultExaminationRepository examinationRepo;
    private final PaymentService paymentService;

    //Lưu hoặc Gửi Đơn thuốc
    @Transactional
    public ApiResponse<?> saveOrSendPrescription(Integer recordId, PrescriptionRequest dto) {

        MedicalRecord record = recordRepo.findById(recordId)
                .orElseThrow(() -> new InvalidInputException("Hồ sơ bệnh án không tồn tại."));

        //Danh sách các ID dịch vụ (Service ID) cần thanh toán
        List<ServiceDetail> serviceItems = new ArrayList<>();

        // Tổng giá trị Đơn thuốc
        Double PrescriptionPrice = 0.0;

        // 1. Tìm hoặc Tạo mới Đơn thuốc (Prescription)
        Optional<Prescriptions> existingPrescription = prescriptionRepo.findByRecord(record);

        // Kiểm tra trạng thái PAID trước khi chỉnh sửa
        if(existingPrescription.isPresent()){
            if(existingPrescription.get().getStatus() == PrescriptionStatus.PENDING_PAYMENT)
                return new ApiResponse<>(false,"Đơn thuốc đang chờ thanh toán, không thể chỉnh sửa.", null);
            else if(existingPrescription.get().getStatus() == PrescriptionStatus.PAID)
                return new ApiResponse<>(false,"Đơn thuốc đã được thanh toán, không thể chỉnh sửa.", null);
        }

        Prescriptions prescription = existingPrescription.orElseGet(() -> {
            Prescriptions newP = new Prescriptions();
            newP.setRecord(record);
            newP.setDoctor(record.getDoctor());
            newP.setPrescriptionDate(LocalDateTime.now());
            return newP;
        });

        // 2. Cập nhật thông tin chính
        prescription.setTotalDays(dto.getTotal_days());

        // Cập nhật trạng thái
        if (dto.isSend()) {
            prescription.setStatus(PrescriptionStatus.PENDING_PAYMENT); // Hoặc PENDING_DISPENSE nếu miễn phí
        } else {
            prescription.setStatus(PrescriptionStatus.DRAFT);
        }

        // Lưu để đảm bảo có ID Prescription
        Prescriptions final_prescription = prescriptionRepo.save(prescription);

        // 3. Đồng bộ hóa Chi tiết Đơn thuốc (DELETE CŨ + INSERT MỚI)

        // 3.1. Xóa các chi tiết cũ
        detailRepo.deleteByPrescriptionId(prescription.getId());

        // --- 3.2. Tạo Chi tiết mới và Tính toán Tổng tiền (BỘ PHẬN ĐÃ SỬA) ---
        List<PrescriptionDetails> newDetails = new ArrayList<>();

        for (PrescriptionDetailsRequest detailDto : dto.getPrescriptionDetails()){
            Medicine medicine = medicineRepo.findById(detailDto.getMedicineId())
                            .orElseThrow(() -> new InvalidInputException("Thuốc không tồn tại: " + detailDto.getMedicineId()));

            PrescriptionDetails detail = new PrescriptionDetails();
            detail.setPrescription(final_prescription);
            detail.setMedicine(medicine);
            detail.setQuantity(detailDto.getQuantity());
            detail.setDailyQuantity(detailDto.getDailyQuantity());
            detail.setDosage(detailDto.getDosage());
            detail.setNotes(detailDto.getNotes());
            detail.set_substitutable(detailDto.getIsSubstitutable());

            PrescriptionPrice += medicine.getPrice() * detailDto.getQuantity();
            newDetails.add(detail);
        }

        detailRepo.saveAll(newDetails);

        //4. Xử lý logic dịch vụ thanh toán nếu là gửi đơn thuốc
        if(dto.isSend()){
            // Thêm mục Đơn thuốc vào danh sách dịch vụ cần thanh toán
            serviceItems.add(new ServiceDetail(
                    "PRESCRIPTION",
                    final_prescription.getId(),
                    "Đơn thuốc ngày " + final_prescription.getPrescriptionDate().toString(),
                    PrescriptionPrice
            ));

            // Thêm mục Khám bệnh vào danh sách dịch vụ cần thanh toán
            ResultExamination resultExamination = examinationRepo.findByRecord(record).orElseThrow
                    (() -> new InvalidInputException("Không tìm thấy dịch v khám bệnh cho hồ sơ bệnh án này.")
            );

            if(resultExamination != null)
                serviceItems.add(new ServiceDetail(
                        "EXAMINATION",
                        resultExamination.getId(),
                        resultExamination.getExamination().getExaminationName(),
                        resultExamination.getExamination().getPrice()
                ));

            paymentService.createPaymentOrder(record, serviceItems);
            record.setStatus(MedicalRecordStatus.PENDING_POSTPAYMENT);
            recordRepo.save(record);
            return new ApiResponse<>(true, "Đơn thuốc đã được xác nhận và chuyển sang thanh toán.",null);
        }


        return new ApiResponse<>(true,"Đơn thuốc đã được lưu thành công.", null);
    }

    // Lấy Đơn thuốc theo ID Hồ sơ bệnh án
    public PrescriptionResponse getPrescriptionByRecordId(Integer recordId) {
        MedicalRecord record = recordRepo.findById(recordId).orElseThrow(
                () -> new InvalidInputException("Hồ sơ này không tồn tại"));

        Prescriptions prescription = prescriptionRepo.findByRecord(record)
                .orElseThrow(() -> new InvalidInputException("Không tìm thấy đơn thuốc cho hồ sơ này."));

        List<PrescriptionDetails> details = detailRepo.findAllByPrescription(prescription); // Cần viết method này

        return convertToResponseDTO(prescription, details);
    }

    private PrescriptionResponse convertToResponseDTO(Prescriptions p, List<PrescriptionDetails> details) {
        // 1. Ánh xạ các chi tiết thuốc (Details)
        List<PrescriptionDetailsResponse> detailDtos = details.stream()
                .map(this::mapDetailToDTO)
                .collect(Collectors.toList());

        // 2. Ánh xạ Đơn thuốc chính (Prescriptions)
        PrescriptionResponse response = new PrescriptionResponse();

        response.setPrescriptionId(p.getId());
        response.setTotalDays(p.getTotalDays());
        response.setStatus(p.getStatus().name()); // Chuyển Enum sang String
        if(!p.getStatus().equals(PrescriptionStatus.DRAFT)){
            response.setSend(true);
        }

        // Gán danh sách chi tiết đã ánh xạ
        response.setDetails(detailDtos);
        return response; // Dữ liệu trả về
    }

    /**
     * Hàm ánh xạ chi tiết từng loại thuốc từ Entity sang DTO.
     */
    private PrescriptionDetailsResponse mapDetailToDTO(PrescriptionDetails detail) {
        PrescriptionDetailsResponse detailDto = new PrescriptionDetailsResponse();
        Medicine medicine = detail.getMedicine();

        // Thông tin thuốc cơ bản
        detailDto.setMedicineId(medicine.getId());
        detailDto.setMedicineName(medicine.getMedicineName());

        // Thông tin kê đơn
        detailDto.setQuantity(detail.getQuantity());
        detailDto.setDosage(detail.getDosage());
        detailDto.setDailyQuantity(detail.getDailyQuantity());
        detailDto.setNotes(detail.getNotes());
        detailDto.setIsSubstitutable(detail.is_substitutable());

        return detailDto;
    }
}
