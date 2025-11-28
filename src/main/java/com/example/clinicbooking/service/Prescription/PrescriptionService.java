package com.example.clinicbooking.service.Prescription;

import com.example.clinicbooking.DTO.ApiResponse;
import com.example.clinicbooking.DTO.LabTest.LabTestOfStaffResponse;
import com.example.clinicbooking.DTO.LabTest.LabTestWaitingRequest;
import com.example.clinicbooking.DTO.LabTest.LabTestWaitingResponse;
import com.example.clinicbooking.DTO.MedicalRecord.ServiceData.ServiceDetail;
import com.example.clinicbooking.DTO.PaginatedResponseDTO;
import com.example.clinicbooking.DTO.Patient.PatientSummary;
import com.example.clinicbooking.DTO.Prescription.*;
import com.example.clinicbooking.DTO.Prescription.Detail.PrescriptionDetailsOfStaffRp;
import com.example.clinicbooking.DTO.Prescription.Detail.PrescriptionDetailsRequest;
import com.example.clinicbooking.DTO.Prescription.Detail.PrescriptionDetailsResponse;
import com.example.clinicbooking.entity.*;
import com.example.clinicbooking.exceptions.InvalidInputException;
import com.example.clinicbooking.repository.*;
import com.example.clinicbooking.service.LabTest.LabTestSpecification;
import com.example.clinicbooking.service.Payment.PaymentService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
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
    private final PaymentRepository paymentRepo;
    private final PaymentService paymentService;
    private final PharmacyStaffRepository pharmacyStaffRepo;

    //Lưu hoặc Gửi Đơn thuốc
    @Transactional
    public ApiResponse<?> saveOrSendPrescription(Integer recordId, PrescriptionRequest dto) {

        MedicalRecord record = recordRepo.findById(recordId)
                .orElseThrow(() -> new InvalidInputException("Hồ sơ bệnh án không tồn tại."));

        //Danh sách các ID dịch vụ (Service ID) cần thanh toán
        List<ServiceDetail> serviceItems = new ArrayList<>();

        // Tổng giá trị Đơn thuốc
        Double PrescriptionPrice = 0.0;

        // List các trạng thái bị loại trừ (ví dụ: CANCELED, COMPLETED)
        List<PrescriptionStatus> excludedStatuses = List.of(PrescriptionStatus.CANCELED, PrescriptionStatus.COMPLETED, PrescriptionStatus.PAID, PrescriptionStatus.PENDING_PAYMENT);

        // 1. Tìm hoặc Tạo mới Đơn thuốc (Prescription)
        // Tìm đơn có tồn tại nhưng chưa bị hủy hoặc hoàn thành
        Optional<Prescriptions> existingPrescription = prescriptionRepo.findByRecordAndStatusNotIn(record,excludedStatuses);

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
            record.setStatus(MedicalRecordStatus.COMPLETED);
            recordRepo.save(record);
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
            Optional<ResultExamination> optionalExamResult = examinationRepo.findByRecord(record);

            if(optionalExamResult.isPresent()) {
                ResultExamination resultExamination = optionalExamResult.get();
                serviceItems.add(new ServiceDetail(
                        "EXAMINATION",
                        resultExamination.getId(),
                        resultExamination.getExamination().getExaminationName(),
                        resultExamination.getExamination().getPrice()
                ));
            }

            paymentService.createPaymentOrder(record, serviceItems);
            record.setStatus(MedicalRecordStatus.PENDING_POSTPAYMENT);
            recordRepo.save(record);
            return new ApiResponse<>(true, "Đơn thuốc đã được xác nhận và chuyển sang thanh toán.",null);
        }


        return new ApiResponse<>(true,"Đơn thuốc đã được lưu thành công.", null);
    }

    //Hủy đơn thuốc đang chờ thanh toán
    @Transactional
    public ApiResponse<?> cancelPrescriptionAndAllowNew(Integer recordId) {

        MedicalRecord medicalRecord = recordRepo.findById(recordId)
                .orElseThrow(() -> new InvalidInputException("Không tồn tại hồ sơ bệnh án này."));

        // 1. Tìm đơn thuốc hiện tại
        Prescriptions prescription = prescriptionRepo.findByRecord(medicalRecord)
                .orElseThrow(() -> new InvalidInputException("Hồ sơ bệnh án không có đơn thuốc nào."));

        // 2. Kiểm tra trạng thái đơn thuốc
        if (prescription.getStatus() == PrescriptionStatus.PAID ||
                prescription.getStatus() == PrescriptionStatus.COMPLETED) {

            return new ApiResponse<>(false, "Đơn thuốc đã được thanh toán hoặc hoàn thành, không thể hủy. Cần thực hiện hoàn tiền (Refund).", null);
        }

        if (prescription.getStatus() == PrescriptionStatus.CANCELED) {
            return new ApiResponse<>(false, "Đơn thuốc đã bị hủy trước đó.", null);
        }

        // 3. Xử lý HỦY PHIẾU THANH TOÁN (nếu đang ở trạng thái PENDING_PAYMENT)
        if (prescription.getStatus() == PrescriptionStatus.PENDING_PAYMENT) {

            // 3.1. Tìm phiếu thanh toán liên quan
            Optional<Payment> paymentOpt = paymentRepo.findByObjectTypeAndObjectIdAndStatus(
                    "PRESCRIPTION",
                    prescription.getId(),
                    PaymentStatus.PENDING_PAYMENT
            );

            if (paymentOpt.isPresent()) {
                // 3.2. Cập nhật trạng thái Payment sang CANCELED
                Payment paymentToCancel = paymentOpt.get();
                paymentToCancel.setStatus(PaymentStatus.CANCELLED);
                paymentRepo.save(paymentToCancel);

                // Gửi thông báo đến Thu ngân về việc hủy hóa đơn
                // paymentService.notifyCashierOfCancellation(paymentToCancel);
            }
        }

        // 4. Cập nhật trạng thái Đơn thuốc sang CANCELED
        prescription.setStatus(PrescriptionStatus.CANCELED);
        prescriptionRepo.save(prescription);

        // 5. Kết quả: Cho phép bác sĩ tạo đơn mới
        return new ApiResponse<>(true, "Đơn thuốc cũ đã được hủy thành công. Bạn có thể kê đơn mới.", null);
    }

    //XÁC NHẬN ĐẢM NHẬN SOOẠN TOA THUỐC
    public ApiResponse<?> assignPrescription(Integer prescriptionId, Integer currentUserId) {
        //1. Lấy thông tin toa thuốc
        Prescriptions prescriptions = prescriptionRepo.findById(prescriptionId)
                .orElseThrow(() -> new InvalidInputException("Toa thuốc không tồn tại."));
        if (!prescriptions.getStatus().equals(PrescriptionStatus.PAID) || prescriptions.getPharmacyStaff() != null) {
            throw new InvalidInputException("Toa thuốc này không thể đảm nhận (đã có người làm hoặc đang tiến hành).");
        }

        //2.Lấy thông tin LabStaff
        PharmacyStaff pharmacyStaff = pharmacyStaffRepo.findByUserId(currentUserId);
        if(pharmacyStaff == null){
            throw new InvalidInputException("Nhân viên nhà thuốc không tồn tại.");
        }


        //3. Gán nhân viên xét nghiệm
        prescriptions.setPharmacyStaff(pharmacyStaff);
        prescriptions.setStatus(PrescriptionStatus.IN_PROGRESS);
        prescriptionRepo.save(prescriptions);

        return new ApiResponse<>(true, "Xác nhận toa thuốc thành công", null);
    }

    @Transactional
    public void completeDispensing(int prescriptionId, Integer currentUserId) {
        //0.Lấy thông tin PharmacyStaff
        PharmacyStaff pharmacyStaff = pharmacyStaffRepo.findByUserId(currentUserId);
        if(pharmacyStaff == null){
            throw new InvalidInputException("Nhân viên nhà thuốc không tồn tại.");
        }

        // 1. Lấy đơn thuốc chính
        Prescriptions prescription = prescriptionRepo.findById(prescriptionId)
                .orElseThrow(() -> new InvalidInputException("Prescription not found"));

        // Kiểm tra xem nhân viên có được phân công cho đơn thuốc này không
        if(!pharmacyStaff.equals(prescription.getPharmacyStaff())){
            throw new InvalidInputException("You are not assigned to this prescription.");
        }

        // Kiểm tra trạng thái: chỉ hoàn thành khi đơn đã thanh toán
        if (prescription.getStatus() != PrescriptionStatus.IN_PROGRESS) {
            throw new IllegalStateException("Prescription must be in IN_PROGRESS status to be completed.");
        }

        // 2. Cập nhật trạng thái đơn thuốc
        prescription.setStatus(PrescriptionStatus.COMPLETED);
        prescription.setDispensedAt(LocalDateTime.now()); // Lưu thời điểm hoàn thành

        prescriptionRepo.save(prescription);

        // 3. Cập nhật tồn kho cho từng chi tiết thuốc
        List<PrescriptionDetails> details = detailRepo.findAllByPrescription(prescription);

        for (PrescriptionDetails detail : details) {
            Medicine medicine = detail.getMedicine();
            Integer quantityToDeduct = detail.getQuantity();

            // 3.1. Cập nhật Tồn kho Thực tế (current_quantity)
            double newCurrentQty = medicine.getCurrent_quantity() - quantityToDeduct;
            if (newCurrentQty < 0) {
                throw new InvalidInputException("Inventory shortage detected for medicine: " + medicine.getMedicineName());
            }
            medicine.setCurrent_quantity(newCurrentQty);

            // 3.2. Cập nhật Tồn kho Trừ Mềm (reserved_quantity)
            // Khi cấp phát xong, phải giải phóng lượng đã trừ mềm
            double newReservedQty = medicine.getReserved_quantity() - quantityToDeduct;
            if (newReservedQty < 0) {
                //reserved không bao giờ được nhỏ hơn 0
                newReservedQty = 0;
            }
            medicine.setReserved_quantity(newReservedQty);

            // Lưu lại cập nhật tồn kho
            medicineRepo.save(medicine);
        }
    }

    //=======GET========
    // Lấy Đơn thuốc theo ID Hồ sơ bệnh án
    public PrescriptionResponse getPrescriptionByRecordId(Integer recordId) {
        MedicalRecord record = recordRepo.findById(recordId).orElseThrow(
                () -> new InvalidInputException("Hồ sơ này không tồn tại"));

        // List các trạng thái bị loại trừ (ví dụ: CANCELED, COMPLETED)
        List<PrescriptionStatus> excludedStatuses = List.of(PrescriptionStatus.CANCELED);
        Prescriptions prescription = prescriptionRepo.findByRecordAndStatusNotIn(record, excludedStatuses)
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

    public PrescriptionResponseDTO getPrescriptionByRecord(MedicalRecord record) {
        // List các trạng thái bị loại trừ (ví dụ: CANCELED, COMPLETED)
        List<PrescriptionStatus> excludedStatuses = List.of(PrescriptionStatus.CANCELED);
        Prescriptions prescription = prescriptionRepo.findByRecordAndStatusNotIn(record, excludedStatuses)
                .orElse(null);
        if(prescription == null){
            return null;
        }

        List<PrescriptionDetails> details = detailRepo.findAllByPrescription(prescription); // Cần viết method này

        // 1. Ánh xạ các chi tiết thuốc (Details)
        List<PrescriptionDetailsResponse> detailDtos = details.stream()
                .map(this::mapDetailToDTO)
                .collect(Collectors.toList());

        // 2. Ánh xạ Đơn thuốc chính (Prescriptions)
        PrescriptionResponseDTO response = new PrescriptionResponseDTO();

        response.setTotalDays(prescription.getTotalDays());
        response.setStatus(prescription.getStatus().name());
        if(prescription.getPharmacyStaff() != null && prescription.getPharmacyStaff().getStaff().getUser().getFullname() != null){
            response.setPharmacistName(prescription.getPharmacyStaff().getStaff().getUser().getFullname());
            response.setPharmacistCode(prescription.getPharmacyStaff().getPharmacyCode());
        }else {
            response.setPharmacistName("Chưa phân công");
        }

        // Gán danh sách chi tiết đã ánh xạ
        response.setDetails(detailDtos);
        return response;
    }

    //Hàm ánh xạ chi tiết từng loại thuốc từ Entity sang DTO.
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

    // HIỂN THỊ DS ĐƠN THUỐC CẦN THỰC HIỆN THEO BỘ LỌC VỚI PHÂN TRANG
    public PaginatedResponseDTO<PrescriptionWaitingResponse> searchPrescriptionWaiting(PrescriptionWaitingRequest request) {

        // 1. Chuẩn bị phân trang và sắp xếp
        Sort sort = Sort.by(Sort.Direction.fromString(request.getSortDir()), request.getSortBy());
        Pageable pageable = PageRequest.of(request.getPage(), request.getSize(), sort);

        // 2. Xây dựng Specification (logic lọc)
        Specification<Prescriptions> spec = PrescriptionSpecification.filterPrescriptions(request,ServiceStatus.PAID.name(), null);

        // 3. Thực hiện truy vấn
        Page<Prescriptions> prescriptionPage = prescriptionRepo.findAll(spec, pageable);

        // 4. Ánh xạ (Mapping) Entity sang Response DTO
        List<PrescriptionWaitingResponse> responsePrecription = prescriptionPage.getContent().stream()
                .map(this::covertToWaitingResponse) // Sử dụng hàm covertToResponse để chuyển đổi
                .collect(Collectors.toList());

        // 5. Trả về Paginated Response
        return new PaginatedResponseDTO<PrescriptionWaitingResponse>(
                prescriptionPage.getNumber(),
                prescriptionPage.getSize(),
                prescriptionPage.getTotalElements(),
                prescriptionPage.getTotalPages(),
                responsePrecription
        );
    }

    // Hàm chuyển đổi từ Prescriptions entity sang PrescriptionWaitingResponse DTO
    private PrescriptionWaitingResponse covertToWaitingResponse(Prescriptions prescriptions) {
        PrescriptionWaitingResponse dto = new PrescriptionWaitingResponse();
        dto.setPrescriptionId(prescriptions.getId());
        dto.setPrescriptionCode(prescriptions.getCode());
        dto.setRecordCode(prescriptions.getRecord().getCode());
        dto.setRequestedDate(prescriptions.getPrescriptionDate());
        dto.setDoctorInChargeName(prescriptions.getDoctor().getStaff().getUser().getFullname());
        dto.setStatus(prescriptions.getStatus().name());

        PatientSummary patientDto = new PatientSummary();
        patientDto.setPhoneNumber(prescriptions.getRecord().getPatient().getUser().getPhoneNumber());
        patientDto.setPatientCode(prescriptions.getRecord().getPatient().getPatientCode());
        patientDto.setFullName(prescriptions.getRecord().getPatient().getUser().getFullname());
        patientDto.setDateOfBirth(prescriptions.getRecord().getPatient().getUser().getDateOfBirth());
        dto.setPatient(patientDto);
        return dto;
    }

    // HIỂN THỊ XÉT NGHIỆM MÀ NHÂN VIÊN ĐẢM NHIỆM THEO BỘ LỌC VỚI PHÂN TRANG
    public PaginatedResponseDTO<PrescriptionOfStaffResponse> searchPrescriptionOfPharmacist(PrescriptionWaitingRequest request, Integer currentUserId) {
        //0.Lấy thông tin PharmacyStaff
        PharmacyStaff pharmacyStaff = pharmacyStaffRepo.findByUserId(currentUserId);
        if(pharmacyStaff == null){
            throw new InvalidInputException("Nhân viên nhà thuốc không tồn tại.");
        }

        // 1. Chuẩn bị phân trang và sắp xếp
        Sort sort = Sort.by(Sort.Direction.fromString(request.getSortDir()), request.getSortBy());
        Pageable pageable = PageRequest.of(request.getPage(), request.getSize(), sort);

        // 2. Xây dựng Specification (logic lọc)
        Specification<Prescriptions> spec = PrescriptionSpecification.filterPrescriptions(request,null, pharmacyStaff.getId());

        // 3. Thực hiện truy vấn
        Page<Prescriptions> prescriptionsPage = prescriptionRepo.findAll(spec, pageable);

        // 4. Ánh xạ (Mapping) Entity sang Response DTO
        List<PrescriptionOfStaffResponse> responsePrescription = prescriptionsPage.getContent().stream()
                .map(this::covertToPrescriptionOfStaffResponse) // Sử dụng hàm covertToResponse để chuyển đổi
                .collect(Collectors.toList());

        // 5. Trả về Paginated Response
        return new PaginatedResponseDTO<PrescriptionOfStaffResponse>(
                prescriptionsPage.getNumber(),
                prescriptionsPage.getSize(),
                prescriptionsPage.getTotalElements(),
                prescriptionsPage.getTotalPages(),
                responsePrescription
        );
    }

    // Hàm chuyển đổi từ Prescriptions entity sang PrescriptionOfStaffResponse DTO
    private PrescriptionOfStaffResponse covertToPrescriptionOfStaffResponse(Prescriptions prescriptions) {
        PrescriptionOfStaffResponse dto = new PrescriptionOfStaffResponse();
        dto.setPrescriptionId(prescriptions.getId());
        dto.setPrescriptionCode(prescriptions.getCode());
        dto.setRecordCode(prescriptions.getRecord().getCode());
        dto.setRequestedDate(prescriptions.getPrescriptionDate());
        dto.setDoctorInChargeName(prescriptions.getDoctor().getStaff().getUser().getFullname());
        dto.setStatus(prescriptions.getStatus().name());
        dto.setDipenseDate(prescriptions.getDispensedAt());

        PatientSummary patientDto = new PatientSummary();
        patientDto.setPhoneNumber(prescriptions.getRecord().getPatient().getUser().getPhoneNumber());
        patientDto.setPatientCode(prescriptions.getRecord().getPatient().getPatientCode());
        patientDto.setFullName(prescriptions.getRecord().getPatient().getUser().getFullname());
        patientDto.setDateOfBirth(prescriptions.getRecord().getPatient().getUser().getDateOfBirth());
        dto.setPatient(patientDto);
        return dto;
    }

    // Lấy Đơn thuốc theo ID prescription cho nhân viên nhà thuốc
    public PrescriptionDetailsOfStaffRp getPrescriptionByPrescriptionId(Integer prescriptionId) {
        Prescriptions prescription = prescriptionRepo.findById(prescriptionId).orElseThrow(
                () -> new InvalidInputException("Đơn thuốc này không tồn tại"));

        List<PrescriptionDetails> details = detailRepo.findAllByPrescription(prescription);

        return convertToResponseOfStaffDTO(prescription, details);
    }

    // Hàm chuyển đổi từ Prescriptions entity sang PrescriptionDetailsOfStaffRp DTO
    private PrescriptionDetailsOfStaffRp convertToResponseOfStaffDTO(Prescriptions p, List<PrescriptionDetails> details) {
        // 1. Ánh xạ các chi tiết thuốc (Details)
        List<PrescriptionDetailsResponse> detailDtos = details.stream()
                .map(this::mapDetailToDTO)
                .collect(Collectors.toList());

        // 2. Ánh xạ Đơn thuốc chính (Prescriptions)
        PrescriptionDetailsOfStaffRp response = new PrescriptionDetailsOfStaffRp();

        response.setPrescriptionId(p.getId());
        response.setPrescriptionCode(p.getCode());
        response.setTotalDays(p.getTotalDays());

        PatientSummary patientDto = new PatientSummary();
        patientDto.setPhoneNumber(p.getRecord().getPatient().getUser().getPhoneNumber());
        patientDto.setPatientCode(p.getRecord().getPatient().getPatientCode());
        patientDto.setFullName(p.getRecord().getPatient().getUser().getFullname());
        patientDto.setDateOfBirth(p.getRecord().getPatient().getUser().getDateOfBirth());
        response.setPatient(patientDto);

        // Gán danh sách chi tiết đã ánh xạ
        response.setDetails(detailDtos);
        return response; // Dữ liệu trả về
    }
}
