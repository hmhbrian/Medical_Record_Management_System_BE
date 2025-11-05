package com.example.clinicbooking.service;

import com.example.clinicbooking.entity.InvoiceSequence;
import com.example.clinicbooking.repository.InvoiceSequenceRepository;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;

@Service
@AllArgsConstructor
public class InvoiceService {
    private final InvoiceSequenceRepository invoiceSequenceRepo;

    private static final String CURRENT_SERIAL = "AA/20E"; // Ký hiệu hóa đơn hiện tại
    /**
     * Cấp phát số hóa đơn mới một cách an toàn luồng (Thread-Safe).
     * @return Số hóa đơn đầy đủ (VD: AA/20E-2025-000101)
     */
    @Transactional
    public String generateNextInvoiceNumber() {
        String currentYear = String.valueOf(LocalDate.now().getYear());

        // 1. Tìm và KHÓA bản ghi sequence
        InvoiceSequence sequence = invoiceSequenceRepo
                .findAndLockByYearAndSerial(currentYear, CURRENT_SERIAL)
                .orElseGet(() -> {
                    // Nếu chưa tồn tại, tạo sequence mới (hoặc logic reset năm)
                    InvoiceSequence newSequence = new InvoiceSequence();
                    newSequence.setYear(currentYear);
                    newSequence.setSerial(CURRENT_SERIAL);
                    newSequence.setCurrentNumber(0);
                    return newSequence;
                });

        // 2. Tăng số thứ tự
        Integer nextNumber = sequence.getCurrentNumber() + 1;
        sequence.setCurrentNumber(nextNumber);

        // 3. Lưu lại bản ghi đã cập nhật (và mở khóa)
        invoiceSequenceRepo.save(sequence);

        // 4. Định dạng số hóa đơn
        String formattedNumber = String.format("%06d", nextNumber); // Định dạng 6 chữ số
        return CURRENT_SERIAL + "-" + currentYear + "-" + formattedNumber;
    }

    public String getCurrentInvoiceSerial() {
        return CURRENT_SERIAL;
    }
}
