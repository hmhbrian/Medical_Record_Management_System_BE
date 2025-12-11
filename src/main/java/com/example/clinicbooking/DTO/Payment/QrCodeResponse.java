package com.example.clinicbooking.DTO.Payment;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

/**
 * DTO trả về thông tin mã QR cho thanh toán chuyển khoản
 * Frontend sử dụng data này để hiển thị mã QR cho bệnh nhân quét
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class QrCodeResponse {

    /**
     * URL của ảnh QR Code (định dạng data URL hoặc HTTP URL)
     * Frontend có thể hiển thị trực tiếp trong thẻ <img src="...">
     * 
     * Ví dụ:
     * "https://img.vietqr.io/image/970415-0123456789-compact.png?amount=100000&addInfo=PAY001"
     */
    private String qrDataUrl;

    /**
     * Số tiền cần thanh toán (VND)
     * Hiển thị cho bệnh nhân biết số tiền cần chuyển khoản
     */
    private BigDecimal amount;

    /**
     * Nội dung chuyển khoản (payment code)
     * Đây là chuỗi duy nhất để nhận diện giao dịch khi webhook callback
     * 
     * Format: PAY-{paymentId} hoặc payment.getPaymentCode()
     * Ví dụ: "PAY-00123" hoặc "HD20231210001"
     */
    private String transactionContent;

    /**
     * Tên ngân hàng nhận tiền
     * Ví dụ: "Ngân hàng TMCP Kỹ Thương Việt Nam (Techcombank)"
     */
    private String bankName;

    /**
     * Số tài khoản ngân hàng nhận tiền
     * Ví dụ: "0123456789"
     */
    private String accountNumber;

    /**
     * Tên chủ tài khoản
     * Ví dụ: "PHONG KHAM DA KHOA ABC"
     */
    private String accountName;

    /**
     * Mã ngân hàng (BIN code) theo chuẩn Napas
     * Ví dụ: "970415" (Vietinbank), "970422" (MB Bank)
     * Danh sách đầy đủ: https://api.vietqr.io/v2/banks
     */
    private String bankCode;
}
