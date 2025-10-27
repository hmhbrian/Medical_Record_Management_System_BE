package com.example.clinicbooking.service;

import com.example.clinicbooking.DTO.MedicalRecord.ServiceData.ServiceDetail;
import com.example.clinicbooking.entity.MedicalRecord;
import com.example.clinicbooking.entity.Payment;
import com.example.clinicbooking.entity.PaymentDetail;
import com.example.clinicbooking.repository.PaymentDetailRepository;
import com.example.clinicbooking.repository.PaymentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.annotation.Propagation;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class PaymentService {
    private final PaymentRepository paymentRepo;
    private final PaymentDetailRepository paymentDetailRepo;

    /**
     * logic tạo/cập nhật Payment và PaymentDetail.
     * @param record Hồ sơ bệnh án
     * @param serviceItems Danh sách dịch vụ cần thanh toán
     * @return Payment Entity đã được lưu/cập nhật
     */
    @Transactional(propagation = Propagation.REQUIRED) // Đảm bảo nằm trong giao dịch lớn
    public Payment handlePayment(MedicalRecord record, List<ServiceDetail> serviceItems) {

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

        // 3. Tìm kiếm Payment. Nếu không có thì tạo mới.
        Payment payment = paymentRepo.findByRecord(record).orElse(new Payment());

        // 4. Cập nhật Payment
        if (payment.getId() == null) { // Chỉ đặt createdAt nếu là Payment mới
            payment.setCreatedAt(LocalDateTime.now());
            payment.setRecord(record);
            payment.setTotalAmount(totalEstimatedAmount);
            payment.setInsuranceCoverage(bhytCovered);
            payment.setPatientPayment(patientOwed);
        }
        else{
            payment.setTotalAmount(payment.getTotalAmount().add(totalEstimatedAmount));
            payment.setInsuranceCoverage(payment.getInsuranceCoverage().add(bhytCovered));
            payment.setPatientPayment(payment.getPatientPayment().add(patientOwed));
        }
        payment.setStatus("PENDING_PAYMENT");

        // 5. Lưu Payment (để có ID gán cho PaymentDetail)
        payment = paymentRepo.save(payment);

        // 6. Lưu PaymentDetails
        for (PaymentDetail detail : paymentDetails) {
            detail.setPayment(payment);
        }
        paymentDetailRepo.saveAll(paymentDetails);

        return payment;
    }
}
