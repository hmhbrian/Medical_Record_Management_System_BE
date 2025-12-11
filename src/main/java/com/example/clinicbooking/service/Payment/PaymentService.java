package com.example.clinicbooking.service.Payment;

import com.example.clinicbooking.DTO.MedicalRecord.ServiceData.ServiceDetail;
import com.example.clinicbooking.DTO.PaginatedResponseDTO;
import com.example.clinicbooking.DTO.Payment.*;
import com.example.clinicbooking.DTO.Payment.DetailForAdmin.InvoiceItemDTO;
import com.example.clinicbooking.DTO.Payment.DetailForAdmin.InvoiceSummaryResponse;
import com.example.clinicbooking.DTO.Payment.DetailForCashier.ItemPaymentDetail;
import com.example.clinicbooking.DTO.Payment.DetailForCashier.PaymentDetailResponse;
import com.example.clinicbooking.entity.*;
import com.example.clinicbooking.exceptions.InvalidInputException;
import com.example.clinicbooking.repository.*;
import com.example.clinicbooking.security.CustomUserDetails;
import com.example.clinicbooking.service.InvoiceService;
import lombok.RequiredArgsConstructor;
import net.sf.jasperreports.engine.*;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.annotation.Propagation;

import java.io.InputStream;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PaymentService {
    private final PaymentRepository paymentRepo;
    private final PaymentDetailRepository paymentDetailRepo;
    private final InvoiceService invoiceService;
    private final CashierRepository cashierRepo;
    private final MedicalRecordRepository medicalRecordRepos;
    private final ResultExaminationRepository resultExaminationRepo;
    private final PrescriptionRepository prescriptionRepo;
    private final ImagingTestsRepository imagingTestsRepo;
    private final LabTestsRepository labTestRepo;
    private final UserRepository userRepo;
    private final AppointmentStatusRepository appointmentStatusRepo;

    // ===== THÊM MỚI: Configuration cho SePay/VietQR =====
    private final com.example.clinicbooking.config.SepayConfig sepayConfig;

    /**
     * logic tạo/cập nhật Payment và PaymentDetail.
     * 
     * @param record       Hồ sơ bệnh án
     * @param serviceItems Danh sách dịch vụ cần thanh toán
     * @return Payment Entity đã được lưu/cập nhật
     */
    @Transactional(propagation = Propagation.REQUIRED) // Đảm bảo nằm trong giao dịch lớn
    public Payment createPaymentOrder(MedicalRecord record, List<ServiceDetail> serviceItems) {

        // 1. Tính toán Tổng tiền ước tính
        BigDecimal totalEstimatedAmount = serviceItems.stream()
                .map(item -> BigDecimal.valueOf(item.price()))
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        // 2. TÍNH TOÁN BHYT VÀ BỆNH NHÂN TRẢ (Giả sử logic phức tạp hơn ở đây)
        // Lấy tỷ lệ BHYT của bệnh nhân
        double patientInsuranceRate = record.getPatient().getInsuranceRate();

        BigDecimal bhytCovered = BigDecimal.ZERO;
        BigDecimal patientOwed = totalEstimatedAmount;

        // --- 2a. TÍNH TOÁN CHO TỪNG ITEM (Thay vì chỉ tính tổng) ---
        List<PaymentDetail> paymentDetails = new ArrayList<>();
        int scale = 0; // scale = 0 cho VND
        RoundingMode roundingMode = RoundingMode.HALF_UP;

        // Cần tổng hợp lại tổng tiền BHYT và BN trả cho Payment chính
        BigDecimal totalInsuranceCovered = BigDecimal.ZERO;
        BigDecimal totalPatientOwed = BigDecimal.ZERO;

        for (ServiceDetail item : serviceItems) {
            BigDecimal servicePrice = BigDecimal.valueOf(item.price());
            double insuranceRateService = 0.8; // Giả định tỷ lệ hỗ trợ BHYT của dịch vụ (cần lấy từ catalog)

            // Lấy tỷ lệ hỗ trợ BHYT áp dụng thấp hơn
            double finalRateDouble = Math.min(insuranceRateService, patientInsuranceRate);
            BigDecimal insuranceRate = new BigDecimal(finalRateDouble);

            // Tính số tiền BHYT chi trả
            BigDecimal itemInsuranceCovered = servicePrice
                    .multiply(insuranceRate)
                    .setScale(scale, roundingMode);

            // Tính số tiền bệnh nhân phải trả
            BigDecimal itemPatientOwed = servicePrice.subtract(itemInsuranceCovered);

            totalInsuranceCovered = totalInsuranceCovered.add(itemInsuranceCovered);
            totalPatientOwed = totalPatientOwed.add(itemPatientOwed);

            // Tạo PaymentDetail
            PaymentDetail detail = new PaymentDetail();
            // payment sẽ được gán sau khi payment được lưu
            detail.setServiceType(item.serviceType());
            detail.setServiceId(item.serviceId());
            detail.setDescription(item.description());
            detail.setTotalAmount(servicePrice);
            detail.setCreatedAt(LocalDateTime.now());
            detail.setInsuranceCoveredAmount(itemInsuranceCovered);
            detail.setPatientPaidAmount(itemPatientOwed);

            paymentDetails.add(detail);
        }

        bhytCovered = totalInsuranceCovered;
        patientOwed = totalPatientOwed;

        // 3. TẠO MỚI Payment (Luôn tạo mới vì là trả trước/trả sau độc lập)
        Payment newpayment = new Payment();
        newpayment.setCreatedAt(LocalDateTime.now());
        newpayment.setRecord(record);
        newpayment.setTotalAmount(totalEstimatedAmount);
        newpayment.setInsuranceCoverage(bhytCovered);
        newpayment.setPatientPayment(patientOwed);
        newpayment.setStatus(PaymentStatus.PENDING_PAYMENT);

        // 5. Lưu Payment (để có ID gán cho PaymentDetail)
        newpayment = paymentRepo.save(newpayment);

        // 6. Lưu PaymentDetails
        for (PaymentDetail detail : paymentDetails) {
            detail.setPayment(newpayment);
        }
        paymentDetailRepo.saveAll(paymentDetails);

        return newpayment;
    }

    // Tìm kiếm và phân trang hồ sơ ngoại trú theo các tiêu chí
    public PaginatedResponseDTO<PaymentResponse> searchPayment(PaymentSearchRequest request) {

        // 1. Chuẩn bị phân trang và sắp xếp
        Sort sort = Sort.by(Sort.Direction.fromString(request.getSortDir()), request.getSortBy());
        Pageable pageable = PageRequest.of(request.getPage(), request.getSize(), sort);

        // 2. Xây dựng Specification (logic lọc)
        Specification<Payment> spec = PaymentSpecification.filterPayments(request);

        // 3. Thực hiện truy vấn
        Page<Payment> paymentsPage = paymentRepo.findAll(spec, pageable);

        // 4. Ánh xạ (Mapping) Entity sang Response DTO
        List<PaymentResponse> responsePayment = paymentsPage.getContent().stream()
                .map(this::covertToResponse) // Sử dụng hàm covertToResponse để chuyển đổi
                .collect(Collectors.toList());

        // 5. Trả về Paginated Response
        return new PaginatedResponseDTO<PaymentResponse>(
                paymentsPage.getNumber(),
                paymentsPage.getSize(),
                paymentsPage.getTotalElements(),
                paymentsPage.getTotalPages(),
                responsePayment);
    }

    // Chuyển đổi Payment entity sang PaymentResponse DTO
    private PaymentResponse covertToResponse(Payment payment) {
        PaymentResponse dto = new PaymentResponse();
        dto.setPaymentId(payment.getId());
        dto.setPaymentCode(payment.getPaymentCode());
        dto.setRecordCode(payment.getRecord().getCode());
        dto.setPatientId(payment.getRecord().getPatient().getId());
        dto.setPatientCode(payment.getRecord().getPatient().getPatientCode());
        dto.setPatientName(payment.getRecord().getPatient().getUser().getFullname());
        dto.setPatientDateOfBirth(payment.getRecord().getPatient().getUser().getDateOfBirth());
        dto.setStatus(payment.getStatus().name());
        dto.setTotalAmount(payment.getTotalAmount());
        dto.setInsuranceCoverage(payment.getInsuranceCoverage());
        dto.setPatientPaymentTotal(payment.getPatientPayment());
        dto.setCreatedAt(payment.getCreatedAt());
        return dto;
    }

    // Lấy chi tiết của một phiếu thanh toán theo ID.
    public PaymentDetailResponse getPaymentDetails(Integer paymentId) {
        // 1. Lấy thông tin Payment và MedicalRecord/Patient
        Payment payment = paymentRepo.findById(paymentId)
                .orElseThrow(() -> new InvalidInputException("Phiếu thanh toán không tồn tại với ID: " + paymentId));

        User patientUser = payment.getRecord().getPatient().getUser();
        String patientName = patientUser.getFullname();
        Integer recordId = payment.getRecord().getId();

        // 2. Lấy danh sách chi tiết PaymentDetails
        List<PaymentDetail> details = paymentDetailRepo.findAllByPayment(payment);

        // 3. Mapping chi tiết PaymentDetail sang DTO
        List<ItemPaymentDetail> itemDetails = details.stream()
                .map(this::mapToPaymentDetailResponse)
                .collect(Collectors.toList());

        // 4. Tạo đối tượng Response cuối cùng
        PaymentDetailResponse response = new PaymentDetailResponse();
        response.setPaymentId(payment.getId());
        response.setPaymentCode(payment.getPaymentCode());

        response.setRecordCode(payment.getRecord().getCode());
        response.setPatientCode(payment.getRecord().getPatient().getPatientCode());
        response.setPatientName(payment.getRecord().getPatient().getUser().getFullname());
        response.setStatus(payment.getStatus().name());
        response.setTotalAmount(payment.getTotalAmount());
        response.setInsuranceCoverage(payment.getInsuranceCoverage());
        response.setPatientPaymentTotal(payment.getPatientPayment());

        response.setCreatedAt(payment.getCreatedAt());
        response.setPaidAt(payment.getPaymentDate());
        response.setPaymentMethod(payment.getPaymentMethod());
        response.setItemPayments(itemDetails);
        return response;
    }

    // Hàm mapping một PaymentDetail Entity sang PaymentDetailResponse DTO
    private ItemPaymentDetail mapToPaymentDetailResponse(PaymentDetail detail) {
        ItemPaymentDetail dto = new ItemPaymentDetail();
        dto.setItemId(detail.getId());
        dto.setServiceType(detail.getServiceType());
        dto.setServiceId(detail.getServiceId());
        dto.setDescription(detail.getDescription());
        dto.setGrossAmount(detail.getTotalAmount());
        dto.setInsuranceCoverage(detail.getInsuranceCoveredAmount());
        dto.setPatientPaymentTotal(detail.getPatientPaidAmount());
        return dto;
    }

    // Thực hiện thanh toán.
    @Transactional
    public Payment processPayment(Integer paymentId, PaymentProcessRequest request) {

        // 0.Lấy id cashier từ user đang đăng nhập
        var auth = SecurityContextHolder.getContext().getAuthentication();
        if (!(auth.getPrincipal() instanceof CustomUserDetails cud)) {
            throw new AccessDeniedException("Unauthorized");
        }
        // Lấy thông tin Cashier
        Integer cashierId = cashierRepo.findIdByUserId(cud.getId());
        Cashier cashier = cashierRepo.findById(cashierId)
                .orElseThrow(() -> new InvalidInputException("Nhân viên thu ngân không tồn tại."));

        // 1. Kiểm tra và Lấy Payment
        Payment payment = paymentRepo.findById(paymentId)
                .orElseThrow(() -> new InvalidInputException("Phiếu thanh toán không tồn tại."));

        // 2. Xác thực Trạng thái
        if (!PaymentStatus.PENDING_PAYMENT.equals(payment.getStatus())) {
            throw new InvalidInputException("Phiếu thanh toán đã được xử lý hoặc đã hủy.");
        }

        // 3. (Tùy chọn) Kiểm tra số tiền: Đảm bảo số tiền trả >= số tiền bệnh nhân nợ
        BigDecimal patientDue = payment.getPatientPayment(); // Số tiền bệnh nhân nợ (đã tính BHYT)
        BigDecimal actualPaid = request.getActualPaidAmount().setScale(0, RoundingMode.HALF_UP); // Làm tròn (VND)

        // kiểm tra số tiền thanh toán
        if (actualPaid.compareTo(patientDue) < 0) {
            throw new InvalidInputException(
                    "Số tiền thanh toán không đủ. Bệnh nhân còn nợ: " + patientDue.subtract(actualPaid));
        }

        // 4. Cập nhật Payment
        payment.setPaymentMethod(request.getPaymentMethod());
        payment.setCashier(cashier);
        payment.setActualPaidAmount(actualPaid);
        payment.setPaymentDate(LocalDateTime.now()); // Ghi nhận thời điểm thanh toán
        payment.setStatus(PaymentStatus.PAID);

        // Ghi lại số tiền hoàn lại (Refund) nếu bệnh nhân trả thừa
        if (actualPaid.compareTo(patientDue) > 0) {
            BigDecimal refundAmount = actualPaid.subtract(patientDue);
            // THỰC HIỆN LOGIC GHI NHẬN HOÀN TIỀN VÀO BẢNG RIÊNG (Nếu cần)
            // Ví dụ: logRefundTransaction(payment, refundAmount, request.getCashierId());
            payment.setNotes("Hoàn lại: " + refundAmount.toString());
        }

        Payment updatedPayment = paymentRepo.save(payment);

        // Khởi tạo cờ để kiểm tra nhu cầu chờ kết quả
        boolean requiresPendingResults = false;
        List<PaymentDetail> paymentDetail = paymentDetailRepo.findAllByPayment(payment);

        // CẬP NHẬT TRẠNG THÁI DỊCH VỤ
        for (PaymentDetail detail : paymentDetail) {
            String serviceType = detail.getServiceType();
            Integer serviceId = detail.getServiceId();

            if (ServiceType.IMAGING_TEST.name().equals(serviceType)) {
                ImagingTests imagingTests = imagingTestsRepo.findById(serviceId)
                        .orElseThrow(() -> new InvalidInputException(
                                "Dịch vụ hình ảnh không tồn tại với id: " + detail.getServiceId()));
                imagingTests.setStatus(ServiceStatus.PAID);
                imagingTestsRepo.save(imagingTests);
                // Đặt cờ: Hồ sơ này cần chờ kết quả
                requiresPendingResults = true;
            } else if (ServiceType.LAB_TEST.name().equals(serviceType)) {
                LabTests labTests = labTestRepo.findById(detail.getServiceId())
                        .orElseThrow(() -> new InvalidInputException(
                                "Dịch vụ xét nghiệm không tồn tại với id: " + detail.getServiceId()));
                labTests.setStatus(ServiceStatus.PAID);
                labTestRepo.save(labTests);
                // Đặt cờ: Hồ sơ này cần chờ kết quả
                requiresPendingResults = true;
            } else if (ServiceType.EXAMINATION.name().equals(serviceType)) {
                ResultExamination resultExamination = resultExaminationRepo.findById(detail.getServiceId())
                        .orElseThrow(() -> new InvalidInputException(
                                "Dịch vụ khám bệnh không tồn tại với id: " + detail.getServiceId()));
                resultExamination.setStatus(ServiceStatus.PAID);
                resultExaminationRepo.save(resultExamination);
            } else if (ServiceType.PRESCRIPTION.name().equals(serviceType)) {
                Prescriptions prescriptions = prescriptionRepo.findById(detail.getServiceId())
                        .orElseThrow(() -> new InvalidInputException(
                                "Không có đơn thuốc tồn tại với id: " + detail.getServiceId()));
                prescriptions.setStatus(PrescriptionStatus.PAID);
                prescriptionRepo.save(prescriptions);
            }
        }

        // CẬP NHẬT TRẠNG THÁI HỒ SƠ CUỐI CÙNG
        MedicalRecord record = payment.getRecord();

        if (requiresPendingResults) {
            // Nếu có bất kỳ dịch vụ nào cần chờ kết quả, ưu tiên trạng thái này
            record.setStatus(MedicalRecordStatus.PENDING_RESULTS);
        } else {
            // Nếu không có dịch vụ nào cần chờ kết quả, hồ sơ được coi là hoàn tất
            record.setStatus(MedicalRecordStatus.COMPLETED);

            // Cập nhật trạng thái cuộc hẹn sau khi hoàn thành đơn thuốc(hoàn tất khám)
            Appointment appointment = record.getAppointment();
            if (appointment != null) {
                User cudUser = userRepo.findById(cud.getId())
                        .orElseThrow(() -> new InvalidInputException("User not found with ID: " + cud.getId()));
                AppointmentStatus appointmentStatus = new AppointmentStatus();
                appointmentStatus.setStatus(5); // Trạng thái "Đã hoàn thành"
                appointmentStatus.setAppointment(appointment);
                appointmentStatus.setUpdateAt(LocalDateTime.now());
                appointmentStatus.setUpdate_by(cudUser);
                appointmentStatusRepo.save(appointmentStatus);
            }
        }

        medicalRecordRepos.save(record);

        return updatedPayment;
    }

    // Hàm tạo Báo cáo Hóa đơn dưới dạng PDF (JasperReports)
    public byte[] generateInvoiceReport(Integer paymentId) throws JRException {

        // 1. Lấy và Xác thực Dữ liệu (Dùng logic getPaymentDetails đã có)
        Payment payment = paymentRepo.findById(paymentId)
                .orElseThrow(() -> new InvalidInputException("Phiếu thanh toán không tồn tại."));

        if (!PaymentStatus.PAID.equals(payment.getStatus())) {
            throw new InvalidInputException("Hóa đơn chỉ có thể xuất khi thanh toán đã hoàn tất.");
        }

        // Kiểm tra và Gán số hóa đơn nếu chưa có
        if (payment.getInvoiceNumber() == null || payment.getInvoiceNumber().isEmpty()) {

            // GỌI INVOICE SERVICE ĐỂ TẠO SỐ MỚI
            String newInvoiceNumber = invoiceService.generateNextInvoiceNumber();
            String newInvoiceSerial = invoiceService.getCurrentInvoiceSerial();

            payment.setInvoiceNumber(newInvoiceNumber);
            payment.setInvoiceSerial(newInvoiceSerial);
            payment.setIsInvoiceIssued(true); // Đánh dấu đã phát hành
            payment = paymentRepo.save(payment);
        }

        // Lấy chi tiết dịch vụ (Cần DTO đã được mapping ở bước trước)
        List<PaymentDetail> details = paymentDetailRepo.findAllByPayment(payment);

        // Tạo danh sách DTO chi tiết để đưa vào JasperReports
        List<ItemPaymentDetail> detailDataList = details.stream()
                .map(this::mapToPaymentDetailResponse) // Sử dụng hàm mapping đã tạo
                .collect(Collectors.toList());

        // 2. Chuẩn bị Tham số (Parameters) cho Phần Header/Summary
        Map<String, Object> parameters = new HashMap<>();
        parameters.put("P_INVOICE_NUMBER", payment.getInvoiceNumber()); // Mã hóa đơn đã cấp
        parameters.put("P_PATIENT_NAME", payment.getRecord().getPatient().getUser().getFullname());
        parameters.put("P_PAYMENT_DATE",
                payment.getPaymentDate().format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm")));

        // Dữ liệu Tổng kết
        parameters.put("P_TOTAL_AMOUNT", payment.getTotalAmount());
        parameters.put("P_BHYT_COVERAGE", payment.getInsuranceCoverage());
        parameters.put("P_PATIENT_DUE", payment.getPatientPayment());

        // 3. Gọi Hàm Tạo Báo cáo
        return generateReport(detailDataList, parameters);
    }

    // Hàm thực hiện việc Biên dịch và Xuất PDF
    private byte[] generateReport(List<ItemPaymentDetail> details, Map<String, Object> parameters) throws JRException {
        // 1. Load và Biên dịch Template
        InputStream templateStream = Thread.currentThread().getContextClassLoader()
                .getResourceAsStream("reports/invoice_template.jrxml");
        if (templateStream == null) {
            // Ném ngoại lệ rõ ràng nếu không tìm thấy template
            throw new InvalidInputException("Invoice Template file not found in resources/reports/");
        }
        JasperReport jasperReport = JasperCompileManager.compileReport(templateStream);

        // 2. Chuẩn bị Data Source cho Detail Band (Dùng danh sách chi tiết)
        JRDataSource dataSource = new JRBeanCollectionDataSource(details);

        // 3. Điền dữ liệu và Xuất PDF
        JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, parameters, dataSource);
        return JasperExportManager.exportReportToPdf(jasperPrint);
    }

    public List<InvoiceSummaryResponse> getInvoicesDetail(MedicalRecord record) {

        // 1. Lấy tất cả các Payments liên quan đến recordId
        List<Payment> payments = paymentRepo.findAllByRecord(record);

        return payments.stream()
                .map(payment -> {
                    // Lấy chi tiết PaymentDetail cho từng Payment
                    List<PaymentDetail> details = paymentDetailRepo.findAllByPayment(payment);

                    // Map Payment sang InvoiceSummaryDTO
                    InvoiceSummaryResponse invoiceDto = new InvoiceSummaryResponse();
                    invoiceDto.setPaymentId(payment.getId());
                    invoiceDto.setPaymentCode(payment.getPaymentCode());
                    invoiceDto.setPaymentStatus(payment.getStatus().name());
                    invoiceDto.setTotalAmount(payment.getTotalAmount());
                    invoiceDto.setPatientPaid(payment.getPatientPayment());
                    invoiceDto.setInsuranceCoverage(payment.getInsuranceCoverage());
                    invoiceDto.setPaymentDate(payment.getPaymentDate());
                    if (payment.getCashier() != null
                            && payment.getCashier().getStaff().getUser().getFullname() != null) {
                        invoiceDto.setCashierName(payment.getCashier().getStaff().getUser().getFullname()); // Lấy tên
                                                                                                            // thu ngân
                        invoiceDto.setCashierCode(payment.getCashier().getCashierCode());
                    } else {
                        invoiceDto.setCashierName("Chưa phân công");
                    }

                    // Map PaymentDetail sang InvoiceItemDTO
                    List<InvoiceItemDTO> itemDtos = details.stream()
                            .map(detail -> {
                                InvoiceItemDTO item = new InvoiceItemDTO();
                                item.setServiceType(detail.getServiceType());
                                item.setDescription(detail.getDescription());
                                item.setInsuranceCoverage(detail.getInsuranceCoveredAmount());
                                item.setPatientPaymentTotal(detail.getPatientPaidAmount());
                                item.setTotalAmount(detail.getTotalAmount());
                                return item;
                            })
                            .collect(Collectors.toList());

                    invoiceDto.setItems(itemDtos);
                    return invoiceDto;
                })
                .collect(Collectors.toList());
    }

    // ============================================
    // QR CODE PAYMENT VỚI VIETQR/SEPAY
    // ============================================

    /**
     * Tạo mã QR VietQR cho thanh toán chuyển khoản
     * 
     * CÁCH HOẠT ĐỘNG:
     * 1. Lấy thông tin Cashier đang đăng nhập
     * 2. Gán Cashier vào Payment (lưu thông tin người phụ trách)
     * 3. Lấy thông tin Payment từ database
     * 4. Tạo URL QR Code sử dụng VietQR API (https://vietqr.io)
     * 5. Trả về thông tin QR cho frontend hiển thị
     * 
     * LƯU Ý QUAN TRỌNG:
     * - Cashier sẽ được GÁN VÀO PAYMENT ngay khi tạo QR
     * - Khi webhook callback, Payment đã có sẵn thông tin Cashier phụ trách
     * 
     * VietQR API Format:
     * https://img.vietqr.io/image/{BANK_CODE}-{ACCOUNT_NUMBER}-{TEMPLATE}.png?amount={AMOUNT}&addInfo={CONTENT}
     * 
     * @param paymentId ID của phiếu thanh toán
     * @return QrCodeResponse chứa URL QR và thông tin ngân hàng
     * @throws InvalidInputException nếu payment không tồn tại hoặc đã thanh toán
     */
    public QrCodeResponse generateQrCode(Integer paymentId) {
        // 1. Kiểm tra xem có cấu hình SePay chưa
        if (!sepayConfig.isConfigured()) {
            throw new InvalidInputException(
                    "Chưa cấu hình SePay. Vui lòng thêm thông tin vào application.properties: " +
                            "sepay.api.key, sepay.account.number, sepay.bank.code");
        }

        // 2. Lấy thông tin Cashier đang đăng nhập
        // QUAN TRỌNG: Lưu thông tin Cashier phụ trách ngay khi tạo QR
        var auth = SecurityContextHolder.getContext().getAuthentication();
        if (!(auth.getPrincipal() instanceof CustomUserDetails cud)) {
            throw new AccessDeniedException("Unauthorized - Chỉ Cashier mới được tạo mã QR");
        }

        // Lấy thông tin Cashier từ userId
        Integer cashierId = cashierRepo.findIdByUserId(cud.getId());
        Cashier cashier = cashierRepo.findById(cashierId)
                .orElseThrow(() -> new InvalidInputException("Nhân viên thu ngân không tồn tại."));

        // 3. Lấy thông tin Payment
        Payment payment = paymentRepo.findById(paymentId)
                .orElseThrow(() -> new InvalidInputException("Phiếu thanh toán không tồn tại với ID: " + paymentId));

        // 4. Kiểm tra trạng thái Payment (chỉ cho phép tạo QR với PENDING_PAYMENT)
        if (!PaymentStatus.PENDING_PAYMENT.equals(payment.getStatus())) {
            throw new InvalidInputException("Phiếu thanh toán này đã được xử lý hoặc đã hủy.");
        }

        // 5. GÁN CASHIER VÀO PAYMENT (lưu thông tin người phụ trách)
        // Khi webhook callback, Payment sẽ có sẵn thông tin Cashier
        payment.setCashier(cashier);
        paymentRepo.save(payment);

        System.out.println("=== QR CODE GENERATED ===");
        System.out.println("Payment: " + payment.getPaymentCode());
        System.out.println("Cashier: " + cashier.getStaff().getUser().getFullname());

        // 6. Lấy số tiền bệnh nhân cần trả (sau khi trừ BHYT)
        BigDecimal amount = payment.getPatientPayment();

        // 7. Tạo nội dung chuyển khoản (transaction content)
        // Format: Sử dụng paymentCode hoặc tạo mã duy nhất
        // VÍ DỤ: "PAY00123" hoặc "HD20231210001"
        String transactionContent = payment.getPaymentCode() != null
                ? payment.getPaymentCode()
                : "PAY" + String.format("%05d", paymentId);

        // 8. Tạo URL QR Code sử dụng VietQR API
        // Template options: "compact" (nhỏ gọn), "compact2", "print" (in ấn)
        String template = "compact2";
        String qrUrl = String.format(
                "https://img.vietqr.io/image/%s-%s-%s.png?amount=%s&addInfo=%s&accountName=%s",
                sepayConfig.getBankCode(),
                sepayConfig.getAccountNumber(),
                template,
                amount.longValue(), // VietQR API chỉ nhận số nguyên (VND không có phần thập phân)
                transactionContent,
                sepayConfig.getAccountName());

        // 9. Tạo Response DTO
        QrCodeResponse response = new QrCodeResponse();
        response.setQrDataUrl(qrUrl);
        response.setAmount(amount);
        response.setTransactionContent(transactionContent);
        response.setBankCode(sepayConfig.getBankCode());
        response.setBankName(sepayConfig.getBankName());
        response.setAccountNumber(sepayConfig.getAccountNumber());
        response.setAccountName(sepayConfig.getAccountName());

        return response;
    }

    /**
     * Xử lý webhook callback từ SePay khi có giao dịch chuyển khoản
     * 
     * FLOW:
     * 1. Bệnh nhân quét QR và chuyển khoản
     * 2. SePay nhận thông báo từ ngân hàng
     * 3. SePay gửi POST request tới endpoint này
     * 4. Parse nội dung chuyển khoản để lấy paymentCode
     * 5. Tìm Payment tương ứng
     * 6. Cập nhật status = PAID
     * 7. Cập nhật trạng thái hồ sơ và dịch vụ (giống logic processPayment)
     * 
     * LƯU Ý:
     * - Endpoint này phải public (không cần authentication)
     * - Nên verify signature từ SePay (nếu có) để tránh fake webhook
     * - Xử lý idempotent (tránh xử lý trùng lặp nếu SePay gửi nhiều lần)
     * 
     * @param request Data từ SePay webhook
     * @return true nếu xử lý thành công
     * @throws InvalidInputException nếu data không hợp lệ
     */
    @Transactional
    public boolean handleSepayWebhook(SepayWebhookRequest request) {
        try {
            // 1. Log thông tin webhook để debug (nên dùng logger thay vì sysout trong
            // production)
            System.out.println("=== SEPAY WEBHOOK RECEIVED ===");
            System.out.println("Reference Code: " + request.getReferenceCode());
            System.out.println("Content: " + request.getContent());
            System.out.println("Amount: " + request.getTransferAmount());
            System.out.println("Type: " + request.getTransferType());

            // 2. Chỉ xử lý giao dịch TIỀN VÀO (transferType = "in")
            // Lưu ý: SePay dùng lowercase "in", không phải "IN"
            if (!"in".equalsIgnoreCase(request.getTransferType())) {
                System.out.println("Bỏ qua giao dịch type = " + request.getTransferType());
                return false;
            }

            // 3. Parse nội dung chuyển khoản để lấy paymentCode
            // Ví dụ content: "PAY11- Ma GD ACSP/ 8H081982"
            // Lấy phần trước dấu gạch ngang hoặc dấu cách đầu tiên
            String content = request.getContent();
            if (content == null || content.trim().isEmpty()) {
                System.out.println("ERROR: Nội dung chuyển khoản rỗng");
                return false;
            }

            // FIXED: Split bởi dấu - hoặc space, lấy phần đầu tiên
            // "PAY11- Ma GD ACSP" → ["PAY11", " Ma GD ACSP"] → "PAY11"
            String paymentCode = content.trim().split("[-\\s]")[0].trim();
            System.out.println("Parsed paymentCode: " + paymentCode);

            // 4. Tìm Payment theo paymentCode
            Payment payment = paymentRepo.findByPaymentCode(paymentCode);
            if (payment == null) {
                System.out.println("ERROR: Không tìm thấy Payment với code: " + paymentCode);
                return false;
            }

            // 5. Kiểm tra trạng thái Payment (tránh xử lý trùng)
            if (payment.getStatus() == PaymentStatus.PAID) {
                System.out.println("WARNING: Payment đã được thanh toán trước đó, bỏ qua.");
                return true; // Trả về true vì đã xử lý rồi (idempotent)
            }

            if (payment.getStatus() != PaymentStatus.PENDING_PAYMENT) {
                System.out.println("ERROR: Payment status không hợp lệ: " + payment.getStatus());
                return false;
            }

            // 6. Kiểm tra số tiền (optional - có thể bỏ qua nếu tin tưởng SePay)
            BigDecimal expectedAmount = payment.getPatientPayment();
            BigDecimal receivedAmount = BigDecimal.valueOf(request.getTransferAmount());

            if (receivedAmount.compareTo(expectedAmount) < 0) {
                System.out.println("WARNING: Số tiền chuyển khoản ít hơn số tiền cần thanh toán");
                System.out.println("Expected: " + expectedAmount + ", Received: " + receivedAmount);
                // Optional: Có thể từ chối hoặc vẫn chấp nhận tùy business logic
            }

            // 7. Cập nhật Payment (tương tự processPayment)
            payment.setPaymentMethod("TRANSFER"); // Đánh dấu là chuyển khoản
            payment.setActualPaidAmount(receivedAmount);
            payment.setPaymentDate(LocalDateTime.now());
            payment.setStatus(PaymentStatus.PAID);
            payment.setNotes("Thanh toán qua QR - Reference: " + request.getReferenceCode());

            paymentRepo.save(payment);

            // 8. Cập nhật trạng thái dịch vụ (copy logic từ processPayment)
            boolean requiresPendingResults = false;
            List<PaymentDetail> paymentDetails = paymentDetailRepo.findAllByPayment(payment);

            for (PaymentDetail detail : paymentDetails) {
                String serviceType = detail.getServiceType();
                Integer serviceId = detail.getServiceId();

                if (ServiceType.IMAGING_TEST.name().equals(serviceType)) {
                    ImagingTests imagingTests = imagingTestsRepo.findById(serviceId)
                            .orElseThrow(() -> new InvalidInputException("Dịch vụ hình ảnh không tồn tại"));
                    imagingTests.setStatus(ServiceStatus.PAID);
                    imagingTestsRepo.save(imagingTests);
                    requiresPendingResults = true;
                } else if (ServiceType.LAB_TEST.name().equals(serviceType)) {
                    LabTests labTests = labTestRepo.findById(serviceId)
                            .orElseThrow(() -> new InvalidInputException("Dịch vụ xét nghiệm không tồn tại"));
                    labTests.setStatus(ServiceStatus.PAID);
                    labTestRepo.save(labTests);
                    requiresPendingResults = true;
                } else if (ServiceType.EXAMINATION.name().equals(serviceType)) {
                    ResultExamination resultExamination = resultExaminationRepo.findById(serviceId)
                            .orElseThrow(() -> new InvalidInputException("Dịch vụ khám bệnh không tồn tại"));
                    resultExamination.setStatus(ServiceStatus.PAID);
                    resultExaminationRepo.save(resultExamination);
                } else if (ServiceType.PRESCRIPTION.name().equals(serviceType)) {
                    Prescriptions prescriptions = prescriptionRepo.findById(serviceId)
                            .orElseThrow(() -> new InvalidInputException("Đơn thuốc không tồn tại"));
                    prescriptions.setStatus(PrescriptionStatus.PAID);
                    prescriptionRepo.save(prescriptions);
                }
            }

            // 9. Cập nhật trạng thái hồ sơ
            MedicalRecord record = payment.getRecord();
            if (requiresPendingResults) {
                record.setStatus(MedicalRecordStatus.PENDING_RESULTS);
            } else {
                record.setStatus(MedicalRecordStatus.COMPLETED);

                // Cập nhật trạng thái cuộc hẹn (nếu có)
                Appointment appointment = record.getAppointment();
                if (appointment != null) {
                    // Lưu ý: Webhook không có user context, dùng system user hoặc bỏ qua update_by
                    AppointmentStatus appointmentStatus = new AppointmentStatus();
                    appointmentStatus.setStatus(5); // Đã hoàn thành
                    appointmentStatus.setAppointment(appointment);
                    appointmentStatus.setUpdateAt(LocalDateTime.now());
                    // update_by có thể null hoặc tạo system user
                    appointmentStatusRepo.save(appointmentStatus);
                }
            }
            medicalRecordRepos.save(record);

            System.out.println("=== SUCCESS: Payment " + paymentCode + " đã được cập nhật ===");
            return true;

        } catch (Exception e) {
            System.err.println("ERROR xử lý SePay webhook: " + e.getMessage());
            e.printStackTrace();
            // Không throw exception để tránh SePay retry liên tục
            return false;
        }
    }
}
